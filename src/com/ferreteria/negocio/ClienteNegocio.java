package com.ferreteria.negocio;

import org.mindrot.jbcrypt.BCrypt;

import com.ferreteria.datos.impl.ClienteDAOImpl;
import com.ferreteria.datos.interfaces.IClienteDAO;
import com.ferreteria.entidades.Cliente;
import java.time.LocalDateTime;
import java.util.Optional;

public class ClienteNegocio {

    private final IClienteDAO DATOS_CLI;

    public ClienteNegocio() {
        this.DATOS_CLI = new ClienteDAOImpl();
    }

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

    public Optional<Cliente> buscarPorDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return Optional.empty();
        }
        return this.DATOS_CLI.buscarPorDni(dni.trim());
    }

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
