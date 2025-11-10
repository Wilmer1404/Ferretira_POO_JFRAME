package com.ferreteria.negocio;

import com.ferreteria.datos.impl.VentaDAOImpl;
import com.ferreteria.datos.interfaces.IVentaDAO;
import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Venta;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class VentaNegocio {

    private final IVentaDAO DATOS_VENTA;
    private final ProductoNegocio PRODUCTO_NEGOCIO;
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

    public String insertar(Venta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            return "El carrito está vacío, no se puede procesar la venta.";
        }

        for (DetalleVenta det : venta.getDetalles()) {
            ItemVendible itemEnDB = this.PRODUCTO_NEGOCIO.buscarPorId(det.getItem().getProductoId());
            if (itemEnDB == null) {
                return "El producto '" + det.getItem().getNombre() + "' ya no existe.";
            }
            if (!itemEnDB.validarStock(det.getCantidad())) {
                return "Stock insuficiente para '" + itemEnDB.getNombre() + "'. Stock actual: " + itemEnDB.obtenerStock();
            }
        }
        boolean insercionVentaOk = this.DATOS_VENTA.insertar(venta);

        if (!insercionVentaOk) {
            return "Error al registrar la Venta en la base de datos.";
        }

        try {
            for (DetalleVenta det : venta.getDetalles()) {
                ItemVendible item = this.PRODUCTO_NEGOCIO.buscarPorId(det.getItem().getProductoId());

                if (item instanceof ProductoUnitario) {
                    ProductoUnitario pu = (ProductoUnitario) item;
                    pu.setStockActual(pu.getStockActual() - (int) det.getCantidad());
                    this.PRODUCTO_NEGOCIO.actualizarStock(pu);

                } else if (item instanceof ProductoAGranel) {
                    ProductoAGranel pg = (ProductoAGranel) item;
                    pg.setStockMedido(pg.getStockMedido() - det.getCantidad());
                    this.PRODUCTO_NEGOCIO.actualizarStock(pg);
                }
            }
        } catch (Exception e) {

            return "¡VENTA REALIZADA! Pero ocurrió un error crítico al actualizar el stock: " + e.getMessage();
        }

        return null;
    }
}
