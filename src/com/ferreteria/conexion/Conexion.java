package com.ferreteria.conexion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de gestionar las conexiones a la base de datos PostgreSQL
 * utilizando el pool de conexiones HikariCP para optimizar el rendimiento.
 * 
 * Esta clase implementa el patrón Singleton y maneja todas las conexiones
 * de manera centralizada, proporcionando configuración de pool de conexiones
 * para mejorar la eficiencia en el acceso a la base de datos.
 */
public class Conexion {

    // Configuración de la base de datos PostgreSQL local
    private static final String URL = "jdbc:postgresql://localhost:5432/ferreteria_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "200414"; 
    
    private static final Logger LOGGER = Logger.getLogger(Conexion.class.getName());
    private static HikariDataSource dataSource; // Pool de conexiones HikariCP

    // Inicialización estática del pool de conexiones
    static {
        try {
            // Configuración del pool de conexiones HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setDriverClassName("org.postgresql.Driver");
            
            // Configuración de parámetros del pool
            config.setMaximumPoolSize(10);        // Máximo 10 conexiones
            config.setMinimumIdle(2);             // Mínimo 2 conexiones inactivas
            config.setConnectionTimeout(30000);   // Timeout de 30 segundos
            config.setIdleTimeout(600000);        // Timeout de inactividad 10 minutos
            config.setMaxLifetime(1800000);       // Vida máxima de conexión 30 minutos
            
            // Optimizaciones de rendimiento para PostgreSQL
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            
            LOGGER.log(Level.INFO, "Pool de Conexiones (HikariCP) inicializado.");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar el pool de conexiones HikariCP", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene una conexión del pool de conexiones.
     * 
     * @return Connection objeto de conexión a la base de datos.
     * @throws SQLException si no se puede obtener una conexión del DataSource.
     */
    public static Connection obtenerConexion() throws SQLException {
        if (dataSource == null) {
            LOGGER.log(Level.SEVERE, "El pool (DataSource) no fue inicializado.");
            throw new SQLException("No se puede obtener conexión, el DataSource es nulo.");
        }
        return dataSource.getConnection();
    }
}