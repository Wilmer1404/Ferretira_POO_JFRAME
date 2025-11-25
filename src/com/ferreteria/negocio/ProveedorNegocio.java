package com.ferreteria.negocio;

import com.ferreteria.datos.impl.ProveedorDAOImpl;
import com.ferreteria.datos.interfaces.IProveedorDAO;
import com.ferreteria.entidades.Proveedor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProveedorNegocio {
    
    private final IProveedorDAO DATOS_PROV;

    public ProveedorNegocio() {
        this.DATOS_PROV = new ProveedorDAOImpl();
    }
    
    public List<Proveedor> listar() {
        return this.DATOS_PROV.listarTodos();
    }
    
    public List<Proveedor> buscar(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return this.DATOS_PROV.listarTodos();
        }
        return this.DATOS_PROV.buscar(termino.trim());
    }
    
    private String validar(Proveedor p) {
        if (p.getRazonSocial() == null || p.getRazonSocial().trim().isEmpty()) {
            return "La Razón Social es obligatoria.";
        }
        if (p.getRuc() == null || p.getRuc().trim().isEmpty()) {
            return "El RUC es obligatorio.";
        }
        if (p.getRuc().length() != 11 && p.getRuc().length() != 8) {
             return "El RUC/DNI debe tener 8 u 11 dígitos.";
        }
        return null;
    }
    
    public Proveedor buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return this.DATOS_PROV.buscarPorId(id);
    }
    
    public String insertar(Proveedor p) {
        String error = validar(p);
        if (error != null) {
            return error;
        }
        
        Optional<Proveedor> existe = this.DATOS_PROV.buscarPorRuc(p.getRuc());
        if (existe.isPresent()) {
            return "El RUC ingresado ya pertenece a otro proveedor.";
        }
        
        if (this.DATOS_PROV.insertar(p)) {
            return null; 
        } else {
            return "Error desconocido al insertar el proveedor.";
        }
    }
    
    public String actualizar(Proveedor p) {
        String error = validar(p);
        if (error != null) {
            return error;
        }
        
        Optional<Proveedor> existe = this.DATOS_PROV.buscarPorRuc(p.getRuc());
        if (existe.isPresent() && existe.get().getProveedorId() != p.getProveedorId()) {
            return "El RUC ingresado ya pertenece a otro proveedor.";
        }
        
        if (this.DATOS_PROV.actualizar(p)) {
            return null; 
        } else {
            return "Error desconocido al actualizar el proveedor.";
        }
    }
    
    public String desactivar(int id) {
        if (this.DATOS_PROV.eliminar(id)) {
            return null; 
        } else {
            return "Error desconocido al desactivar el proveedor.";
        }
    }
}