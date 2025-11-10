package com.ferreteria.datos.impl;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.interfaces.IClienteDAO;
import com.ferreteria.entidades.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAOImpl implements IClienteDAO {

    private final Connection cnx;
    private static final Logger LOGGER = Logger.getLogger(ClienteDAOImpl.class.getName());

    public ClienteDAOImpl() {
        this.cnx = Conexion.obtenerConexion();
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        String sql = "SELECT * FROM Cliente WHERE dni = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
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
    public Optional<Cliente> buscarPorEmail(String email) {
        String sql = "SELECT * FROM Cliente WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
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
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente ORDER BY apellidos, nombre ASC";
        try (PreparedStatement ps = cnx.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
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
    public boolean insertar(Cliente entidad) {
        // --- CORRECCIÓN EN LA CONSULTA SQL ---
        String sql = "INSERT INTO Cliente (dni, nombre, apellidos, direccion, "
                + // <-- CORREGIDO
                "email, telefono, password_hash, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, entidad.getDni());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getApellidos());
            ps.setString(4, entidad.getDireccion()); // <-- CORREGIDO
            ps.setString(5, entidad.getEmail());
            ps.setString(6, entidad.getTelefono());
            ps.setString(7, entidad.getPasswordHash());
            ps.setTimestamp(8, java.sql.Timestamp.valueOf(entidad.getFechaRegistro()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar cliente", ex);
            return false;
        }
    }

    @Override
    public boolean actualizar(Cliente entidad) {
        String sql = "UPDATE Cliente SET nombre = ?, apellidos = ?, direccion = ?, "
                + 
                "telefono = ? WHERE cliente_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidos());
            ps.setString(3, entidad.getDireccion());
            ps.setString(4, entidad.getTelefono());
            ps.setInt(5, entidad.getClienteId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar cliente", ex);
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        LOGGER.log(Level.WARNING, "Llamada a 'eliminar' cliente. Se prefiere desactivar.");

        String sql = "DELETE FROM Cliente WHERE cliente_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar cliente. Verifique FKs en Ventas.", ex);
            return false;
        }
    }

    private Cliente mapearResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setClienteId(rs.getInt("cliente_id"));
        cliente.setDni(rs.getString("dni"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setPasswordHash(rs.getString("password_hash"));
        cliente.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return cliente;
    }
}
