package com.ferreteria.presentacion;

import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.Venta;
import com.ferreteria.negocio.VentaNegocio;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.ferreteria.negocio.ClienteNegocio;
import com.ferreteria.entidades.Cliente;
import java.util.ArrayList;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class FrmReporteVentas extends javax.swing.JInternalFrame {

    private final VentaNegocio VENTA_NEGOCIO;
    private final ClienteNegocio CLIENTE_NEGOCIO;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado;
    private List<Venta> listaActual;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public FrmReporteVentas() {
        initComponents();

        this.VENTA_NEGOCIO = new VentaNegocio();
        this.CLIENTE_NEGOCIO = new ClienteNegocio();
        this.listaActual = new ArrayList<>();        
        this.definirCabecerasTabla();
        setClosable(true);
        setResizable(true);
        this.refrescarDatos(); 
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent evt) {
                refrescarDatos();
            }
        });
    }
    
    private void refrescarDatos() {
        this.listarVentas();
        this.txtBuscarDni.setText("");
        this.btnVerDetalle.setEnabled(false);
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
            this.listaActual = this.VENTA_NEGOCIO.listar();
            this.cargarTabla(this.listaActual);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al listar ventas: " + ex.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(List<Venta> lista) {
        modeloTabla.setRowCount(0);
        btnVerDetalle.setEnabled(false);
        this.idSeleccionado = 0;

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron ventas para el criterio seleccionado.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTabla = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        panelControles = new javax.swing.JPanel();
        btnRefrescar = new javax.swing.JButton();
        btnVerDetalle = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarDni = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();

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
        tablaVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tablaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaVentasMousePressed(evt);
            }
        });
        scrollTabla.setViewportView(tablaVentas);

        getContentPane().add(scrollTabla, java.awt.BorderLayout.PAGE_START);

        panelControles.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones del Reporte"));

        btnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/refresca.png"))); // NOI18N
        btnRefrescar.setText("Refrescar Lista");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        btnVerDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/ventas.png"))); // NOI18N
        btnVerDetalle.setText("Ver Detalle de Venta");
        btnVerDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetalleActionPerformed(evt);
            }
        });

        jLabel1.setText("Buscar por Nombre:");

        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/buscar.png"))); // NOI18N
        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(btnRefrescar)
                        .addGap(65, 65, 65)
                        .addComponent(jLabel1))
                    .addComponent(btnVerDetalle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBuscarDni, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(155, Short.MAX_VALUE))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefrescar)
                    .addComponent(jLabel1)
                    .addComponent(txtBuscarDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVerDetalle)
                    .addComponent(btnBuscarCliente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelControles, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        this.refrescarDatos();
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

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        String dni = txtBuscarDni.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un DNI para buscar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<Cliente> optCliente = this.CLIENTE_NEGOCIO.buscarPorDni(dni);

        if (!optCliente.isPresent()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el DNI: " + dni, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = optCliente.get();
        this.listaActual = this.VENTA_NEGOCIO.listarPorCliente(cliente.getClienteId());

        this.cargarTabla(this.listaActual);
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void tablaVentasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVentasMousePressed
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
    }//GEN-LAST:event_tablaVentasMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnVerDetalle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelControles;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField txtBuscarDni;
    // End of variables declaration//GEN-END:variables
//holap

}
