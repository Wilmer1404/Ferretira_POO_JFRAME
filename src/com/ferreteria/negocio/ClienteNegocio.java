package com.ferreteria.negocio;

import org.mindrot.jbcrypt.BCrypt;

import com.ferreteria.datos.impl.ClienteDAOImpl;
import com.ferreteria.datos.interfaces.IClienteDAO;
import com.ferreteria.entidades.Cliente;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Clase de negocio para la gestión de clientes.
 * Implementa la lógica de negocio relacionada con clientes, incluyendo:
 * - Autenticación y login de clientes
 * - Registro de nuevos clientes
 * - Validaciones de negocio
 * - Encriptación de contraseñas
 * 
 * Esta clase actúa como intermediaria entre la capa de presentación
 * y la capa de datos, aplicando reglas de negocio y validaciones.
 */
public class ClienteNegocio {

    private final IClienteDAO DATOS_CLI; // DAO para acceso a datos de clientes

    /**
     * Constructor que inicializa el DAO de clientes
     */
    public ClienteNegocio() {
        this.DATOS_CLI = new ClienteDAOImpl();
    }

    /**
     * Método para autenticar un cliente en base a sus credenciales
     * @param email El email del cliente
     * @param password La contraseña en texto plano del cliente
     * @return Un Optional que contiene el cliente autenticado o está vacío si las credenciales son inválidas
     */
    public Optional<Cliente> login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return Optional.empty();
        }

        Optional<Cliente> clienteOpt = this.DATOS_CLI.buscarPorEmail(email);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            if (BCrypt.checkpw(password, cliente.getPasswordHash())) {
                return Optional.of(cliente);
            }
        }

        return Optional.empty();
    }

    /**
     * Busca un cliente por su DNI
     * @param dni El DNI del cliente a buscar
     * @return Un Optional que contiene el cliente encontrado o está vacío si no se encuentra
     */
    public Optional<Cliente> buscarPorDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return Optional.empty();
        }
        return this.DATOS_CLI.buscarPorDni(dni.trim());
    }

    /**
     * Registra un nuevo cliente en el sistema
     * @param cliente El cliente a registrar
     * @param passwordPlano La contraseña en texto plano del cliente
     * @return Un mensaje de error en caso de fallo, o null si el registro fue exitoso
     */
    public String registrar(Cliente cliente, String passwordPlano) {
        if (cliente.getDni() == null || cliente.getDni().length() < 8) {
            return "El DNI debe tener al menos 8 caracteres.";
        }
        if (passwordPlano == null || passwordPlano.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }

        if (this.DATOS_CLI.buscarPorDni(cliente.getDni()).isPresent()) {
            return "El DNI ingresado ya está registrado.";
        }

        if (this.DATOS_CLI.buscarPorEmail(cliente.getEmail()).isPresent()) {
            return "El Email ingresado ya está registrado.";
        }

        String hashGenerado = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        cliente.setPasswordHash(hashGenerado);

        cliente.setFechaRegistro(LocalDateTime.now());

        if (this.DATOS_CLI.insertar(cliente)) {
            return null;
        } else {
            return "Error desconocido al guardar en la base de datos.";
        }
    }
}
