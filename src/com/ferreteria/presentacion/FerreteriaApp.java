package com.ferreteria.presentacion;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FerreteriaApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                System.err.println("No se pudo aplicar el Look & Feel Nimbus.");
            }
            
            FrmLogin loginForm = new FrmLogin();
            
            loginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginForm.setLocationRelativeTo(null); 
            loginForm.setVisible(true);
        });
    }
}