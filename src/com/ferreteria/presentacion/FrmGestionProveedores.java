package com.ferreteria.presentacion;

import com.ferreteria.entidades.Proveedor;
import com.ferreteria.negocio.ProveedorNegocio;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class FrmGestionProveedores extends javax.swing.JInternalFrame {

    private final ProveedorNegocio PROVEEDOR_NEGOCIO;
    private DefaultTableModel modeloTabla;
    private String accion;
    private int idSeleccionado;

    public FrmGestionProveedores() {
        initComponents(); 

        this.setSize(900, 600);
        this.PROVEEDOR_NEGOCIO = new ProveedorNegocio();
        
        this.definirCabecerasTabla();
        this.cargarComboActivo(); 

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
        this.listarProveedores("");         
        this.gestionarEstadoFormulario("INICIO");        
        this.txtBuscar.setText("");
    }

    private void definirCabecerasTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("RUC");
        modeloTabla.addColumn("Razón Social");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Estado");

        this.tablaProveedores.setModel(modeloTabla);
        tablaProveedores.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaProveedores.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaProveedores.getColumnModel().getColumn(2).setPreferredWidth(200);
    }

    private void cargarComboActivo() {
        DefaultComboBoxModel<String> modeloActivo = (DefaultComboBoxModel<String>) cmbActivo.getModel();
        modeloActivo.removeAllElements();
        modeloActivo.addElement("Activo");
        modeloActivo.addElement("Inactivo");
    }

    private void bloquearControles(boolean bloquear) {
        txtRuc.setEnabled(!bloquear);
        txtRazonSocial.setEnabled(!bloquear);
        txtEmail.setEnabled(!bloquear);
        txtTelefono.setEnabled(!bloquear);
        txtDireccion.setEnabled(!bloquear);
        cmbActivo.setEnabled(!bloquear);
    }

    private void limpiarFormulario() {
        txtRuc.setText("");
        txtRazonSocial.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        cmbActivo.setSelectedIndex(0);
        this.idSeleccionado = 0;
        bloquearControles(true);
    }

    private void gestionarEstadoFormulario(String estado) {
        switch (estado) {
            case "INICIO":
                this.accion = "guardar";
                btnNuevo.setEnabled(true);
                btnGuardar.setEnabled(false);
                btnEditar.setEnabled(false);
                btnDesactivar.setEnabled(false);
                btnCancelar.setEnabled(false);
                txtBuscar.setEnabled(true);
                btnBuscar.setEnabled(true);
                limpiarFormulario();
                break;
            case "NUEVO":
                this.accion = "guardar";
                btnNuevo.setEnabled(false);
                btnGuardar.setEnabled(true);
                btnGuardar.setText("Guardar");
                btnEditar.setEnabled(false);
                btnDesactivar.setEnabled(false);
                btnCancelar.setEnabled(true);
                txtBuscar.setEnabled(false);
                btnBuscar.setEnabled(false);
                limpiarFormulario();
                bloquearControles(false);
                break;
            case "EDITAR":
                this.accion = "editar";
                btnNuevo.setEnabled(false);
                btnGuardar.setEnabled(true);
                btnGuardar.setText("Actualizar");
                btnEditar.setEnabled(false);
                btnDesactivar.setEnabled(false);
                btnCancelar.setEnabled(true);
                txtBuscar.setEnabled(false);
                btnBuscar.setEnabled(false);
                bloquearControles(false);
                break;
            case "SELECCIONADO":
                btnNuevo.setEnabled(true);
                btnGuardar.setEnabled(false);
                btnEditar.setEnabled(true);
                btnDesactivar.setEnabled(true);
                btnCancelar.setEnabled(false);
                txtBuscar.setEnabled(true);
                btnBuscar.setEnabled(true);
                bloquearControles(true);
                break;
        }
    }

    private void listarProveedores(String terminoBusqueda) {
        try {
            modeloTabla.setRowCount(0);
            List<Proveedor> lista = this.PROVEEDOR_NEGOCIO.buscar(terminoBusqueda);

            if (lista.isEmpty() && !terminoBusqueda.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron proveedores.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }

            for (Proveedor p : lista) {
                Object[] fila = new Object[6];
                fila[0] = p.getProveedorId();
                fila[1] = p.getRuc();
                fila[2] = p.getRazonSocial();
                fila[3] = p.getEmail();
                fila[4] = p.getTelefono();
                fila[5] = p.isActivo() ? "Activo" : "Inactivo";
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar proveedores: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFormulario = new javax.swing.JPanel();
        jblRuc = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
        lblRazonSocial = new javax.swing.JLabel();
        txtRazonSocial = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        cmbActivo = new javax.swing.JComboBox<>();
        panelAcciones = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btnDesactivar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();

        panelFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Proveedor"));

        jblRuc.setText("Ruc:");

        lblRazonSocial.setText("Razon Social:");

        lblEmail.setText("Email:");

        lblTelefono.setText("Telefono:");

        lblDireccion.setText("Direccion:");

        cmbActivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(lblRazonSocial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRazonSocial))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(jblRuc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail)))
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblDireccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblTelefono)
                        .addGap(12, 12, 12)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12))
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jblRuc)
                    .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefono)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRazonSocial)
                    .addComponent(txtRazonSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccion)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        panelAcciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/buscar.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/nuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/modificar.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnDesactivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/desactivar.png"))); // NOI18N
        btnDesactivar.setText("Desactivar");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAccionesLayout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(panelAccionesLayout.createSequentialGroup()
                        .addComponent(txtBuscar)
                        .addGap(30, 30, 30)))
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAccionesLayout.createSequentialGroup()
                        .addComponent(btnBuscar)
                        .addGap(293, 293, 293))
                    .addGroup(panelAccionesLayout.createSequentialGroup()
                        .addComponent(btnEditar)
                        .addGap(18, 18, 18)
                        .addComponent(btnDesactivar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnEditar)
                    .addComponent(btnDesactivar)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaProveedoresMousePressed(evt);
            }
        });
        scrollTabla.setViewportView(tablaProveedores);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollTabla)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        this.listarProveedores(txtBuscar.getText().trim());
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.gestionarEstadoFormulario("NUEVO");
        txtRuc.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.gestionarEstadoFormulario("INICIO");
        this.listarProveedores("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tablaProveedoresMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProveedoresMousePressed
        if (modeloTabla.getRowCount() == 0) {
            return;
        }
        int fila = this.tablaProveedores.getSelectedRow();
        if (fila < 0) {
            return;
        }

        try {
            this.idSeleccionado = (int) this.modeloTabla.getValueAt(fila, 0);

            Proveedor p = this.PROVEEDOR_NEGOCIO.buscarPorId(idSeleccionado); 

            if (p != null) {
                txtRuc.setText(p.getRuc());
                txtRazonSocial.setText(p.getRazonSocial());
                txtEmail.setText(p.getEmail());
                txtTelefono.setText(p.getTelefono());
                txtDireccion.setText(p.getDireccion());
                cmbActivo.setSelectedItem(p.isActivo() ? "Activo" : "Inactivo");

                this.gestionarEstadoFormulario("SELECCIONADO");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tablaProveedoresMousePressed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.gestionarEstadoFormulario("EDITAR");
        txtRuc.requestFocus();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea desactivar al proveedor: " + txtRazonSocial.getText() + "?",
                "Confirmar Desactivación", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String respuesta = this.PROVEEDOR_NEGOCIO.desactivar(this.idSeleccionado);
        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Proveedor desactivado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.listarProveedores("");
            this.gestionarEstadoFormulario("INICIO");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        Proveedor p = new Proveedor();
        p.setRuc(txtRuc.getText().trim());
        p.setRazonSocial(txtRazonSocial.getText().trim());
        p.setEmail(txtEmail.getText().trim());
        p.setTelefono(txtTelefono.getText().trim());
        p.setDireccion(txtDireccion.getText().trim());
        p.setActivo(cmbActivo.getSelectedItem().equals("Activo"));

        String respuesta = null;
        if (this.accion.equals("guardar")) {
            respuesta = this.PROVEEDOR_NEGOCIO.insertar(p);
        } else {
            p.setProveedorId(this.idSeleccionado);
            respuesta = this.PROVEEDOR_NEGOCIO.actualizar(p);
        }

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Proveedor guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.listarProveedores("");
            this.gestionarEstadoFormulario("INICIO");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbActivo;
    private javax.swing.JLabel jblRuc;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblRazonSocial;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaProveedores;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtRazonSocial;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
