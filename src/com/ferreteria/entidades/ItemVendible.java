package com.ferreteria.entidades;

public abstract class ItemVendible {

    private int productoId;
    private String sku;
    private String nombre;
    private String descripcion;
    private Categoria categoria; 
    private boolean activo;

    public abstract double calcularSubtotal(double cantidad);

    public abstract double obtenerStock();

    public abstract String obtenerUnidadParaGUI();

    public boolean validarStock(double cantidadSolicitada) {
        return this.obtenerStock() >= cantidadSolicitada;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}