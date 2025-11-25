package com.ferreteria.negocio;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.impl.VentaDAOImpl;
import com.ferreteria.datos.impl.InventarioDAOImpl;
import com.ferreteria.datos.impl.ProductoDAOImpl;
import com.ferreteria.datos.interfaces.IVentaDAO;
import com.ferreteria.datos.interfaces.IInventarioDAO;
import com.ferreteria.datos.interfaces.IProductoDAO;
import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Venta;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de negocio para la gestión de ventas.
 * Se ha corregido para manejar transacciones manualmente evitando el cierre prematuro de la conexión.
 */
public class VentaNegocio {

    private final IVentaDAO DATOS_VENTA;
    private final IInventarioDAO DATOS_INVENTARIO;
    // CAMBIO: Usamos IProductoDAO en lugar de ProductoNegocio para control transaccional
    private final IProductoDAO DATOS_PRODUCTO; 
    
    private static final Logger LOGGER = Logger.getLogger(VentaNegocio.class.getName());

    public VentaNegocio() {
        this.DATOS_VENTA = new VentaDAOImpl();
        this.DATOS_INVENTARIO = new InventarioDAOImpl(); 
        this.DATOS_PRODUCTO = new ProductoDAOImpl(); // Inicializamos el DAO directo
    }

    public List<Venta> listar() {
        return this.DATOS_VENTA.listarTodos();
    }

    public List<Venta> listarPorFechas(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            return this.DATOS_VENTA.listarTodos();
        }
        return this.DATOS_VENTA.listarPorFechas(inicio, fin);
    }

    public Optional<Venta> buscarPorId(int id) {
        Venta venta = this.DATOS_VENTA.buscarPorId(id);
        return Optional.ofNullable(venta);
    }

    public List<Venta> listarPorCliente(int clienteId) {
        if (clienteId <= 0) {
            return new ArrayList<>();
        }
        return this.DATOS_VENTA.listarPorCliente(clienteId);
    }

    public String insertar(Venta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            return "El carrito está vacío, no se puede procesar la venta.";
        }
        
        final int empleadoId = (venta.getEmpleado() != null) ? venta.getEmpleado().getEmpleadoId() : 1; 

        Connection conn = null;
        try {
            conn = Conexion.obtenerConexion();
            conn.setAutoCommit(false); // Inicio de transacción

            // 1. Validar productos y stock USANDO LA CONEXIÓN (sin cerrarla)
            for (DetalleVenta det : venta.getDetalles()) {
                // CORRECCIÓN: Usamos DATOS_PRODUCTO pasando 'conn'
                ItemVendible itemEnDB = this.DATOS_PRODUCTO.buscarPorId(det.getItem().getProductoId(), conn);
                
                if (itemEnDB == null) {
                    throw new RuntimeException("El producto '" + det.getItem().getNombre() + "' ya no existe.");
                }
                
                // Validamos stock manualmente aquí ya que tenemos el objeto fresco de la BD
                double stockActual = itemEnDB.obtenerStock(); // Método polimórfico en ItemVendible
                if (stockActual < det.getCantidad()) {
                     throw new RuntimeException("Stock insuficiente para '" + itemEnDB.getNombre() + "'. Stock actual: " + stockActual);
                }
                
                det.setItem(itemEnDB); 
            }

            // 2. Insertar Venta
            int ventaId = this.DATOS_VENTA.insertar(venta, conn);

            // 3. Actualizar Stock y Registrar Movimiento
            for (DetalleVenta det : venta.getDetalles()) {
                
                String tipo = "SERVICIO";
                if (det.getItem() instanceof ProductoUnitario) {
                    tipo = "UNITARIO";
                } else if (det.getItem() instanceof ProductoAGranel) {
                    tipo = "GRANEL";
                }

                double cantidadVenta = det.getCantidad() * -1.0; 

                // CORRECCIÓN: Actualizar stock usando el DAO directo y la conexión abierta
                this.DATOS_PRODUCTO.actualizarStock(
                    det.getItem().getProductoId(),
                    cantidadVenta, 
                    tipo,
                    conn
                );
                
                if (!tipo.equals("SERVICIO")) {
                        this.DATOS_INVENTARIO.registrarMovimientoVenta(det, ventaId, empleadoId, conn);
                }
            }

            conn.commit(); // Confirmar transacción
            return null; 

        } catch (Exception e) { 
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error crítico. Falló el ROLLBACK.", ex);
                }
            }
            return "Error al registrar la Venta: " + e.getMessage();

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Cerramos la conexión SOLO al final
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar la conexión.", ex);
                }
            }
        }
    }
}