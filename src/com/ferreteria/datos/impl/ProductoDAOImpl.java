package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IProductoDAO;
import com.ferreteria.entidades.Categoria;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDAOImpl implements IProductoDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductoDAOImpl.class.getName());

    public ProductoDAOImpl() {
    }

    private ItemVendible mapearResultSet(ResultSet rs) throws SQLException {
        ItemVendible item;
        String tipo = rs.getString("tipo_producto");

        switch (tipo) {
            case "UNITARIO":
                ProductoUnitario unitario = new ProductoUnitario();
                unitario.setPrecioUnitario(rs.getDouble("precio_unitario"));
                unitario.setStockActual(rs.getInt("stock_actual"));
                item = unitario;
                break;
            case "GRANEL":
                ProductoAGranel granel = new ProductoAGranel();
                granel.setPrecioPorMedida(rs.getDouble("precio_por_medida"));
                granel.setStockMedido(rs.getDouble("stock_medido"));
                granel.setUnidadMedida(rs.getString("unidad_medida"));
                item = granel;
                break;
            case "SERVICIO":
            default:
                Servicio servicio = new Servicio();
                servicio.setTarifaServicio(rs.getDouble("tarifa_servicio"));
                item = servicio;
                break;
        }
        item.setProductoId(rs.getInt("producto_id"));
        item.setSku(rs.getString("sku"));
        item.setNombre(rs.getString("nombre"));
        item.setDescripcion(rs.getString("descripcion"));
        item.setActivo(rs.getBoolean("activo"));
        
        Categoria cat = new Categoria();
        cat.setCategoriaId(rs.getInt("categoria_id"));
        cat.setNombre(rs.getString("categoria_nombre"));
        item.setCategoria(cat);
        return item;
    }

    @Override
    public List<ItemVendible> listarTodos() {
        List<ItemVendible> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as categoria_nombre FROM Producto p "
                + "LEFT JOIN Categoria c ON p.categoria_id = c.categoria_id "
                + "WHERE p.activo = TRUE "
                + "ORDER BY p.nombre ASC";
        try (Connection conn = Conexion.obtenerConexion(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar productos", ex);
        }
        return lista;
    }

    // Standard method from ICrudDAO
    @Override
    public ItemVendible buscarPorId(Integer id) {
        try (Connection conn = Conexion.obtenerConexion()) {
            return buscarPorId(id, conn);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar producto por ID", ex);
        }
        return null;
    }

    // Transactional method (Overloaded)
    @Override
    public ItemVendible buscarPorId(Integer id, Connection conn) {
        String sql = "SELECT p.*, c.nombre as categoria_nombre FROM Producto p "
                + "LEFT JOIN Categoria c ON p.categoria_id = c.categoria_id "
                + "WHERE p.producto_id = ?";
        // NOTE: We do not close 'conn' here as it is passed in from a transaction
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar producto por ID (Transaccional)", ex);
            // We throw RuntimeException to ensure the caller knows something failed
            throw new RuntimeException("Error SQL al buscar producto: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean insertar(ItemVendible entidad) {
        String sql = "INSERT INTO Producto (sku, nombre, descripcion, categoria_id, activo, tipo_producto, "
                + "precio_unitario, stock_actual, precio_por_medida, stock_medido, unidad_medida, tarifa_servicio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entidad.getSku());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getDescripcion());
            ps.setInt(4, entidad.getCategoria().getCategoriaId());
            ps.setBoolean(5, entidad.isActivo());
            
            if (entidad instanceof ProductoUnitario) {
                ps.setString(6, "UNITARIO");
                ProductoUnitario pu = (ProductoUnitario) entidad;
                ps.setDouble(7, pu.getPrecioUnitario());
                ps.setInt(8, pu.getStockActual());
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.DECIMAL);
            } else if (entidad instanceof ProductoAGranel) {
                ps.setString(6, "GRANEL");
                ProductoAGranel pg = (ProductoAGranel) entidad;
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setDouble(9, pg.getPrecioPorMedida());
                ps.setDouble(10, pg.getStockMedido());
                ps.setString(11, pg.getUnidadMedida());
                ps.setNull(12, java.sql.Types.DECIMAL);
            } else if (entidad instanceof Servicio) {
                ps.setString(6, "SERVICIO");
                Servicio s = (Servicio) entidad;
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setDouble(12, s.getTarifaServicio());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar producto", ex);
            return false;
        }
    }

    @Override
    public boolean actualizar(ItemVendible entidad) {
        String sql = "UPDATE Producto SET sku=?, nombre=?, descripcion=?, categoria_id=?, activo=?, "
                + "tipo_producto=?, precio_unitario=?, stock_actual=?, "
                + "precio_por_medida=?, stock_medido=?, unidad_medida=?, tarifa_servicio=? "
                + "WHERE producto_id = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getSku());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getDescripcion());
            ps.setInt(4, entidad.getCategoria().getCategoriaId());
            ps.setBoolean(5, entidad.isActivo());
            
            if (entidad instanceof ProductoUnitario) {
                ps.setString(6, "UNITARIO");
                ProductoUnitario pu = (ProductoUnitario) entidad;
                ps.setDouble(7, pu.getPrecioUnitario());
                ps.setInt(8, pu.getStockActual());
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.DECIMAL);
            } else if (entidad instanceof ProductoAGranel) {
                ps.setString(6, "GRANEL");
                ProductoAGranel pg = (ProductoAGranel) entidad;
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setDouble(9, pg.getPrecioPorMedida());
                ps.setDouble(10, pg.getStockMedido());
                ps.setString(11, pg.getUnidadMedida());
                ps.setNull(12, java.sql.Types.DECIMAL);
            } else if (entidad instanceof Servicio) {
                ps.setString(6, "SERVICIO");
                Servicio s = (Servicio) entidad;
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setDouble(12, s.getTarifaServicio());
            }
            ps.setInt(13, entidad.getProductoId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar producto", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "UPDATE Producto SET activo = FALSE WHERE producto_id = ?";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al desactivar producto", ex);
            return false;
        }
    }

    @Override
    public List<ItemVendible> buscarPorNombre(String nombre) {
        List<ItemVendible> lista = new ArrayList<>();
        // FIX: Using LIKE instead of ILIKE for MySQL compatibility
        String sql = "SELECT p.*, c.nombre as categoria_nombre FROM Producto p "
                + "LEFT JOIN Categoria c ON p.categoria_id = c.categoria_id "
                + "WHERE (p.nombre LIKE ? OR p.sku LIKE ?) AND p.activo = TRUE "
                + "ORDER BY p.nombre ASC";
        try (Connection conn = Conexion.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String termino = "%" + nombre + "%";
            ps.setString(1, termino);
            ps.setString(2, termino);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar productos", ex);
        }
        return lista;
    }

    @Override
    public List<ItemVendible> listarStockBajo(int nivelMinimo) {
        List<ItemVendible> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as categoria_nombre FROM Producto p "
                + "LEFT JOIN Categoria c ON p.categoria_id = c.categoria_id "
                + "WHERE p.activo = TRUE AND ("
                + " (p.tipo_producto = 'UNITARIO' AND p.stock_actual <= ?) OR "
                + " (p.tipo_producto = 'GRANEL' AND p.stock_medido <= ?) "
                + ") ORDER BY p.nombre ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nivelMinimo);
            ps.setDouble(2, nivelMinimo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar stock bajo", ex);
        }
        return lista;
    }

    @Override
    public boolean actualizarStock(int productoId, double cantidad, String tipoProducto, Connection conn) {
        String sql;
        try {
            if (tipoProducto.equals("UNITARIO")) {
                sql = "UPDATE Producto SET stock_actual = stock_actual + ? WHERE producto_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, (int) cantidad);
                    ps.setInt(2, productoId);
                    return ps.executeUpdate() > 0;
                }
            } else if (tipoProducto.equals("GRANEL")) {
                sql = "UPDATE Producto SET stock_medido = stock_medido + ? WHERE producto_id = ?";
                 try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDouble(1, cantidad);
                    ps.setInt(2, productoId);
                    return ps.executeUpdate() > 0;
                }
            } else {
                return true; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar stock (en transacción)", ex);
            throw new RuntimeException(ex); 
        }
    }
}