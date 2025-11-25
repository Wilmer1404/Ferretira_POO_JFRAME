package com.ferreteria.datos.interfaces;

import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD (Create, Read, Update, Delete)
 * básicas para el acceso a datos. Implementa el patrón DAO (Data Access Object).
 * 
 * Esta interfaz es heredada por todas las interfaces DAO específicas del sistema,
 * proporcionando un contrato común para las operaciones de base de datos.
 * 
 * @param <T> Tipo de entidad que maneja este DAO
 * @param <ID> Tipo de dato del identificador único de la entidad
 */
public interface ICrudDAO<T, ID> {

    /**
     * Lista todas las entidades de la base de datos
     * @return Lista completa de entidades del tipo T
     */
    public List<T> listarTodos();
    
    /**
     * Busca una entidad por su identificador único
     * @param id Identificador único de la entidad
     * @return Entidad encontrada o null si no existe
     */
    public T buscarPorId(ID id);

    /**
     * Inserta una nueva entidad en la base de datos
     * @param entidad Entidad a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(T entidad);
    
    /**
     * Actualiza una entidad existente en la base de datos
     * @param entidad Entidad con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizar(T entidad);

    /**
     * Elimina lógicamente una entidad (marca como inactiva)
     * @param id Identificador único de la entidad a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar(ID id);
    
}