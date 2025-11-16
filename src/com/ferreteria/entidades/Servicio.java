package com.ferreteria.entidades;

/**
 * Clase que representa servicios que ofrece la ferretería.
 * Ejemplos: instalación de pisos, reparación de plomería, asesoría técnica, etc.
 * 
 * Los servicios tienen características especiales:
 * - Stock ilimitado (siempre disponibles)
 * - Se venden por cantidad de servicios prestados
 * - Tienen una tarifa fija por servicio
 */
public class Servicio extends ItemVendible {

    private double tarifaServicio;  // Precio fijo por cada servicio prestado

    /**
     * Calcula el subtotal multiplicando la tarifa del servicio por la cantidad.
     * La cantidad se convierte a entero ya que los servicios se cuentan por unidades.
     * 
     * @param cantidad Número de servicios a prestar (se redondea hacia abajo)
     * @return Subtotal de la venta (tarifa × cantidad de servicios)
     */
    @Override
    public double calcularSubtotal(double cantidad) {
        return this.tarifaServicio * (int) cantidad;
    }

    /**
     * Los servicios tienen stock infinito ya que no se consumen físicamente.
     * 
     * @return Infinito positivo indicando disponibilidad ilimitada
     */
    @Override
    public double obtenerStock() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Retorna la descripción de unidad para mostrar en la interfaz gráfica.
     * 
     * @return "Servicio" como texto descriptivo para la GUI
     */
    @Override
    public String obtenerUnidadParaGUI() {
        return "Servicio";
    }
    
    /**
     * Sobrescribe la validación de stock para siempre retornar true.
     * Los servicios siempre están disponibles independientemente de la cantidad.
     * 
     * @param cantidadSolicitada Cantidad de servicios solicitados (no se usa)
     * @return Siempre true ya que los servicios tienen disponibilidad ilimitada
     */
    @Override
    public boolean validarStock(double cantidadSolicitada) {
        return true; 
    }

    // Métodos getter y setter específicos del servicio
    public double getTarifaServicio() {
        return tarifaServicio;
    }

    public void setTarifaServicio(double tarifaServicio) {
        this.tarifaServicio = tarifaServicio;
    }
}