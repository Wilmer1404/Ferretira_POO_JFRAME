package com.ferreteria.negocio;

import com.ferreteria.datos.impl.CategoriaDAOImpl;
import com.ferreteria.datos.interfaces.ICategoriaDAO;
import com.ferreteria.entidades.Categoria;
import java.util.List;


public class CategoriaNegocio {
    
    private final ICategoriaDAO DATOS_CAT;

    public CategoriaNegocio() {
        this.DATOS_CAT = new CategoriaDAOImpl();
    }
    

    public List<Categoria> listar() {
        return this.DATOS_CAT.listarTodos();
    }
    
}