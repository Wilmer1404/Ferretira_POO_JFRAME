package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IEmpleadoDAO;
import com.ferreteria.entidades.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpleadoDAOImpl implements IEmpleadoDAO {
    
    private static final Logger LOGGER = Logger.getLogger(EmpleadoDAOImpl.class.getName());

    public EmpleadoDAOImpl() {
    }

    private Empleado mapearResultSet(ResultSet rs) throws SQLException {
        Empleado emp = new Empleado();
        emp.setEmpleadoId(rs.getInt("empleado_id"));
        emp.setDni(rs.getString("dni"));
        emp.setNombre(rs.getString("nombre"));
        emp.setApellidos(rs.getString("apellidos"));
        emp.setEmail(rs.getString("email"));
        emp.setPasswordHash(rs.getString("password_hash"));
        emp.setRol(rs.getString("rol"));
        emp.setActivo(rs.getBoolean("activo"));
        return emp;
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Empleado ORDER BY apellidos ASC, nombre ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar empleados", ex);
        }
        return lista;
    }

    @Override
    public Empleado buscarPorId(Integer id) {
        String sql = "SELECT * FROM Empleado WHERE empleado_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar empleado por ID", ex);
        }
        return null;
    }
    
    @Override
    public Optional<Empleado> buscarPorEmail(String email) {
        String sql = "SELECT * FROM Empleado WHERE email = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar empleado por Email", ex);
        }
        return Optional.empty();
    }

    @Override
    public boolean insertar(Empleado entidad) {
        String sql = "INSERT INTO Empleado (dni, nombre, apellidos, email, password_hash, rol, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getDni());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getApellidos());
            ps.setString(4, entidad.getEmail());
            ps.setString(5, entidad.getPasswordHash());
            ps.setString(6, entidad.getRol());
            ps.setBoolean(7, entidad.isActivo());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar empleado", ex);
            return false;
        }
    }

    @Override
    public boolean actualizar(Empleado entidad) {
        String sql = "UPDATE Empleado SET dni = ?, nombre = ?, apellidos = ?, email = ?, rol = ?, activo = ? "
                + "WHERE empleado_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getDni());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getApellidos());
            ps.setString(4, entidad.getEmail());
            ps.setString(5, entidad.getRol());
            ps.setBoolean(6, entidad.isActivo());
            ps.setInt(7, entidad.getEmpleadoId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar empleado", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "UPDATE Empleado SET activo = FALSE WHERE empleado_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al desactivar empleado", ex);
            return false;
        }
    }

    @Override
    public List<Empleado> buscar(String termino) {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Empleado " +
                     "WHERE (dni = ? OR nombre ILIKE ? OR apellidos ILIKE ?) " +
                     "ORDER BY apellidos, nombre ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String terminoLike = "%" + termino + "%";
            ps.setString(1, termino);
            ps.setString(2, terminoLike);
            ps.setString(3, terminoLike);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar empleados", ex);
        }
        return lista;
    }
}