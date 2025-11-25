package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IProveedorDAO;
import com.ferreteria.entidades.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProveedorDAOImpl implements IProveedorDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ProveedorDAOImpl.class.getName());

    public ProveedorDAOImpl() {
    }
    
    private Proveedor mapearResultSet(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setProveedorId(rs.getInt("proveedor_id"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setRuc(rs.getString("ruc"));
        p.setEmail(rs.getString("email"));
        p.setTelefono(rs.getString("telefono"));
        p.setDireccion(rs.getString("direccion"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    @Override
    public List<Proveedor> listarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Proveedor WHERE activo = TRUE ORDER BY razon_social ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar proveedores activos", ex);
        }
        return lista;
    }

    @Override
    public Proveedor buscarPorId(Integer id) {
        String sql = "SELECT * FROM Proveedor WHERE proveedor_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar proveedor por ID", ex);
        }
        return null;
    }

    @Override
    public boolean insertar(Proveedor entidad) {
        String sql = "INSERT INTO Proveedor (razon_social, ruc, email, telefono, direccion, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getRazonSocial());
            ps.setString(2, entidad.getRuc());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getTelefono());
            ps.setString(5, entidad.getDireccion());
            ps.setBoolean(6, entidad.isActivo());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar proveedor", ex);
            return false;
        }
    }

    @Override
    public boolean actualizar(Proveedor entidad) {
        String sql = "UPDATE Proveedor SET razon_social = ?, ruc = ?, email = ?, " +
                     "telefono = ?, direccion = ?, activo = ? WHERE proveedor_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getRazonSocial());
            ps.setString(2, entidad.getRuc());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getTelefono());
            ps.setString(5, entidad.getDireccion());
            ps.setBoolean(6, entidad.isActivo());
            ps.setInt(7, entidad.getProveedorId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar proveedor", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "UPDATE Proveedor SET activo = FALSE WHERE proveedor_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al desactivar proveedor", ex);
            return false;
        }
    }

    @Override
    public List<Proveedor> buscar(String termino) {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Proveedor "
                + "WHERE (razon_social LIKE ? OR ruc LIKE ?) AND activo = TRUE "
                + "ORDER BY razon_social ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String terminoLike = "%" + termino + "%";
            ps.setString(1, terminoLike);
            ps.setString(2, terminoLike);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar proveedores", ex);
        }
        return lista;
    }

    @Override
    public Optional<Proveedor> buscarPorRuc(String ruc) {
        String sql = "SELECT * FROM Proveedor WHERE ruc = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ruc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar proveedor por RUC", ex);
        }
        return Optional.empty();
    }
}