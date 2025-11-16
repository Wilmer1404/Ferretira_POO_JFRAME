package com.ferreteria.entidades;

import java.util.Objects;

public class Proveedor {

    private int proveedorId;
    private String razonSocial;
    private String ruc;
    private String email;
    private String telefono;
    private String direccion;
    private boolean activo;

    // --- MEJORA 1: Constructores ---
    /**
     * Constructor vacío (buena práctica tenerlo explícito).
     */
    public Proveedor() {
    }

    /**
     * Constructor para crear un proveedor nuevo (sin ID, la BD lo asigna).
     */
    public Proveedor(String razonSocial, String ruc, String email, String telefono, String direccion, boolean activo) {
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = activo;
    }

    /**
     * Constructor completo (para cuando lees desde la BD).
     */
    public Proveedor(int proveedorId, String razonSocial, String ruc, String email, String telefono, String direccion, boolean activo) {
        this.proveedorId = proveedorId;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = activo;
    }

    // --- MEJORA 2: Método toString() ---
    /**
     * Define cómo se mostrará este objeto en un JComboBox.
     * Esta es la solución principal para tu problema del ComboBox.
     */
    @Override
    public String toString() {
        // Tu JComboBox usa un ID 0 para el texto "Seleccione..."
        if (this.proveedorId == 0) {
            return this.razonSocial; // Devuelve "Seleccione un Proveedor"
        }
        
        // Para proveedores reales, muestra el RUC y la Razón Social
        return this.ruc + " - " + this.razonSocial;
    }

    // --- MEJORA 3: equals() y hashCode() ---
    /**
     * Permite que los objetos Proveedor se comparen correctamente.
     * Esencial si los usas en Colecciones como HashSets o HashMaps.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // Solo compara proveedores por su ID único.
        final Proveedor other = (Proveedor) obj;
        
        // Si el ID es 0, es un objeto nuevo que no ha sido guardado,
        // por lo que no puede ser "igual" a otro (a menos que sea la misma instancia, ya chequeado arriba).
        if (this.proveedorId == 0) {
            return false;
        }
        
        return this.proveedorId == other.proveedorId;
    }

    @Override
    public int hashCode() {
        // Genera un hashCode basado en el ID único.
        return Objects.hash(this.proveedorId);
    }

    // --- Getters y Setters (Tu código original) ---
    public int getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(int proveedorId) {
        this.proveedorId = proveedorId;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}