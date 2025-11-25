package com.ferreteria.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    private static Connection cnx = null;
    
    private static final String URL = "jdbc:mysql://localhost:3306/ferreteria_db?serverTimezone=UTC";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static final String USUARIO = "root"; 
    private static final String PASSWORD = "$Marcona2025$"; 

    
    private Conexion() {
    }

    public static Connection obtenerConexion() {
        try {
            if (cnx == null || cnx.isClosed()) {
                
                Class.forName(DRIVER);
                
                cnx = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                
                System.out.println("Conexion a BD MySQL 'ferreteria_db' establecida.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(" Error fatal al conectar a la Base de Datos (MySQL):");
            System.err.println("Mensaje: " + ex.getMessage());
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cnx;
    }


    public static void cerrarConexion() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println(" Conexion a BD MySQL cerrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}