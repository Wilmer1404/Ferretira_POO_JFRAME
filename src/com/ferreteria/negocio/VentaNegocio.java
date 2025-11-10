package com.ferreteria.negocio;

import com.ferreteria.datos.impl.VentaDAOImpl;
import com.ferreteria.datos.interfaces.IVentaDAO;
import com.ferreteria.entidades.Venta;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class VentaNegocio {
    
    private final IVentaDAO DATOS_VENTA;

    public VentaNegocio() {
        this.DATOS_VENTA = new VentaDAOImpl();
    }
    
    public List<Venta> listar() {
        return this.DATOS_VENTA.listarTodos();
    }

    public List<Venta> listarPorFechas(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            return this.DATOS_VENTA.listarTodos();
        }
        return this.DATOS_VENTA.listarPorFechas(inicio, fin);
    }
    
    public Optional<Venta> buscarPorId(int id) {
        Venta venta = this.DATOS_VENTA.buscarPorId(id);
        return Optional.ofNullable(venta); 
    }
}