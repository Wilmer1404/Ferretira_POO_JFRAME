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

    private Connection cnx;
    private static final Logger LOGGER = Logger.getLogger(ProductoDAOImpl.class.getName());

    public ProductoDAOImpl() {
        this.cnx = Conexion.obtenerConexion();
    }

    private ItemVendible fabricarItemDesdeResultSet(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo_producto");
        ItemVendible item;

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
                Servicio servicio = new Servicio();
                servicio.setTarifaServicio(rs.getDouble("tarifa_servicio"));
                item = servicio;
                break;

            default:
                throw new SQLException("Tipo de producto desconocido en la BD: " + tipo);
        }

        item.setProductoId(rs.getInt("producto_id"));
        item.setSku(rs.getString("sku"));
        item.setNombre(rs.getString("nombre"));
        item.setDescripcion(rs.getString("descripcion"));
        item.setActivo(rs.getBoolean("activo"));

        return item;
    }

    @Override
    public List<ItemVendible> listarTodos() {
        List<ItemVendible> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE activo = TRUE";

        try (PreparedStatement ps = cnx.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(fabricarItemDesdeResultSet(rs));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar productos", ex);
        }
        return lista;
    }

    @Override
    public ItemVendible buscarPorId(Integer id) {
        String sql = "SELECT * FROM Producto WHERE producto_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return fabricarItemDesdeResultSet(rs);
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar producto por ID", ex);
        }
        return null;
    }

    @Override
    public List<ItemVendible> buscarPorNombre(String nombre) {
        List<ItemVendible> lista = new ArrayList<>();

        String sql = "SELECT * FROM Producto WHERE nombre ILIKE ? AND activo = TRUE";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(fabricarItemDesdeResultSet(rs));
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar productos por nombre", ex);
        }
        return lista;
    }

    @Override
    public boolean insertar(ItemVendible entidad) {
        String sql = "INSERT INTO Producto (sku, nombre, descripcion, categoria_id, activo, "
                + "tipo_producto, precio_unitario, stock_actual, "
                + "precio_por_medida, stock_medido, unidad_medida, tarifa_servicio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, entidad.getSku());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getDescripcion());
            if (entidad.getCategoria() != null) {
                ps.setInt(4, entidad.getCategoria().getCategoriaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setBoolean(5, entidad.isActivo());

            if (entidad instanceof ProductoUnitario) {
                ProductoUnitario pu = (ProductoUnitario) entidad;
                ps.setString(6, "UNITARIO");
                ps.setDouble(7, pu.getPrecioUnitario());
                ps.setInt(8, pu.getStockActual());
                // Campos de otros tipos van NULL
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.DECIMAL);

            } else if (entidad instanceof ProductoAGranel) {
                ProductoAGranel pg = (ProductoAGranel) entidad;
                ps.setString(6, "GRANEL");
                // Campos unitarios van NULL
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);

                ps.setDouble(9, pg.getPrecioPorMedida());
                ps.setDouble(10, pg.getStockMedido());
                ps.setString(11, pg.getUnidadMedida());

                ps.setNull(12, java.sql.Types.DECIMAL);

            } else if (entidad instanceof Servicio) {
                Servicio s = (Servicio) entidad;
                ps.setString(6, "SERVICIO");
                // Campos unitarios y granel van NULL
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
                + "WHERE producto_id = ?"; // Parámetro 13

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {

            // --- INICIO DE LÓGICA FALTANTE ---
            ps.setString(1, entidad.getSku());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getDescripcion());
            if (entidad.getCategoria() != null) {
                ps.setInt(4, entidad.getCategoria().getCategoriaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setBoolean(5, entidad.isActivo());

            if (entidad instanceof ProductoUnitario) {
                ProductoUnitario pu = (ProductoUnitario) entidad;
                ps.setString(6, "UNITARIO");
                ps.setDouble(7, pu.getPrecioUnitario());
                ps.setInt(8, pu.getStockActual());
                // Campos de otros tipos van NULL
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.DECIMAL);

            } else if (entidad instanceof ProductoAGranel) {
                ProductoAGranel pg = (ProductoAGranel) entidad;
                ps.setString(6, "GRANEL");
                // Campos unitarios van NULL
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);

                ps.setDouble(9, pg.getPrecioPorMedida());
                ps.setDouble(10, pg.getStockMedido());
                ps.setString(11, pg.getUnidadMedida());

                ps.setNull(12, java.sql.Types.DECIMAL);

            } else if (entidad instanceof Servicio) {
                Servicio s = (Servicio) entidad;
                ps.setString(6, "SERVICIO");
                // Campos unitarios y granel van NULL
                ps.setNull(7, java.sql.Types.DECIMAL);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.DECIMAL);
                ps.setNull(10, java.sql.Types.DECIMAL);
                ps.setNull(11, java.sql.Types.VARCHAR);

                ps.setDouble(12, s.getTarifaServicio());
            }

            // --- PARÁMETRO FINAL (EL WHERE) ---
            ps.setInt(13, entidad.getProductoId());

            // --- FIN DE LÓGICA FALTANTE ---
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar producto", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "UPDATE Producto SET activo = FALSE WHERE producto_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al (desactivar) eliminar producto", ex);
            return false;
        }
    }

    @Override
    public boolean actualizarStock(ItemVendible item) {

        String sql;
        try {
            if (item instanceof ProductoUnitario) {
                sql = "UPDATE Producto SET stock_actual = ? WHERE producto_id = ?";
                try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                    ps.setInt(1, ((ProductoUnitario) item).getStockActual());
                    ps.setInt(2, item.getProductoId());
                    return ps.executeUpdate() > 0;
                }
            } else if (item instanceof ProductoAGranel) {
                sql = "UPDATE Producto SET stock_medido = ? WHERE producto_id = ?";
                try (PreparedStatement ps = cnx.prepareStatement(sql)) {
                    ps.setDouble(1, ((ProductoAGranel) item).getStockMedido());
                    ps.setInt(2, item.getProductoId());
                    return ps.executeUpdate() > 0;
                }
            } else {
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar stock", ex);
            return false;
        }
    }

    @Override
    public List<ItemVendible> listarStockBajo(int nivelMinimo) {
        List<ItemVendible> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE "
                + "(tipo_producto = 'UNITARIO' AND stock_actual <= ?) OR "
                + "(tipo_producto = 'GRANEL' AND stock_medido <= ?) "
                + "AND activo = TRUE";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, nivelMinimo);
            ps.setDouble(2, (double) nivelMinimo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(fabricarItemDesdeResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar stock bajo", ex);
        }
        return lista;
    }
}
