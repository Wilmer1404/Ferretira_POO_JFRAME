package com.ferreteria.negocio;

import com.ferreteria.datos.impl.ProductoDAOImpl;
import com.ferreteria.datos.interfaces.IProductoDAO;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Servicio;
import java.util.List;


public class ProductoNegocio {

    private final IProductoDAO DATOS_PROD;

    public ProductoNegocio() {
        this.DATOS_PROD = new ProductoDAOImpl();
    }

    public List<ItemVendible> listar() {
        return this.DATOS_PROD.listarTodos();
    }


    public List<ItemVendible> buscar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return this.listar();
        }
        return this.DATOS_PROD.buscarPorNombre(nombre);
    }
    

    public ItemVendible buscarPorId(int id) {
        return this.DATOS_PROD.buscarPorId(id);
    }
    

    public List<ItemVendible> listarStockBajo(int nivelMinimo) {
        return this.DATOS_PROD.listarStockBajo(nivelMinimo);
    }

    public String insertar(ItemVendible item) {
        String error = validar(item);
        if (error != null) {
            return error;
        }

        if (this.DATOS_PROD.insertar(item)) {
            return null; 
        } else {
            return "Error desconocido al guardar el producto en la base de datos.";
        }
    }
    

    public String actualizar(ItemVendible item) {
        if (item.getProductoId() <= 0) {
            return "No se ha seleccionado ningún producto para actualizar.";
        }
        
        String error = validar(item);
        if (error != null) {
            return error;
        }
        
        if (this.DATOS_PROD.actualizar(item)) {
            return null; 
        } else {
            return "Error desconocido al actualizar el producto.";
        }
    }

    public String desactivar(int id) {

        ItemVendible item = this.DATOS_PROD.buscarPorId(id);
        if (item == null) {
            return "El producto que intenta desactivar no existe.";
        }
        
        if (item.obtenerStock() > 0) {
            return "Error: No se puede desactivar un producto que aún tiene stock (" + item.obtenerStock() + " " + item.obtenerUnidadParaGUI() + ").";
        }

        if (this.DATOS_PROD.eliminar(id)) {
            return null; 
        } else {
            return "Error desconocido al desactivar el producto.";
        }
    }
    
    
    private String validar(ItemVendible item) {
        if (item.getNombre() == null || item.getNombre().trim().isEmpty()) {
            return "El nombre del producto no puede estar vacío.";
        }
        if (item.getSku() == null || item.getSku().trim().isEmpty()) {
            return "El SKU del producto no puede estar vacío.";
        }
        
        if (item instanceof ProductoUnitario) {
            ProductoUnitario pu = (ProductoUnitario) item;
            if (pu.getPrecioUnitario() <= 0) {
                return "El precio unitario debe ser mayor a 0.";
            }
            if (pu.getStockActual() < 0) {
                return "El stock no puede ser negativo.";
            }
        } else if (item instanceof ProductoAGranel) {
            ProductoAGranel pg = (ProductoAGranel) item;
            if (pg.getPrecioPorMedida() <= 0) {
                return "El precio por medida debe ser mayor a 0.";
            }
            if (pg.getStockMedido() < 0) {
                return "El stock medido no puede ser negativo.";
            }
            if (pg.getUnidadMedida() == null || pg.getUnidadMedida().trim().isEmpty()) {
                return "La unidad de medida (Kilo, Metro...) es obligatoria para productos a granel.";
            }
        } else if (item instanceof Servicio) {
            Servicio s = (Servicio) item;
            if (s.getTarifaServicio() <= 0) {
                return "La tarifa del servicio debe ser mayor a 0.";
            }
        }
        
        return null;
    }
}