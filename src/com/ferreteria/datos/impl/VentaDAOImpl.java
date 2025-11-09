package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IVentaDAO;
import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.Venta;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaDAOImpl implements IVentaDAO {

    private final Connection cnx;
    private static final Logger LOGGER = Logger.getLogger(VentaDAOImpl.class.getName());

    public VentaDAOImpl() {
        this.cnx = Conexion.obtenerConexion();
    }


    @Override
    public boolean insertar(Venta venta) {
        String sqlVenta = "INSERT INTO Venta (cliente_id, fecha_venta, total, metodo_pago, referencia_transaccion) " +
                          "VALUES (?, ?, ?, ?, ?)";
        
        String sqlDetalle = "INSERT INTO DetalleVenta (venta_id, producto_id, cantidad, precio_historico, subtotal) " +
                            "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = this.cnx;
        
        try {

            conn.setAutoCommit(false);
            
            int ventaIdGenerada;
            try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getCliente().getClienteId());
                psVenta.setTimestamp(2, java.sql.Timestamp.valueOf(venta.getFechaVenta()));
                psVenta.setDouble(3, venta.getTotal());
                psVenta.setString(4, venta.getMetodoPago());
                psVenta.setString(5, venta.getReferenciaTransaccion());
                
                int filasAfectadas = psVenta.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new SQLException("Falló la creación de la venta, no se insertaron filas.");
                }
                
                try (ResultSet generatedKeys = psVenta.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ventaIdGenerada = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Falló la creación de la venta, no se obtuvo ID.");
                    }
                }
            }
            
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    psDetalle.setInt(1, ventaIdGenerada); // Usamos el ID generado
                    psDetalle.setInt(2, detalle.getItem().getProductoId());
                    psDetalle.setDouble(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getPrecioHistorico());
                    psDetalle.setDouble(5, detalle.getSubtotal());
                    
                    psDetalle.addBatch(); 
                }
                
                psDetalle.executeBatch(); 
            }

            conn.commit();
            return true;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error en transacción de Venta. Iniciando ROLLBACK.", ex);
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Error crítico. Falló el ROLLBACK.", rollbackEx);
            }
            return false;
            
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error al restaurar AutoCommit.", ex);
            }
        }
    }
    

    @Override
    public List<Venta> listarPorCliente(int clienteId) {
        List<Venta> lista = new ArrayList<>();
        LOGGER.log(Level.INFO, "listarPorCliente no implementado.");
        return lista;
    }

    @Override
    public List<Venta> listarPorFechas(LocalDate inicio, LocalDate fin) {
        List<Venta> lista = new ArrayList<>();

        LOGGER.log(Level.INFO, "listarPorFechas no implementado.");
        return lista;
    }

    @Override
    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();

        LOGGER.log(Level.INFO, "listarTodos no implementado.");
        return lista;
    }

    @Override
    public Venta buscarPorId(Integer id) {

        LOGGER.log(Level.INFO, "buscarPorId no implementado.");
        return null;
    }

    @Override
    public boolean actualizar(Venta entidad) {
        LOGGER.log(Level.WARNING, "El método 'actualizar' Venta no es una operación estándar.");
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        LOGGER.log(Level.SEVERE, "PELIGRO: Se intentó eliminar una Venta. Esta operación debe ser prohibida.");
        return false;
    }
}