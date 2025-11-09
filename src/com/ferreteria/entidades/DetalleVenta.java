package com.ferreteria.entidades;

public class DetalleVenta {
    
    private int detalleId;
    
    private ItemVendible item; 
    
    private double cantidad;
    private double precioHistorico;
    private double subtotal;
    

    public int getDetalleId() {
        return detalleId;
    }

    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }

    public ItemVendible getItem() {
        return item;
    }

    public void setItem(ItemVendible item) {
        this.item = item;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioHistorico() {
        return precioHistorico;
    }

    public void setPrecioHistorico(double precioHistorico) {
        this.precioHistorico = precioHistorico;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
}