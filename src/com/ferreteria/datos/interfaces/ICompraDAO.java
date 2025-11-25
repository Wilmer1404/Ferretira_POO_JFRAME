package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.Compra;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ICompraDAO extends ICrudDAO<Compra, Integer> {
    
    public int insertar(Compra entidad, Connection conn) throws SQLException;
    
    public List<Compra> listarPorProveedor(int proveedorId);
    public List<Compra> listarPorEmpleado(int empleadoId);
}