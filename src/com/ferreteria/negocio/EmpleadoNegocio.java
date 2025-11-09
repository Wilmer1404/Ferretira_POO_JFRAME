package com.ferreteria.negocio;

import com.ferreteria.datos.impl.EmpleadoDAOImpl;
import com.ferreteria.datos.interfaces.IEmpleadoDAO;
import com.ferreteria.entidades.Empleado;
import java.util.Optional;


public class EmpleadoNegocio {
    
    private final IEmpleadoDAO DATOS_EMP;

    public EmpleadoNegocio() {
        this.DATOS_EMP = new EmpleadoDAOImpl(); 
    }

    public Optional<Empleado> login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Empleado> empleadoOpt = this.DATOS_EMP.buscarPorEmail(email);
        
        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            
            if (empleado.getPasswordHash().equals(password) && empleado.isActivo()) {
                
                
                return Optional.of(empleado); 
            }
        }
        
        return Optional.empty(); 
    }
    
}