package com.ferreteria.entidades;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una transacción de venta en la ferretería.
 * Registra la información general de la venta incluyendo el cliente,
 * empleado que atendió, método de pago y el detalle de productos vendidos.
 * 
 * Una venta está compuesta por:
 * - Información de cabecera (cliente, empleado, fecha, total)
 * - Lista de detalles de venta (productos específicos vendidos)
 */
public class Venta {
    
    private int ventaId;                        // ID único de la venta
    private Cliente cliente;                    // Cliente que realizó la compra
    private Empleado empleado;                  // Empleado que atendió la venta
    private LocalDateTime fechaVenta;           // Fecha y hora de la venta
    private double total;                       // Monto total de la venta
    private String metodoPago;                  // Método de pago (efectivo, tarjeta, etc.)
    private String referenciaTransaccion;       // Referencia o número de transacción
    
    private List<DetalleVenta> detalles;        // Lista de productos vendidos en esta venta

    // Métodos getter y setter con documentación
    
    /**
     * Obtiene el ID único de la venta
     * @return ID de la venta
     */
    public int getVentaId() {
        return ventaId;
    }

    /**
     * Establece el ID único de la venta
     * @param ventaId ID a asignar a la venta
     */
    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    /**
     * Obtiene el cliente que realizó la compra
     * @return Cliente de la venta
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente que realizó la compra
     * @param cliente Cliente a asignar a la venta
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el empleado que atendió la venta
     * @return Empleado de la venta
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * Establece el empleado que atendió la venta
     * @param empleado Empleado a asignar a la venta
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la venta
     * @return Fecha y hora de la venta
     */
    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    /**
     * Establece la fecha y hora de la venta
     * @param fechaVenta Fecha y hora a asignar a la venta
     */
    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    /**
     * Obtiene el monto total de la venta
     * @return Total de la venta
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el monto total de la venta
     * @param total Monto a asignar como total de la venta
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Obtiene el método de pago utilizado en la venta
     * @return Método de pago de la venta
     */
    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago de la venta
     * @param metodoPago Método de pago a asignar a la venta
     */
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene la referencia de la transacción de pago
     * @return Referencia de transacción
     */
    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    /**
     * Establece la referencia de la transacción de pago
     * @param referenciaTransaccion Referencia a asignar a la transacción
     */
    public void setReferenciaTransaccion(String referenciaTransaccion) {
        this.referenciaTransaccion = referenciaTransaccion;
    }

    /**
     * Obtiene la lista de detalles de venta (productos vendidos)
     * @return Lista de detalles de venta
     */
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    /**
     * Establece la lista de detalles de venta
     * @param detalles Lista de detalles a asignar a la venta
     */
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
    
    
}