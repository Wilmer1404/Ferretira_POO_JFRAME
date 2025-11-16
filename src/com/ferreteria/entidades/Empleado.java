package com.ferreteria.entidades;

/**
 * Entidad que representa un empleado de la ferretería.
 * Almacena la información personal, credenciales y rol del empleado.
 * 
 * Los empleados pueden acceder al sistema según su rol:
 * - ADMIN: Acceso completo al dashboard administrativo
 * - VENDEDOR: Acceso a funciones de ventas
 * 
 * Utiliza encriptación BCrypt para almacenar contraseñas de forma segura.
 */
public class Empleado {

    private int empleadoId;         // ID único del empleado
    private String dni;             // Documento Nacional de Identidad
    private String nombre;          // Nombre del empleado
    private String apellidos;       // Apellidos del empleado
    private String email;           // Correo electrónico (usado para login)
    private String passwordHash;    // Contraseña encriptada con BCrypt
    private String rol;             // Rol del empleado (ADMIN, VENDEDOR, etc.)
    private boolean activo;         // Estado del empleado (activo/inactivo)

    // Métodos getter y setter con documentación

    /**
     * Obtiene el ID único del empleado
     * @return ID del empleado
     */
    public int getEmpleadoId() {
        return empleadoId;
    }

    /**
     * Establece el ID único del empleado
     * @param empleadoId ID a establecer
     */
    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    /**
     * Obtiene el DNI del empleado
     * @return DNI del empleado
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del empleado
     * @param dni DNI a establecer
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del empleado
     * @return Nombre del empleado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del empleado
     * @param nombre Nombre a establecer
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del empleado
     * @return Apellidos del empleado
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del empleado
     * @param apellidos Apellidos a establecer
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico del empleado
     * @return Correo electrónico del empleado
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del empleado
     * @param email Correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña encriptada del empleado
     * @return Contraseña encriptada
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece la contraseña encriptada del empleado
     * @param passwordHash Contraseña a establecer
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Obtiene el rol del empleado
     * @return Rol del empleado
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del empleado
     * @param rol Rol a establecer
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Verifica si el empleado está activo
     * @return true si está activo, false si está inactivo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado del empleado (activo/inactivo)
     * @param activo true para establecer como activo, false para inactivo
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}