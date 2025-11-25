package com.ferreteria.negocio;

import org.mindrot.jbcrypt.BCrypt;
import com.ferreteria.datos.impl.EmpleadoDAOImpl;
import com.ferreteria.datos.interfaces.IEmpleadoDAO;
import com.ferreteria.entidades.Empleado;
import java.util.List;
import java.util.Optional;

/**
 * Clase de negocio para la gestión de empleados.
 * Implementa la lógica de negocio relacionada con empleados, incluyendo:
 * - Autenticación y login de empleados
 * - Gestión de empleados (crear, actualizar, desactivar)
 * - Validaciones de negocio y encriptación de contraseñas
 * - Control de roles y permisos
 * 
 * Esta clase es utilizada principalmente por el dashboard administrativo
 * para gestionar el personal de la ferretería.
 */
public class EmpleadoNegocio {

    private final IEmpleadoDAO DATOS_EMP; // DAO para acceso a datos de empleados

    /**
     * Constructor que inicializa el DAO de empleados
     */
    public EmpleadoNegocio() {
        this.DATOS_EMP = new EmpleadoDAOImpl();
    }

    /**
     * Método para autenticar un empleado en base a sus credenciales.
     * 
     * @param email El email del empleado
     * @param password La contraseña en texto plano del empleado
     * @return Un Optional con el empleado autenticado o vacío si las credenciales son inválidas
     */
    public Optional<Empleado> login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return Optional.empty();
        }

        Optional<Empleado> empleadoOpt = this.DATOS_EMP.buscarPorEmail(email);

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();

            if (BCrypt.checkpw(password, empleado.getPasswordHash()) && empleado.isActivo()) {
                return Optional.of(empleado);
            }
        }

        return Optional.empty();
    }

    /**
     * Lista todos los empleados.
     * 
     * @return Una lista con todos los empleados
     */
    public List<Empleado> listar() {
        return this.DATOS_EMP.listarTodos();
    }
    
    /**
     * Busca empleados que coincidan con un término dado.
     * 
     * @param termino El término a buscar (puede ser parte del nombre, email, etc.)
     * @return Una lista de empleados que coinciden con el término de búsqueda
     */
    public List<Empleado> buscar(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return this.listar();
        }
        return this.DATOS_EMP.buscar(termino.trim());
    }

    /**
     * Valida los datos de un empleado antes de ser insertado o actualizado.
     * 
     * @param emp El empleado a validar
     * @param passwordPlano La contraseña en texto plano (solo para inserción)
     * @return Un mensaje de error si hay un problema de validación, o null si es válido
     */
    private String validar(Empleado emp, String passwordPlano) {
        if (emp.getDni() == null || emp.getDni().trim().isEmpty()) {
            return "El DNI es obligatorio.";
        }
        if (emp.getNombre() == null || emp.getNombre().trim().isEmpty()) {
            return "El Nombre es obligatorio.";
        }
        if (emp.getEmail() == null || emp.getEmail().trim().isEmpty()) {
            return "El Email es obligatorio.";
        }
        if (passwordPlano == null || passwordPlano.trim().isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (emp.getRol() == null || (!emp.getRol().equals("ADMIN") && !emp.getRol().equals("VENDEDOR"))) {
            return "El Rol no es válido.";
        }
        return null;
    }

    /**
     * Inserta un nuevo empleado en el sistema.
     * 
     * @param emp El empleado a insertar
     * @param passwordPlano La contraseña en texto plano del empleado
     * @return Un mensaje de error si ocurre un problema, o null si se inserta correctamente
     */
    public String insertar(Empleado emp, String passwordPlano) {
        String error = validar(emp, passwordPlano);
        if (error != null) {
            return error;
        }

        if (this.DATOS_EMP.buscarPorEmail(emp.getEmail()).isPresent()) {
            return "El email ya se encuentra registrado.";
        }

        String hashGenerado = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        emp.setPasswordHash(hashGenerado);

        if (this.DATOS_EMP.insertar(emp)) {
            return null;
        } else {
            return "Error al insertar el empleado en la base de datos.";
        }
    }

    /**
     * Actualiza la información de un empleado existente.
     * 
     * @param emp El empleado con la información actualizada
     * @return Un mensaje de error si ocurre un problema, o null si se actualiza correctamente
     */
    public String actualizar(Empleado emp) {
        if (emp.getDni() == null || emp.getDni().trim().isEmpty()) {
            return "El DNI es obligatorio.";
        }
        if (emp.getNombre() == null || emp.getNombre().trim().isEmpty()) {
            return "El Nombre es obligatorio.";
        }
        if (emp.getEmail() == null || emp.getEmail().trim().isEmpty()) {
            return "El Email es obligatorio.";
        }

        if (this.DATOS_EMP.actualizar(emp)) {
            return null; // Éxito
        } else {
            return "Error al actualizar el empleado.";
        }
    }

    /**
     * Desactiva (elimina) un empleado del sistema.
     * 
     * @param id El ID del empleado a desactivar
     * @return Un mensaje de error si ocurre un problema, o null si se desactiva correctamente
     */
    public String desactivar(int id) {
        if (this.DATOS_EMP.eliminar(id)) {
            return null;
        } else {
            return "Error al desactivar el empleado.";
        }
    }
}
