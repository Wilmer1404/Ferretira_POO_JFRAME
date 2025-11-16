package com.ferreteria.entidades;

/**
 * Clase abstracta que representa un item que puede ser vendido en la ferretería.
 * Implementa el patrón Strategy para diferentes tipos de productos:
 * - ProductoUnitario: productos que se venden por unidades (tornillos, tuercas)
 * - ProductoAGranel: productos que se venden por peso/medida (cemento, arena)
 * - Servicio: servicios que ofrece la ferretería (instalaciones, reparaciones)
 * 
 * Esta clase define la estructura común y comportamientos que deben implementar
 * todos los items vendibles en el sistema.
 */
public abstract class ItemVendible {

    // Atributos comunes a todos los productos vendibles
    private int productoId;          // ID único del producto
    private String sku;              // Código de producto único (Stock Keeping Unit)
    private String nombre;           // Nombre descriptivo del producto
    private String descripcion;      // Descripción detallada del producto
    private Categoria categoria;     // Categoría a la que pertenece el producto
    private boolean activo;          // Estado del producto (activo/inactivo)

    /**
     * Método abstracto que calcula el subtotal de la venta basado en la cantidad.
     * Cada tipo de producto implementa su propia lógica de cálculo.
     * 
     * @param cantidad Cantidad del producto a vender
     * @return Subtotal calculado para la venta
     */
    public abstract double calcularSubtotal(double cantidad);

    /**
     * Método abstracto que obtiene el stock disponible del producto.
     * Cada tipo de producto maneja el stock de manera diferente.
     * 
     * @return Stock disponible del producto
     */
    public abstract double obtenerStock();

    /**
     * Método abstracto que retorna la unidad de medida para mostrar en la interfaz.
     * 
     * @return Unidad de medida del producto (Unidades, Kg, Litros, etc.)
     */
    public abstract String obtenerUnidadParaGUI();

    /**
     * Valida si hay suficiente stock para satisfacer la cantidad solicitada.
     * 
     * @param cantidadSolicitada Cantidad que se desea comprar
     * @return true si hay stock suficiente, false en caso contrario
     */
    public boolean validarStock(double cantidadSolicitada) {
        return this.obtenerStock() >= cantidadSolicitada;
    }

    // Métodos getter y setter para los atributos
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