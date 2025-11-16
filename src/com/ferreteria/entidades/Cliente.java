package com.ferreteria.entidades;

import java.time.LocalDateTime;

/**
 * Entidad que representa un cliente de la ferretería.
 * Almacena la información personal y de contacto de los clientes,
 * incluyendo credenciales para el sistema de autenticación.
 * 
 * Los clientes pueden realizar compras y acceder al dashboard del cliente
 * para ver su historial de compras y realizar nuevas transacciones.
 */
public class Cliente {
    
    private int clienteId;                    // ID único del cliente
    private String dni;                       // Documento Nacional de Identidad
    private String nombre;                    // Nombre del cliente
    private String apellidos;                 // Apellidos del cliente
    private String direccion;                 // Dirección de residencia
    private String email;                     // Correo electrónico (usado para login)
    private String telefono;                  // Número de teléfono de contacto
    private String passwordHash;              // Contraseña encriptada con BCrypt
    private LocalDateTime fechaRegistro;     // Fecha y hora de registro en el sistema

    // Métodos getter y setter con comentarios descriptivos
    
    /**
     * Obtiene el ID único del cliente
     * @return ID del cliente
     */
    public int getClienteId() {
        return clienteId;
    }

    /**
     * Establece el ID único del cliente
     * @param clienteId ID del cliente a establecer
     */
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    /**
     * Obtiene el Documento Nacional de Identidad del cliente
     * @return DNI del cliente
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el Documento Nacional de Identidad del cliente
     * @param dni DNI del cliente a establecer
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del cliente
     * @return Nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente
     * @param nombre Nombre del cliente a establecer
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del cliente
     * @return Apellidos del cliente
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del cliente
     * @param apellidos Apellidos del cliente a establecer
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene la dirección de residencia del cliente
     * @return Dirección del cliente
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de residencia del cliente
     * @param direccion Dirección del cliente a establecer
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el correo electrónico del cliente
     * @return Correo electrónico del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente
     * @param email Correo electrónico del cliente a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono del cliente
     * @return Teléfono del cliente
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente
     * @param telefono Teléfono del cliente a establecer
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la contraseña encriptada del cliente
     * @return Contraseña encriptada del cliente
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece la contraseña encriptada del cliente
     * @param passwordHash Contraseña encriptada del cliente a establecer
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Obtiene la fecha y hora de registro del cliente en el sistema
     * @return Fecha y hora de registro
     */
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Establece la fecha y hora de registro del cliente en el sistema
     * @param fechaRegistro Fecha y hora de registro a establecer
     */
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}