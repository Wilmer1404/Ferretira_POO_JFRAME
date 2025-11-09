package com.ferreteria.entidades;

public class ProductoUnitario extends ItemVendible {

    private double precioUnitario;
    private int stockActual; 
    
    @Override
    public double calcularSubtotal(double cantidad) {
        return this.precioUnitario * (int) cantidad;
    }

    @Override
    public double obtenerStock() {
        return this.stockActual;
    }
    
    @Override
    public String obtenerUnidadParaGUI() {
        return "Unidades";
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
}