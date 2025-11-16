package com.ferreteria.datos.interfaces;

import com.ferreteria.entidades.Cliente;
import java.util.Optional;

/**
 * Interfaz DAO específica para la gestión de clientes en la base de datos.
 * Extiende las operaciones CRUD básicas con funcionalidades específicas
 * para clientes como búsqueda por DNI y email.
 * 
 * Estas búsquedas adicionales son importantes para:
 * - Validar unicidad del DNI al registrar nuevos clientes
 * - Autenticación por email en el sistema de login
 */
public interface IClienteDAO extends ICrudDAO<Cliente, Integer> {

    /**
     * Busca un cliente por su número de DNI
     * @param dni Documento Nacional de Identidad del cliente
     * @return Optional con el cliente si existe, Optional.empty() si no existe
     */
    public Optional<Cliente> buscarPorDni(String dni);

    /**
     * Busca un cliente por su dirección de email
     * @param email Correo electrónico del cliente
     * @return Optional con el cliente si existe, Optional.empty() si no existe
     */
    public Optional<Cliente> buscarPorEmail(String email);
}