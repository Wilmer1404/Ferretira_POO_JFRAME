package com.ferreteria.conexion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    private static final String URL = "jdbc:postgresql://localhost:5432/ferreteria_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "200414"; 
    
    private static final Logger LOGGER = Logger.getLogger(Conexion.class.getName());
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setDriverClassName("org.postgresql.Driver");
            
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
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

    public static Connection obtenerConexion() throws SQLException {
        if (dataSource == null) {
            LOGGER.log(Level.SEVERE, "El pool (DataSource) no fue inicializado.");
            throw new SQLException("No se puede obtener conexión, el DataSource es nulo.");
        }
        return dataSource.getConnection();
    }
}