package com.ferreteria.entidades;

/**
 * Clase que representa un producto que se vende por unidades individuales.
 * Ejemplos: tornillos, tuercas, clavos, herramientas, etc.
 * 
 * Hereda de ItemVendible e implementa la lógica específica para productos
 * que se manejan con un stock de unidades enteras y un precio por unidad.
 */
public class ProductoUnitario extends ItemVendible {

    private double precioUnitario;  // Precio por unidad individual del producto
    private int stockActual;        // Cantidad de unidades disponibles en inventario
    
    /**
     * Calcula el subtotal multiplicando el precio unitario por la cantidad de unidades.
     * La cantidad se convierte a entero ya que solo se pueden vender unidades completas.
     * 
     * @param cantidad Número de unidades a vender (se redondea hacia abajo)
     * @return Subtotal de la venta (precio unitario × cantidad)
     */
    @Override
    public double calcularSubtotal(double cantidad) {
        return this.precioUnitario * (int) cantidad;
    }

    /**
     * Retorna el stock actual disponible en unidades.
     * 
     * @return Número de unidades disponibles en inventario
     */
    @Override
    public double obtenerStock() {
        return this.stockActual;
    }
    
    /**
     * Retorna la unidad de medida para mostrar en la interfaz gráfica.
     * 
     * @return "Unidades" como texto descriptivo para la GUI
     */
    @Override
    public String obtenerUnidadParaGUI() {
        return "Unidades";
    }

    // Métodos getter y setter específicos del producto unitario
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