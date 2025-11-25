package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IClienteDAO;
import com.ferreteria.entidades.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAOImpl implements IClienteDAO {

    private static final Logger LOGGER = Logger.getLogger(ClienteDAOImpl.class.getName());

    public ClienteDAOImpl() {
    }

    private Cliente mapearResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setClienteId(rs.getInt("cliente_id"));
        cliente.setDni(rs.getString("dni"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setPasswordHash(rs.getString("password_hash"));
        
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) {
            cliente.setFechaRegistro(ts.toLocalDateTime());
        }
        return cliente;
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente ORDER BY apellidos ASC, nombre ASC";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar clientes", ex);
        }
        return lista;
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        String sql = "SELECT * FROM Cliente WHERE cliente_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar cliente por ID", ex);
        }
        return null;
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        String sql = "SELECT * FROM Cliente WHERE email = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar cliente por Email", ex);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        String sql = "SELECT * FROM Cliente WHERE dni = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar cliente por DNI", ex);
        }
        return Optional.empty();
    }

    @Override
    public boolean insertar(Cliente entidad) {
        String sql = "INSERT INTO Cliente (dni, nombre, apellidos, email, telefono, direccion, password_hash, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getDni());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getApellidos());
            ps.setString(4, entidad.getEmail());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, entidad.getDireccion());
            ps.setString(7, entidad.getPasswordHash());
            ps.setTimestamp(8, Timestamp.valueOf(entidad.getFechaRegistro()));
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar cliente", ex);
            return false;
        }
    }

    @Override
    public boolean actualizar(Cliente entidad) {
        String sql = "UPDATE Cliente SET dni = ?, nombre = ?, apellidos = ?, email = ?, telefono = ?, direccion = ? "
                + "WHERE cliente_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entidad.getDni());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getApellidos());
            ps.setString(4, entidad.getEmail());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, entidad.getDireccion());
            ps.setInt(7, entidad.getClienteId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar cliente", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM Cliente WHERE cliente_id = ?";
        
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar cliente", ex);
            return false;
        }
    }
}