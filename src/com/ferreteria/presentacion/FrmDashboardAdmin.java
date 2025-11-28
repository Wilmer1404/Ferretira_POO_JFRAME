package com.ferreteria.presentacion;

import com.ferreteria.entidades.Empleado;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class FrmDashboardAdmin extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDashboardAdmin.class.getName());
    
    private Empleado empleadoLogueado;

    public FrmDashboardAdmin(Empleado empleado) {
        initComponents();
        this.empleadoLogueado = empleado; 
        this.setSize(1050, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("Panel de Administración - FerreteriaApp - Usuario: " + empleado.getEmail());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPrincipal = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuGestion = new javax.swing.JMenu();
        itemGestionProductos = new javax.swing.JMenuItem();
        itemGestionEmpleados = new javax.swing.JMenuItem();
        itemGestionProveedores = new javax.swing.JMenuItem();
        itemRegistroCompra = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        itemReporteVentas = new javax.swing.JMenuItem();
        menusistema = new javax.swing.JMenu();
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
            .addGap(0, 261, Short.MAX_VALUE)
        );

        menuGestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/categorias.png"))); // NOI18N
        menuGestion.setText("Gestión");

        itemGestionProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/product.png"))); // NOI18N
        itemGestionProductos.setText("Gestionar Productos");
        itemGestionProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGestionProductosActionPerformed(evt);
            }
        });
        menuGestion.add(itemGestionProductos);

        itemGestionEmpleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/empl1.png"))); // NOI18N
        itemGestionEmpleados.setText("Gestionar Empleados");
        itemGestionEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGestionEmpleadosActionPerformed(evt);
            }
        });
        menuGestion.add(itemGestionEmpleados);

        itemGestionProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/proveedor.png"))); // NOI18N
        itemGestionProveedores.setText("Gestionar Proveedores\"");
        itemGestionProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGestionProveedoresActionPerformed(evt);
            }
        });
        menuGestion.add(itemGestionProveedores);

        itemRegistroCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/compras.png"))); // NOI18N
        itemRegistroCompra.setText("Registro de Compras");
        itemRegistroCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRegistroCompraActionPerformed(evt);
            }
        });
        menuGestion.add(itemRegistroCompra);

        jMenuBar1.add(menuGestion);

        menuReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/reporte.png"))); // NOI18N
        menuReportes.setText("Reportes");

        itemReporteVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/repors.png"))); // NOI18N
        itemReporteVentas.setText("Reporte de Ventas");
        itemReporteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemReporteVentasActionPerformed(evt);
            }
        });
        menuReportes.add(itemReporteVentas);

        jMenuBar1.add(menuReportes);

        menusistema.setText("Sistema");

        itemSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/salir.png"))); // NOI18N
        itemSalir.setText("Salir");
        itemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSalirActionPerformed(evt);
            }
        });
        menusistema.add(itemSalir);

        jMenuBar1.add(menusistema);

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
        FrmGestionProductos frm = new FrmGestionProductos();

        abrirVentanaInterna(frm);
    }//GEN-LAST:event_itemGestionProductosActionPerformed

    private void itemGestionEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGestionEmpleadosActionPerformed
        FrmGestionEmpleados frm = new FrmGestionEmpleados();
        abrirVentanaInterna(frm);
    }//GEN-LAST:event_itemGestionEmpleadosActionPerformed

    private void itemReporteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemReporteVentasActionPerformed
        FrmReporteVentas frm = new FrmReporteVentas();
        abrirVentanaInterna(frm);
    }//GEN-LAST:event_itemReporteVentasActionPerformed

    private void itemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_itemSalirActionPerformed

    private void itemGestionProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGestionProveedoresActionPerformed
        FrmGestionProveedores frm = new FrmGestionProveedores();
        abrirVentanaInterna(frm);
    }//GEN-LAST:event_itemGestionProveedoresActionPerformed

    private void itemRegistroCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRegistroCompraActionPerformed
        FrmRegistroCompra frm = new FrmRegistroCompra(this.empleadoLogueado);
        abrirVentanaInterna(frm);
    }//GEN-LAST:event_itemRegistroCompraActionPerformed

    


    private void abrirVentanaInterna(JInternalFrame frame) {
        for (JInternalFrame openFrame : desktopPrincipal.getAllFrames()) {
            if (openFrame.getClass().equals(frame.getClass())) {
                openFrame.toFront();
                try {
                    openFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
                return;
            }
        }

        Dimension desktopSize = desktopPrincipal.getSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation(
                (desktopSize.width - frameSize.width) / 2,
                (desktopSize.height - frameSize.height) / 2
        );

        desktopPrincipal.add(frame);
        frame.setVisible(true);

        try {
            frame.setMaximum(true);  
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPrincipal;
    private javax.swing.JMenuItem itemGestionEmpleados;
    private javax.swing.JMenuItem itemGestionProductos;
    private javax.swing.JMenuItem itemGestionProveedores;
    private javax.swing.JMenuItem itemRegistroCompra;
    private javax.swing.JMenuItem itemReporteVentas;
    private javax.swing.JMenuItem itemSalir;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu menuGestion;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenu menusistema;
    // End of variables declaration//GEN-END:variables
}
