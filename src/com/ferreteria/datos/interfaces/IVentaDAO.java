package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.Venta;
import java.time.LocalDate;
import java.util.List;

public interface IVentaDAO extends ICrudDAO<Venta, Integer> {
    

    public List<Venta> listarPorCliente(int clienteId);
    
    public List<Venta> listarPorFechas(LocalDate inicio, LocalDate fin);
    
    public int insertar(Venta entidad, java.sql.Connection conn); 
    

}