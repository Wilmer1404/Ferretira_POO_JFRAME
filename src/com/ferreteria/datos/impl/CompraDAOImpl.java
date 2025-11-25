package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.ICompraDAO;
import com.ferreteria.entidades.Compra;
import com.ferreteria.entidades.DetalleCompra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompraDAOImpl implements ICompraDAO {
    
    private static final Logger LOGGER = Logger.getLogger(CompraDAOImpl.class.getName());

    public CompraDAOImpl() {
    }

    @Override
    public int insertar(Compra compra, Connection conn) throws SQLException {
        String sqlCompra = "INSERT INTO Compra (proveedor_id, empleado_id, fecha_compra, total, observaciones) "
                         + "VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DetalleCompra (compra_id, producto_id, cantidad, precio_compra, subtotal) "
                          + "VALUES (?, ?, ?, ?, ?)";
        
        int compraIdGenerada;
        
        try (PreparedStatement psCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
            psCompra.setInt(1, compra.getProveedor().getProveedorId());
            psCompra.setInt(2, compra.getEmpleado().getEmpleadoId());
            psCompra.setTimestamp(3, java.sql.Timestamp.valueOf(compra.getFechaCompra()));
            psCompra.setDouble(4, compra.getTotal());
            psCompra.setString(5, compra.getObservaciones());

            int filas = psCompra.executeUpdate();
            if (filas == 0) {
                throw new SQLException("Falló la creación de la Compra, no se insertaron filas.");
            }

            try (ResultSet generatedKeys = psCompra.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    compraIdGenerada = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Falló la creación de la Compra, no se obtuvo ID.");
                }
            }
        }

        try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                psDetalle.setInt(1, compraIdGenerada);
                psDetalle.setInt(2, detalle.getItem().getProductoId());
                psDetalle.setDouble(3, detalle.getCantidad());
                psDetalle.setDouble(4, detalle.getPrecioCompra());
                psDetalle.setDouble(5, detalle.getSubtotal());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();
        }
        
        return compraIdGenerada;
    }

    @Override
    public boolean insertar(Compra entidad) {
        try (Connection conn = Conexion.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                this.insertar(entidad, conn);
                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error en inserción (no-negocio) de Compra", e);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener conexión para Compra", e);
            return false;
        }
    }

    @Override
    public List<Compra> listarTodos() { return new ArrayList<>(); }
    @Override
    public Compra buscarPorId(Integer id) { return null; }
    @Override
    public boolean actualizar(Compra entidad) { return false; }
    @Override
    public boolean eliminar(Integer id) { return false; }
    @Override
    public List<Compra> listarPorProveedor(int proveedorId) { return new ArrayList<>(); }
    @Override
    public List<Compra> listarPorEmpleado(int empleadoId) { return new ArrayList<>(); }
}