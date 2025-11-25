package com.ferreteria.entidades;

/**
 * Clase que representa un producto que se vende a granel por peso o medida.
 * Ejemplos: cemento (por kg), arena (por m³), pintura (por litros), etc.
 * 
 * Hereda de ItemVendible e implementa la lógica específica para productos
 * que se miden en unidades continuas (peso, volumen, longitud, etc.)
 */
public class ProductoAGranel extends ItemVendible {

    private double precioPorMedida;  // Precio por unidad de medida (ej: precio por kg, por litro)
    private double stockMedido;      // Cantidad disponible en la unidad de medida correspondiente
    private String unidadMedida;     // Unidad de medida (kg, litros, metros, etc.)

    /**
     * Calcula el subtotal multiplicando el precio por medida por la cantidad exacta.
     * A diferencia de ProductoUnitario, aquí se permite cantidad decimal.
     * 
     * @param cantidad Cantidad a vender en la unidad de medida del producto
     * @return Subtotal de la venta (precio por medida × cantidad)
     */
    @Override
    public double calcularSubtotal(double cantidad) {
        return this.precioPorMedida * cantidad;
    }

    /**
     * Retorna el stock actual disponible en la unidad de medida correspondiente.
     * 
     * @return Cantidad disponible (ej: 100.5 kg, 25.75 litros)
     */
    @Override
    public double obtenerStock() {
        return this.stockMedido;
    }

    /**
     * Retorna la unidad de medida para mostrar en la interfaz gráfica.
     * 
     * @return Unidad de medida del producto (kg, litros, metros, etc.)
     */
    @Override
    public String obtenerUnidadParaGUI() {
        return this.unidadMedida;
    }

    // Métodos getter y setter específicos del producto a granel
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