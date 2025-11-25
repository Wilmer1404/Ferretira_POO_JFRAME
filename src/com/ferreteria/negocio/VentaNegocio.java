package com.ferreteria.negocio;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.impl.VentaDAOImpl;
import com.ferreteria.datos.impl.InventarioDAOImpl; 
import com.ferreteria.datos.interfaces.IVentaDAO;
import com.ferreteria.datos.interfaces.IInventarioDAO;
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
 * Coordina todo el proceso de venta incluyendo:
 * - Procesamiento de transacciones de venta
 * - Actualización automática de inventario
 * - Validación de stock y cálculos
 * - Generación de reportes de ventas
 * - Control de integridad transaccional
 * 
 * Esta clase es el núcleo del sistema de ventas y maneja la complejidad
 * de coordinar múltiples operaciones en una sola transacción.
 */
public class VentaNegocio {

    private final IVentaDAO DATOS_VENTA;            // DAO para acceso a datos de ventas
    private final ProductoNegocio PRODUCTO_NEGOCIO; // Negocio de productos para validaciones
    private final IInventarioDAO DATOS_INVENTARIO;  // DAO para manejo de inventario
    
    private static final Logger LOGGER = Logger.getLogger(VentaNegocio.class.getName());

    /**
     * Constructor que inicializa los DAOs y clases de negocio necesarias
     */
    public VentaNegocio() {
        this.DATOS_VENTA = new VentaDAOImpl();
        this.PRODUCTO_NEGOCIO = new ProductoNegocio();
        
        this.DATOS_INVENTARIO = new InventarioDAOImpl(); 
    }

    /**
     * Listar todas las ventas registradas en el sistema
     * @return Lista de objetos Venta
     */
    public List<Venta> listar() {
        return this.DATOS_VENTA.listarTodos();
    }

    /**
     * Listar ventas en un rango de fechas
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de objetos Venta
     */
    public List<Venta> listarPorFechas(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            return this.DATOS_VENTA.listarTodos();
        }
        return this.DATOS_VENTA.listarPorFechas(inicio, fin);
    }

    /**
     * Buscar una venta por su ID
     * @param id ID de la venta
     * @return Objeto Venta si existe, vacío en caso contrario
     */
    public Optional<Venta> buscarPorId(int id) {
        Venta venta = this.DATOS_VENTA.buscarPorId(id);
        return Optional.ofNullable(venta);
    }

    /**
     * Listar ventas realizadas a un cliente en específico
     * @param clienteId ID del cliente
     * @return Lista de objetos Venta
     */
    public List<Venta> listarPorCliente(int clienteId) {
        if (clienteId <= 0) {
            return new ArrayList<>();
        }
        return this.DATOS_VENTA.listarPorCliente(clienteId);
    }

    /**
     * Insertar una nueva venta en el sistema
     * @param venta Objeto Venta con los datos de la venta a insertar
     * @return Mensaje de error en caso de fallo, null si fue exitosa
     */
    public String insertar(Venta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            return "El carrito está vacío, no se puede procesar la venta.";
        }
        
        final int empleadoId = (venta.getEmpleado() != null) ? venta.getEmpleado().getEmpleadoId() : 1; // 1 = Admin por defecto

        Connection conn = null;
        try {
            conn = Conexion.obtenerConexion();
            conn.setAutoCommit(false); 

            for (DetalleVenta det : venta.getDetalles()) {
                ItemVendible itemEnDB = this.PRODUCTO_NEGOCIO.buscarPorId(det.getItem().getProductoId());
                if (itemEnDB == null) {
                    throw new RuntimeException("El producto '" + det.getItem().getNombre() + "' ya no existe.");
                }
                if (!itemEnDB.validarStock(det.getCantidad())) {
                    throw new RuntimeException("Stock insuficiente para '" + itemEnDB.getNombre() + "'. Stock actual: " + itemEnDB.obtenerStock());
                }
                det.setItem(itemEnDB); 
            }

            int ventaId = this.DATOS_VENTA.insertar(venta, conn);

            for (DetalleVenta det : venta.getDetalles()) {
                
                String tipo = "SERVICIO";
                if (det.getItem() instanceof ProductoUnitario) {
                    tipo = "UNITARIO";
                } else if (det.getItem() instanceof ProductoAGranel) {
                    tipo = "GRANEL";
                }

                double cantidadVenta = det.getCantidad() * -1.0; 

                this.PRODUCTO_NEGOCIO.actualizarStock(
                    det.getItem().getProductoId(),
                    cantidadVenta, 
                    tipo,
                    conn
                );
                
                if (!tipo.equals("SERVICIO")) {
                        this.DATOS_INVENTARIO.registrarMovimientoVenta(det, ventaId, empleadoId, conn);
                }
            }

            conn.commit();
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
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar la conexión.", ex);
                }
            }
        }
    }
}