package com.ferreteria.entidades;

public class DetalleCompra {
    private int detalleCompraId;
    private ItemVendible item; 
    private double cantidad;
    private double precioCompra;
    private double subtotal;
    

    public int getDetalleCompraId() {
        return detalleCompraId;
    }

    public void setDetalleCompraId(int detalleCompraId) {
        this.detalleCompraId = detalleCompraId;
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

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
}