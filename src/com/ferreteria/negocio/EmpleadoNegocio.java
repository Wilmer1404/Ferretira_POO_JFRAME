package com.ferreteria.negocio;

import com.ferreteria.datos.impl.EmpleadoDAOImpl;
import com.ferreteria.datos.interfaces.IEmpleadoDAO;
import com.ferreteria.entidades.Empleado;
import java.util.List;
import java.util.Optional;

public class EmpleadoNegocio {
    
    private final IEmpleadoDAO DATOS_EMP;

    public EmpleadoNegocio() {
        this.DATOS_EMP = new EmpleadoDAOImpl(); 
    }

    public Optional<Empleado> login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Empleado> empleadoOpt = this.DATOS_EMP.buscarPorEmail(email);
        
        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            
            if (empleado.getPasswordHash().equals(password) && empleado.isActivo()) {
                return Optional.of(empleado); 
            }
        }
        
        return Optional.empty(); 
    }

    public List<Empleado> listar() {
        return this.DATOS_EMP.listarTodos();
    }

    private String validar(Empleado emp) {
        if (emp.getDni() == null || emp.getDni().trim().isEmpty()) {
            return "El DNI es obligatorio.";
        }
         if (emp.getNombre() == null || emp.getNombre().trim().isEmpty()) {
            return "El Nombre es obligatorio.";
        }
        if (emp.getEmail() == null || emp.getEmail().trim().isEmpty()) {
            return "El Email es obligatorio.";
        }
         if (emp.getPasswordHash() == null || emp.getPasswordHash().trim().isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (emp.getRol() == null || (!emp.getRol().equals("ADMIN") && !emp.getRol().equals("VENDEDOR"))) {
            return "El Rol no es válido.";
        }
        return null;
    }

    public String insertar(Empleado emp) {
        String error = validar(emp);
        if (error != null) {
            return error;
        }
        
        // Validación de negocio (que no exista)
        if (this.DATOS_EMP.buscarPorEmail(emp.getEmail()).isPresent()) {
            return "El email ya se encuentra registrado.";
        }
        
        if (this.DATOS_EMP.insertar(emp)) {
            return null; // Éxito
        } else {
            return "Error al insertar el empleado en la base de datos.";
        }
    }


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


    public String desactivar(int id) {
        if (this.DATOS_EMP.eliminar(id)) { 
            return null; 
        } else {
            return "Error al desactivar el empleado.";
        }
    }
}