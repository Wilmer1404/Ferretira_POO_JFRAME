package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.ItemVendible;
import java.sql.Connection; 
import java.util.List;

public interface IProductoDAO extends ICrudDAO<ItemVendible, Integer> {
    
    public List<ItemVendible> buscarPorNombre(String nombre);

    public List<ItemVendible> listarStockBajo(int nivelMinimo);
    
    public boolean actualizarStock(ItemVendible item); 
    
    public boolean actualizarStock(ItemVendible item, Connection conn);
}