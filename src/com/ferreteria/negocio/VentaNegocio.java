package com.ferreteria.negocio;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.impl.VentaDAOImpl;
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

    private static final Logger LOGGER = Logger.getLogger(VentaNegocio.class.getName());

    public VentaNegocio() {
        this.DATOS_VENTA = new VentaDAOImpl();
        this.PRODUCTO_NEGOCIO = new ProductoNegocio();
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

        try {
            for (DetalleVenta det : venta.getDetalles()) {
                ItemVendible itemEnDB = this.PRODUCTO_NEGOCIO.buscarPorId(det.getItem().getProductoId());
                if (itemEnDB == null) {
                    return "El producto '" + det.getItem().getNombre() + "' ya no existe.";
                }
                if (!itemEnDB.validarStock(det.getCantidad())) {
                    return "Stock insuficiente para '" + itemEnDB.getNombre() + "'. Stock actual: " + itemEnDB.obtenerStock();
                }
            }
        } catch (Exception e) {
            return "Error al validar stock: " + e.getMessage();
        }

        Connection conn = null;
        try {
            conn = Conexion.obtenerConexion();
            conn.setAutoCommit(false); 
            this.DATOS_VENTA.insertar(venta, conn);

            for (DetalleVenta det : venta.getDetalles()) {
                ItemVendible item = this.PRODUCTO_NEGOCIO.buscarPorId(det.getItem().getProductoId());

                if (item instanceof ProductoUnitario) {
                    ProductoUnitario pu = (ProductoUnitario) item;
                    pu.setStockActual(pu.getStockActual() - (int) det.getCantidad());
                    this.PRODUCTO_NEGOCIO.actualizarStock(pu, conn); 

                } else if (item instanceof ProductoAGranel) {
                    ProductoAGranel pg = (ProductoAGranel) item;
                    pg.setStockMedido(pg.getStockMedido() - det.getCantidad());
                    this.PRODUCTO_NEGOCIO.actualizarStock(pg, conn); 
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
