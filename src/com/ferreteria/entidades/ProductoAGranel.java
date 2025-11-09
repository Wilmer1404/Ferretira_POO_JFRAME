package com.ferreteria.entidades;

public class ProductoAGranel extends ItemVendible {

    private double precioPorMedida; 
    private double stockMedido; 
    private String unidadMedida; 


    @Override
    public double calcularSubtotal(double cantidad) {
        return this.precioPorMedida * cantidad;
    }

    @Override
    public double obtenerStock() {
        return this.stockMedido;
    }

    @Override
    public String obtenerUnidadParaGUI() {
        return this.unidadMedida;
    }

    public double getPrecioPorMedida() {
        return precioPorMedida;
    }

    public void setPrecioPorMedida(double precioPorMedida) {
        this.precioPorMedida = precioPorMedida;
    }

    public double getStockMedido() {
        return stockMedido;
    }

    public void setStockMedido(double stockMedido) {
        this.stockMedido = stockMedido;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}