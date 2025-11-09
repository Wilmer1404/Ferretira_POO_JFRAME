package com.ferreteria.datos.interfaces;

import java.util.List;

public interface ICrudDAO<T, ID> {

    public List<T> listarTodos();
    

    public T buscarPorId(ID id);

    public boolean insertar(T entidad);
    
    public boolean actualizar(T entidad);

    public boolean eliminar(ID id);
    
}