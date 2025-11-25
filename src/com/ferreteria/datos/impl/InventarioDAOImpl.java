package com.ferreteria.datos.impl;

import com.ferreteria.datos.interfaces.IInventarioDAO;
import com.ferreteria.entidades.DetalleCompra;
import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.ItemVendible;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class InventarioDAOImpl implements IInventarioDAO {
    
    private static final Logger LOGGER = Logger.getLogger(InventarioDAOImpl.class.getName());

    // Este método es interno y usa la conexión que se le pasa, es seguro.
    private double getStockActual(int productoId, String tipoProducto, Connection conn) throws SQLException {
        String stockField = tipoProducto.equals("UNITARIO") ? "stock_actual" : "stock_medido";
        String sql = "SELECT " + stockField + " FROM Producto WHERE producto_id = ? FOR UPDATE";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                } else {
                    throw new SQLException("No se pudo obtener stock para el producto ID: " + productoId);
                }
            }
        }
    }

    private void insertarMovimiento(int productoId, String tipoMov, double cantidad, 
                                    Integer ventaId, Integer compraId, int empleadoId, 
                                    double stockAnterior, double stockNuevo, Connection conn) throws SQLException {
        
        String sql = "INSERT INTO InventarioMovimiento (producto_id, tipo_movimiento, cantidad_movimiento, "
                   + "referencia_venta_id, referencia_compra_id, empleado_id, stock_anterior, stock_nuevo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ps.setString(2, tipoMov);
            ps.setDouble(3, cantidad);
            
            if (ventaId != null) ps.setInt(4, ventaId);
            else ps.setNull(4, java.sql.Types.INTEGER);
            
            if (compraId != null) ps.setInt(5, compraId);
            else ps.setNull(5, java.sql.Types.INTEGER);
            
            ps.setInt(6, empleadoId); 
            ps.setDouble(7, stockAnterior);
            ps.setDouble(8, stockNuevo);
            
            ps.executeUpdate();
        }
    }

    @Override
    public boolean registrarMovimientoVenta(DetalleVenta detalle, int ventaId, int empleadoId, Connection conn) throws SQLException {
        ItemVendible item = detalle.getItem();
        String tipoProducto = item.getClass().getSimpleName().toUpperCase().replace("PRODUCTO", "");
        
        if (tipoProducto.equals("SERVICIO")) {
            return true; 
        }

        double cantidadNegativa = detalle.getCantidad() * -1.0;
        double stockAnterior = getStockActual(item.getProductoId(), tipoProducto, conn);
        double stockNuevo = stockAnterior + cantidadNegativa;

        insertarMovimiento(item.getProductoId(), "VENTA", cantidadNegativa, 
                           ventaId, null, empleadoId, 
                           stockAnterior, stockNuevo, conn);
        return true;
    }

    @Override
    public boolean registrarMovimientoCompra(DetalleCompra detalle, int compraId, int empleadoId, Connection conn) throws SQLException {
        ItemVendible item = detalle.getItem();
        String tipoProducto = item.getClass().getSimpleName().toUpperCase().replace("PRODUCTO", "");

        if (tipoProducto.equals("SERVICIO")) {
            return true; 
        }
        
        double cantidadPositiva = detalle.getCantidad();
        double stockAnterior = getStockActual(item.getProductoId(), tipoProducto, conn);
        double stockNuevo = stockAnterior + cantidadPositiva;
        
        insertarMovimiento(item.getProductoId(), "COMPRA", cantidadPositiva, 
                           null, compraId, empleadoId,
                           stockAnterior, stockNuevo, conn);
        return true;
    }
}