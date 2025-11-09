package com.ferreteria.conexion; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    private static Connection cnx = null;
    
    private static final String URL = "jdbc:postgresql://localhost:5432/ferreteria_db";
    private static final String DRIVER = "org.postgresql.Driver";
    
    private static final String USUARIO = "postgres"; 
    private static final String PASSWORD = "200414"; 

    
    private Conexion() {
    }

    public static Connection obtenerConexion() {
        try {
            if (cnx == null || cnx.isClosed()) {
                
                Class.forName(DRIVER);
                
                cnx = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                
                System.out.println("✅ Conexión a BD 'ferreteria_db' establecida.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("❌ Error fatal al conectar a la Base de Datos:");
            System.err.println("Mensaje: " + ex.getMessage());
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cnx;
    }


    public static void cerrarConexion() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println("🔌 Conexión a BD cerrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}