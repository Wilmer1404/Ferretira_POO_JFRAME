package com.ferreteria.negocio;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.impl.CompraDAOImpl;
import com.ferreteria.datos.impl.InventarioDAOImpl;
import com.ferreteria.datos.impl.ProductoDAOImpl;
import com.ferreteria.datos.interfaces.ICompraDAO;
import com.ferreteria.datos.interfaces.IInventarioDAO;
import com.ferreteria.datos.interfaces.IProductoDAO;
import com.ferreteria.entidades.Compra;
import com.ferreteria.entidades.DetalleCompra;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompraNegocio {

    private final ICompraDAO DATOS_COMPRA;
    private final IInventarioDAO DATOS_INVENTARIO;
    private final IProductoDAO DATOS_PRODUCTO; // Usamos el DAO directamente
    
    private static final Logger LOGGER = Logger.getLogger(CompraNegocio.class.getName());

    public CompraNegocio() {
        this.DATOS_COMPRA = new CompraDAOImpl();
        this.DATOS_INVENTARIO = new InventarioDAOImpl();
        this.DATOS_PRODUCTO = new ProductoDAOImpl(); // Inicializamos
    }

    public String registrarCompra(Compra compra) {
        
        if (compra.getProveedor() == null || compra.getProveedor().getProveedorId() <= 0) {
            return "Debe seleccionar un Proveedor.";
        }
        if (compra.getEmpleado() == null || compra.getEmpleado().getEmpleadoId() <= 0) {
            return "No se ha identificado al Empleado que registra la compra.";
        }
        if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            return "La lista de productos de la compra está vacía.";
        }
        
        final int empleadoId = compra.getEmpleado().getEmpleadoId();

        Connection conn = null;
        try {
            conn = Conexion.obtenerConexion();
            conn.setAutoCommit(false); 

            // 1. Validar productos y recuperar datos completos USANDO LA MISMA CONEXIÓN
            for (DetalleCompra det : compra.getDetalles()) {
                // Usamos el DAO directamente pasando 'conn'
                ItemVendible itemEnDB = this.DATOS_PRODUCTO.buscarPorId(det.getItem().getProductoId(), conn);
                
                if (itemEnDB == null) {
                    throw new RuntimeException("El producto (ID: " + det.getItem().getProductoId() + ") ya no existe en el catálogo.");
                }
                det.setItem(itemEnDB);
            }

            // 2. Insertar Compra
            int compraId = this.DATOS_COMPRA.insertar(compra, conn);
            
            // 3. Procesar Detalles y Stock
            for (DetalleCompra det : compra.getDetalles()) {
                
                String tipoProducto = "SERVICIO";
                if (det.getItem() instanceof ProductoUnitario) {
                    tipoProducto = "UNITARIO";
                } else if (det.getItem() instanceof ProductoAGranel) {
                    tipoProducto = "GRANEL";
                }
                
                double cantidadPositiva = det.getCantidad();

                // Usamos el DAO directamente pasando 'conn'
                boolean stockOk = this.DATOS_PRODUCTO.actualizarStock(
                    det.getItem().getProductoId(), 
                    cantidadPositiva, 
                    tipoProducto, 
                    conn
                );
                
                if (!stockOk) {
                    throw new RuntimeException("Falló la actualización de stock para " + det.getItem().getNombre());
                }

                // Registrar movimiento
                this.DATOS_INVENTARIO.registrarMovimientoCompra(det, compraId, empleadoId, conn);
            }

            conn.commit(); // Confirmar transacción
            return null; // Éxito

        } catch (Exception e) { 
            LOGGER.log(Level.SEVERE, "Error en transacción de Compra", e);
            if (conn != null) {
                try {
                    conn.rollback(); // Ahora funcionará porque conn sigue abierta
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error crítico. Falló el ROLLBACK de Compra.", ex);
                }
            }
            return "Error al registrar la Compra: " + e.getMessage();

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Cerramos la conexión SOLO al final de todo
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar la conexión de Compra.", ex);
                }
            }
        }
    }
}