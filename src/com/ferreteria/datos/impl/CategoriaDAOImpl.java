package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.ICategoriaDAO;
import com.ferreteria.entidades.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaDAOImpl implements ICategoriaDAO {

    private static final Logger LOGGER = Logger.getLogger(CategoriaDAOImpl.class.getName());

    public CategoriaDAOImpl() {
    }

    private Categoria mapearResultSet(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria();
        cat.setCategoriaId(rs.getInt("categoria_id"));
        cat.setNombre(rs.getString("nombre"));
        cat.setDescripcion(rs.getString("descripcion"));
        return cat;
    }

    @Override
    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM Categoria ORDER BY nombre ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar categorías", ex);
        }
        return lista;
    }

    @Override
    public Categoria buscarPorId(Integer id) {
        String sql = "SELECT * FROM Categoria WHERE categoria_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar categoría por ID", ex);
        }
        return null;
    }

    @Override
    public boolean insertar(Categoria entidad) {
        String sql = "INSERT INTO Categoria (nombre, descripcion) VALUES (?, ?)";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar categoría", ex);
            return false;
        }
    }

    @Override
    public boolean actualizar(Categoria entidad) {
        String sql = "UPDATE Categoria SET nombre = ?, descripcion = ? WHERE categoria_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setInt(3, entidad.getCategoriaId());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar categoría", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM Categoria WHERE categoria_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar categoría. Verifique que no esté en uso.", ex);
            return false;
        }
    }
}