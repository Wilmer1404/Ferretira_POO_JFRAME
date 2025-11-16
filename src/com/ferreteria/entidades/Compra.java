package com.ferreteria.entidades;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una compra realizada por la ferretería a un proveedor.
 * Registra la adquisición de productos para reabastecer el inventario.
 * 
 * Una compra incluye:
 * - Información del proveedor que suministra los productos
 * - Empleado responsable de la transacción
 * - Fecha, total y observaciones de la compra
 * - Lista de detalles con los productos específicos comprados
 */
public class Compra {
    private int compraId;                    // ID único de la compra
    private Proveedor proveedor;             // Proveedor que suministra los productos
    private Empleado empleado;               // Empleado responsable de la compra
    private LocalDateTime fechaCompra;       // Fecha y hora de la compra
    private double total;                    // Monto total de la compra
    private String observaciones;            // Notas adicionales sobre la compra
    
    private List<DetalleCompra> detalles;    // Lista de productos comprados

    // Métodos getter y setter con documentación
    
    /**
     * Obtiene el ID único de la compra
     * @return ID de la compra
     */
    public int getCompraId() {
        return compraId;
    }

    /**
     * Establece el ID único de la compra
     * @param compraId ID a asignar a la compra
     */
    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    /**
     * Obtiene el proveedor de la compra
     * @return Proveedor que suministra los productos
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Establece el proveedor de la compra
     * @param proveedor Proveedor a asignar
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Obtiene el empleado responsable de la compra
     * @return Empleado que gestionó la compra
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * Establece el empleado responsable de la compra
     * @param empleado Empleado a asignar
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    /**
     * Obtiene la fecha y hora de la compra
     * @return Fecha de la compra
     */
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Establece la fecha y hora de la compra
     * @param fechaCompra Fecha a asignar
     */
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /**
     * Obtiene el monto total de la compra
     * @return Total de la compra
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el monto total de la compra
     * @param total Monto total a asignar
     */
    public void setTotal(double total) {
        this.total = total;
    }
    
    /**
     * Obtiene las observaciones adicionales de la compra
     * @return Observaciones de la compra
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece observaciones adicionales sobre la compra
     * @param observaciones Notas o comentarios sobre la compra
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Obtiene la lista de detalles de la compra
     * @return Lista de detalles con los productos comprados
     */
    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    /**
     * Establece la lista de detalles de la compra
     * @param detalles Lista de detalles a asignar
     */
    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }
    
    
}