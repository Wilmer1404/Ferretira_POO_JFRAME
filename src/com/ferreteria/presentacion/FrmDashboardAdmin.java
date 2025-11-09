package com.ferreteria.presentacion;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class FrmDashboardAdmin extends javax.swing.JFrame {
        
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDashboardAdmin.class.getName());

    public FrmDashboardAdmin() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Panel de Administración - FerreteriaApp");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPrincipal = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuGestion = new javax.swing.JMenu();
        itemGestionProductos = new javax.swing.JMenuItem();
        itemGestionEmpleados = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        itemReporteVentas = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        itemSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout desktopPrincipalLayout = new javax.swing.GroupLayout(desktopPrincipal);
        desktopPrincipal.setLayout(desktopPrincipalLayout);
        desktopPrincipalLayout.setHorizontalGroup(
            desktopPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        desktopPrincipalLayout.setVerticalGroup(
            desktopPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        menuGestion.setText("Gestión");

        itemGestionProductos.setText("Gestionar Productos");
        itemGestionProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGestionProductosActionPerformed(evt);
            }
        });
        menuGestion.add(itemGestionProductos);

        itemGestionEmpleados.setText("Gestionar Empleados");
        itemGestionEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGestionEmpleadosActionPerformed(evt);
            }
        });
        menuGestion.add(itemGestionEmpleados);

        jMenuBar1.add(menuGestion);

        menuReportes.setText("Reportes");

        itemReporteVentas.setText("Reporte de Ventas");
        itemReporteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemReporteVentasActionPerformed(evt);
            }
        });
        menuReportes.add(itemReporteVentas);

        jMenuBar1.add(menuReportes);

        jMenu3.setText("Sistema");

        itemSalir.setText("Salir");
        itemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSalirActionPerformed(evt);
            }
        });
        jMenu3.add(itemSalir);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPrincipal, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPrincipal, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemGestionProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGestionProductosActionPerformed
        // 1. Creas la instancia del formulario
    FrmGestionProductos frm = new FrmGestionProductos();
    
    // 2. Lo añades a tu panel de escritorio (ej. jDesktopPane1)
    desktopPrincipal.add(frm);
    
    // 3. Lo haces visible
    frm.setVisible(true);
    
    // 4. Y AQUÍ LO MAXIMIZAS
    try {
        frm.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        // Manejar la excepción
        // e.printStackTrace();
    }
    }//GEN-LAST:event_itemGestionProductosActionPerformed

    private void itemGestionEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGestionEmpleadosActionPerformed
        // TODO: Abrir el JInternalFrame de Gestión de Empleados
    }//GEN-LAST:event_itemGestionEmpleadosActionPerformed

    private void itemReporteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemReporteVentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemReporteVentasActionPerformed

    private void itemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_itemSalirActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new FrmDashboardAdmin().setVisible(true));
    }
    
    private void abrirVentanaInterna(JInternalFrame frame) {
        // (Lógica avanzada opcional: verificar si ya está abierto)
        
        // Centrar la ventana interna
        Dimension desktopSize = desktopPrincipal.getSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation((desktopSize.width - frameSize.width) / 2, (desktopSize.height - frameSize.height) / 2);
        
        desktopPrincipal.add(frame);
        frame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPrincipal;
    private javax.swing.JMenuItem itemGestionEmpleados;
    private javax.swing.JMenuItem itemGestionProductos;
    private javax.swing.JMenuItem itemReporteVentas;
    private javax.swing.JMenuItem itemSalir;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu menuGestion;
    private javax.swing.JMenu menuReportes;
    // End of variables declaration//GEN-END:variables
}
