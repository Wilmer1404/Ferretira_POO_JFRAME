package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.DetalleCompra;
import com.ferreteria.entidades.DetalleVenta;
import java.sql.Connection;
import java.sql.SQLException;

public interface IInventarioDAO {
    
    public boolean registrarMovimientoVenta(DetalleVenta detalle, int ventaId, int empleadoId, Connection conn) throws SQLException;
    
    public boolean registrarMovimientoCompra(DetalleCompra detalle, int compraId, int empleadoId, Connection conn) throws SQLException;
    
}