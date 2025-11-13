package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IVentaDAO;
import com.ferreteria.entidades.Cliente;
import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.Empleado;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Venta;
import java.sql.Connection;
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

    private Venta mapearVentaParaReporte(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setVentaId(rs.getInt("venta_id"));
        venta.setFechaVenta(rs.getTimestamp("fecha_venta").toLocalDateTime());
        venta.setTotal(rs.getDouble("total"));
        venta.setMetodoPago(rs.getString("metodo_pago"));

        Cliente cliente = new Cliente();
        cliente.setNombre(rs.getString("cliente_nombre"));
        cliente.setApellidos(rs.getString("cliente_apellidos"));
        venta.setCliente(cliente);

        Empleado empleado = new Empleado();
        String nombreEmpleado = rs.getString("empleado_nombre");
        empleado.setNombre(nombreEmpleado == null ? "N/A" : nombreEmpleado);
        venta.setEmpleado(empleado);

        return venta;
    }

    @Override
    public boolean insertar(Venta venta) {
        String sqlVenta = "INSERT INTO Venta (cliente_id, fecha_venta, total, metodo_pago, referencia_transaccion) "
                + "VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO DetalleVenta (venta_id, producto_id, cantidad, precio_historico, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";
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
                    psDetalle.setInt(1, ventaIdGenerada);
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
    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.venta_id, v.fecha_venta, v.total, v.metodo_pago, "
                + "c.nombre as cliente_nombre, c.apellidos as cliente_apellidos, "
                + "e.nombre as empleado_nombre "
                + "FROM Venta v "
                + "JOIN Cliente c ON v.cliente_id = c.cliente_id "
                + "LEFT JOIN Empleado e ON v.empleado_id = e.empleado_id "
                + "ORDER BY v.fecha_venta DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearVentaParaReporte(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar todas las ventas", ex);
        }
        return lista;
    }

    @Override
    public List<Venta> listarPorFechas(LocalDate inicio, LocalDate fin) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.venta_id, v.fecha_venta, v.total, v.metodo_pago, "
                + "c.nombre as cliente_nombre, c.apellidos as cliente_apellidos, "
                + "e.nombre as empleado_nombre "
                + "FROM Venta v "
                + "JOIN Cliente c ON v.cliente_id = c.cliente_id "
                + "LEFT JOIN Empleado e ON v.empleado_id = e.empleado_id "
                + "WHERE DATE(v.fecha_venta) BETWEEN ? AND ? "
                + "ORDER BY v.fecha_venta DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setObject(1, inicio);
            ps.setObject(2, fin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVentaParaReporte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar ventas por fechas", ex);
        }
        return lista;
    }

    @Override
    public List<Venta> listarPorCliente(int clienteId) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.venta_id, v.fecha_venta, v.total, v.metodo_pago, "
                + "c.nombre as cliente_nombre, c.apellidos as cliente_apellidos, "
                + "e.nombre as empleado_nombre "
                + "FROM Venta v "
                + "JOIN Cliente c ON v.cliente_id = c.cliente_id "
                + "LEFT JOIN Empleado e ON v.empleado_id = e.empleado_id "
                + "WHERE v.cliente_id = ? " // <-- El filtro
                + "ORDER BY v.fecha_venta DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, clienteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVentaParaReporte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar ventas por cliente", ex);
        }
        return lista;
    }

    @Override
    public Venta buscarPorId(Integer id) {
        Venta venta = null;
        String sqlVenta = "SELECT v.venta_id, v.fecha_venta, v.total, v.metodo_pago, "
                + "c.nombre as cliente_nombre, c.apellidos as cliente_apellidos, "
                + "e.nombre as empleado_nombre "
                + "FROM Venta v "
                + "JOIN Cliente c ON v.cliente_id = c.cliente_id "
                + "LEFT JOIN Empleado e ON v.empleado_id = e.empleado_id "
                + "WHERE v.venta_id = ?";

        try (PreparedStatement psVenta = cnx.prepareStatement(sqlVenta)) {
            psVenta.setInt(1, id);

            try (ResultSet rsVenta = psVenta.executeQuery()) {
                if (rsVenta.next()) {
                    venta = mapearVentaParaReporte(rsVenta);

                    String sqlDetalle = "SELECT d.cantidad, d.precio_historico, d.subtotal, p.producto_id, p.nombre "
                            + "FROM DetalleVenta d "
                            + "JOIN Producto p ON d.producto_id = p.producto_id "
                            + "WHERE d.venta_id = ?";

                    List<DetalleVenta> detalles = new ArrayList<>();
                    try (PreparedStatement psDetalle = cnx.prepareStatement(sqlDetalle)) {
                        psDetalle.setInt(1, id);
                        try (ResultSet rsDetalle = psDetalle.executeQuery()) {
                            while (rsDetalle.next()) {
                                DetalleVenta detalle = new DetalleVenta();
                                detalle.setCantidad(rsDetalle.getDouble("cantidad"));
                                detalle.setPrecioHistorico(rsDetalle.getDouble("precio_historico"));
                                detalle.setSubtotal(rsDetalle.getDouble("subtotal"));

                                ItemVendible item = new ProductoUnitario();
                                item.setProductoId(rsDetalle.getInt("producto_id"));
                                item.setNombre(rsDetalle.getString("nombre"));

                                detalle.setItem(item);
                                detalles.add(detalle);
                            }
                        }
                    }
                    venta.setDetalles(detalles);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar la Venta completa por ID", ex);
        }
        return venta;
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
