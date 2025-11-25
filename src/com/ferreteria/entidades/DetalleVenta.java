package com.ferreteria.entidades;

/**
 * Entidad que representa un item específico vendido dentro de una venta.
 * Cada DetalleVenta corresponde a una línea en la factura de venta,
 * conteniendo información específica del producto vendido.
 * 
 * Almacena el precio histórico al momento de la venta para mantener
 * la integridad de los datos ante cambios futuros de precios.
 */
public class DetalleVenta {
    
    private int detalleId;               // ID único del detalle de venta
    private ItemVendible item;           // Producto o servicio vendido
    private double cantidad;             // Cantidad vendida del item
    private double precioHistorico;      // Precio del item al momento de la venta
    private double subtotal;             // Subtotal calculado (cantidad × precio histórico)

    // Métodos getter y setter con documentación

    /**
     * Obtiene el ID único del detalle de venta
     * @return ID del detalle
     */
    public int getDetalleId() {
        return detalleId;
    }

    /**
     * Establece el ID único del detalle de venta
     * @param detalleId ID a asignar
     */
    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }

    /**
     * Obtiene el item vendido (producto o servicio)
     * @return Item vendido
     */
    public ItemVendible getItem() {
        return item;
    }

    /**
     * Establece el item vendido
     * @param item Producto o servicio a asignar
     */
    public void setItem(ItemVendible item) {
        this.item = item;
    }

    /**
     * Obtiene la cantidad vendida del item
     * @return Cantidad vendida
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad vendida del item
     * @param cantidad Cantidad a asignar
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio histórico del item al momento de la venta
     * @return Precio histórico
     */
    public double getPrecioHistorico() {
        return precioHistorico;
    }

    /**
     * Establece el precio histórico del item
     * @param precioHistorico Precio a asignar
     */
    public void setPrecioHistorico(double precioHistorico) {
        this.precioHistorico = precioHistorico;
    }

    /**
     * Obtiene el subtotal calculado para el detalle de venta
     * @return Subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal para el detalle de venta
     * @param subtotal Subtotal a asignar
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
}