package com.ferreteria.entidades;

/**
 * Entidad que representa un item específico comprado dentro de una compra.
 * Cada DetalleCompra corresponde a una línea en la orden de compra,
 * conteniendo información específica del producto adquirido.
 * 
 * Registra el precio de compra para calcular márgenes de ganancia
 * y controlar costos de inventario.
 */
public class DetalleCompra {
    private int detalleCompraId;     // ID único del detalle de compra
    private ItemVendible item;       // Producto o servicio comprado
    private double cantidad;         // Cantidad comprada del item
    private double precioCompra;     // Precio al que se compró el item
    private double subtotal;         // Subtotal calculado (cantidad × precio compra)

    // Métodos getter y setter con documentación

    /**
     * Obtiene el ID único del detalle de compra
     * @return ID del detalle
     */
    public int getDetalleCompraId() {
        return detalleCompraId;
    }

    /**
     * Establece el ID único del detalle de compra
     * @param detalleCompraId ID a asignar
     */
    public void setDetalleCompraId(int detalleCompraId) {
        this.detalleCompraId = detalleCompraId;
    }

    /**
     * Obtiene el item comprado (producto o servicio)
     * @return Item comprado
     */
    public ItemVendible getItem() {
        return item;
    }

    /**
     * Establece el item comprado
     * @param item Producto o servicio a asignar
     */
    public void setItem(ItemVendible item) {
        this.item = item;
    }

    /**
     * Obtiene la cantidad comprada del item
     * @return Cantidad comprada
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad comprada del item
     * @param cantidad Cantidad a asignar
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio al que se compró el item
     * @return Precio de compra
     */
    public double getPrecioCompra() {
        return precioCompra;
    }

    /**
     * Establece el precio al que se compró el item
     * @param precioCompra Precio a asignar
     */
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    /**
     * Obtiene el subtotal calculado para el detalle de compra
     * @return Subtotal (cantidad × precio compra)
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal para el detalle de compra
     * @param subtotal Subtotal a asignar
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
}