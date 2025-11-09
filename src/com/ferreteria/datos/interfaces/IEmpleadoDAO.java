package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.Empleado;
import java.util.Optional;

public interface IEmpleadoDAO extends ICrudDAO<Empleado, Integer> {

    public Optional<Empleado> buscarPorEmail(String email);
}