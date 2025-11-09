package com.ferreteria.entidades;

public class Servicio extends ItemVendible {

    private double tarifaServicio;

    @Override
    public double calcularSubtotal(double cantidad) {

        return this.tarifaServicio * (int) cantidad;
    }

    @Override
    public double obtenerStock() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public String obtenerUnidadParaGUI() {
        return "Servicio";
    }
    
    @Override
    public boolean validarStock(double cantidadSolicitada) {
        return true; 
    }

    public double getTarifaServicio() {
        return tarifaServicio;
    }

    public void setTarifaServicio(double tarifaServicio) {
        this.tarifaServicio = tarifaServicio;
    }
}