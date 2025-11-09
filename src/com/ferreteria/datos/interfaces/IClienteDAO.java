package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.Cliente;
import java.util.Optional; // Usamos Optional para un manejo moderno de nulos

public interface IClienteDAO extends ICrudDAO<Cliente, Integer> {

    public Optional<Cliente> buscarPorDni(String dni);

    public Optional<Cliente> buscarPorEmail(String email);
}