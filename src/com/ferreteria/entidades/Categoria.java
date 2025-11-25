package com.ferreteria.entidades;

/**
 * Entidad que representa una categoría de productos en la ferretería.
 * Las categorías permiten organizar y clasificar los productos para
 * facilitar la búsqueda y gestión del inventario.
 * 
 * Ejemplos: Herramientas, Materiales de Construcción, Plomería, Electricidad, etc.
 */
public class Categoria {
    private int categoriaId;      // ID único de la categoría
    private String nombre;        // Nombre de la categoría
    private String descripcion;   // Descripción detallada de la categoría

    /**
     * Constructor por defecto
     */
    public Categoria() {
    }

    /**
     * Constructor con parámetros para crear una categoría completa
     * 
     * @param categoriaId ID de la categoría
     * @param nombre Nombre de la categoría
     * @param descripcion Descripción de la categoría
     */
    public Categoria(int categoriaId, String nombre, String descripcion) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.categoriaId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Categoria other = (Categoria) obj;
        return this.categoriaId == other.categoriaId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}