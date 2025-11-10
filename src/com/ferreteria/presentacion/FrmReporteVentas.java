package com.ferreteria.presentacion;

import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.Venta;
import com.ferreteria.negocio.VentaNegocio;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmReporteVentas extends javax.swing.JInternalFrame {

    private final VentaNegocio VENTA_NEGOCIO;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public FrmReporteVentas() {
        initComponents();

        this.VENTA_NEGOCIO = new VentaNegocio();
        this.definirCabecerasTabla();
        this.listarVentas();
        btnVerDetalle.setEnabled(false);
    }

    private void definirCabecerasTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID Venta");
        modeloTabla.addColumn("Fecha y Hora");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Atendido por");
        modeloTabla.addColumn("Método Pago");
        modeloTabla.addColumn("Total");

        this.tablaVentas.setModel(modeloTabla);
        tablaVentas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaVentas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaVentas.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaVentas.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaVentas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaVentas.getColumnModel().getColumn(5).setPreferredWidth(80);
    }

    private void listarVentas() {
        try {
            modeloTabla.setRowCount(0);
            List<Venta> lista = this.VENTA_NEGOCIO.listar();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron ventas registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Venta venta : lista) {
                Object[] fila = new Object[6];
                fila[0] = venta.getVentaId();
                fila[1] = venta.getFechaVenta().format(formatter);
                fila[2] = venta.getCliente().getNombre() + " " + venta.getCliente().getApellidos();
                fila[3] = venta.getEmpleado().getNombre(); 
                fila[4] = venta.getMetodoPago();
                fila[5] = venta.getTotal();

                modeloTabla.addRow(fila);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al listar ventas: " + ex.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelControles = new javax.swing.JPanel();
        btnRefrescar = new javax.swing.JButton();
        btnVerDetalle = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();

        panelControles.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones del Reporte"));

        btnRefrescar.setText("Refrescar Lista");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        btnVerDetalle.setText("Ver Detalle de Venta");
        btnVerDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetalleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefrescar)
                .addGap(18, 18, 18)
                .addComponent(btnVerDetalle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefrescar)
                    .addComponent(btnVerDetalle))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(panelControles, java.awt.BorderLayout.CENTER);

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollTabla.setViewportView(tablaVentas);

        getContentPane().add(scrollTabla, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        this.listarVentas();
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnVerDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerDetalleActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una venta de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<Venta> optVenta = this.VENTA_NEGOCIO.buscarPorId(this.idSeleccionado);

        if (!optVenta.isPresent()) {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar la venta (ID: " + this.idSeleccionado + ").", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Venta venta = optVenta.get();
        StringBuilder sb = new StringBuilder();
        sb.append("--- Detalle de la Venta ID: ").append(venta.getVentaId()).append(" ---\n");
        sb.append("Cliente: ").append(venta.getCliente().getNombre()).append(" ").append(venta.getCliente().getApellidos()).append("\n");
        sb.append("Fecha: ").append(venta.getFechaVenta().format(formatter)).append("\n");
        sb.append("Total Pagado: S/ ").append(venta.getTotal()).append("\n");
        sb.append("\n--- Productos Comprados ---\n");

        for (DetalleVenta det : venta.getDetalles()) {
            sb.append(String.format("  - %s (Cant: %s, P.U.: S/ %s, Subtotal: S/ %s)\n",
                    det.getItem().getNombre(),
                    det.getCantidad(),
                    det.getPrecioHistorico(),
                    det.getSubtotal()
            ));
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Detalle de Venta", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_btnVerDetalleActionPerformed

    private void tablaVentasMousePressed(java.awt.event.MouseEvent evt) {                                         
        if (modeloTabla.getRowCount() == 0) {
            return;
        }

        int fila = this.tablaVentas.getSelectedRow();
        if (fila < 0) {
            return;
        }

        try {
            this.idSeleccionado = (int) this.modeloTabla.getValueAt(fila, 0);

            btnVerDetalle.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                             


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnVerDetalle;
    private javax.swing.JPanel panelControles;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaVentas;
    // End of variables declaration//GEN-END:variables
}
