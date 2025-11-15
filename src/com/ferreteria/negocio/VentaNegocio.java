package com.ferreteria.negocio;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.impl.InventarioDAOImpl; 
import com.ferreteria.datos.impl.VentaDAOImpl;
import com.ferreteria.datos.interfaces.IInventarioDAO;
import com.ferreteria.datos.interfaces.IVentaDAO;
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

public class VentaNegocio {

    private final IVentaDAO DATOS_VENTA;
    private final ProductoNegocio PRODUCTO_NEGOCIO;
    
    private final IInventarioDAO DATOS_INVENTARIO; 
    
    private static final Logger LOGGER = Logger.getLogger(VentaNegocio.class.getName());

    public VentaNegocio() {
        this.DATOS_VENTA = new VentaDAOImpl();
        this.PRODUCTO_NEGOCIO = new ProductoNegocio();
        
        this.DATOS_INVENTARIO = new InventarioDAOImpl(); 
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