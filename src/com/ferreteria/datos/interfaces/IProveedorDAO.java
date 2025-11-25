package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.Proveedor;
import java.util.List;
import java.util.Optional;

public interface IProveedorDAO extends ICrudDAO<Proveedor, Integer> {
    
    @Override
    public List<Proveedor> listarTodos(); 
    public List<Proveedor> buscar(String termino);    
    public Optional<Proveedor> buscarPorRuc(String ruc);
}