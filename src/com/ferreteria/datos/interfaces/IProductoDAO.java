package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.ItemVendible;
import java.sql.Connection;
import java.util.List;

// CORRECTION HERE: Added ", Integer" to match ICrudDAO<T, ID>
public interface IProductoDAO extends ICrudDAO<ItemVendible, Integer> {
    
    public List<ItemVendible> buscarPorNombre(String nombre);
    
    public List<ItemVendible> listarStockBajo(int nivelMinimo);

    // Method for transactional updates
    public boolean actualizarStock(int productoId, double cantidad, String tipoProducto, Connection conn);

    // Overloaded method for transactional reads
    public ItemVendible buscarPorId(Integer id, Connection conn);
}