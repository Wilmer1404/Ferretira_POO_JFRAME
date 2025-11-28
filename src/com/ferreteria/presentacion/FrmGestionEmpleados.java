package com.ferreteria.presentacion;

import com.ferreteria.entidades.Empleado;
import com.ferreteria.negocio.EmpleadoNegocio;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class FrmGestionEmpleados extends javax.swing.JInternalFrame {

    private final EmpleadoNegocio EMPLEADO_NEGOCIO;
    private DefaultTableModel modeloTabla;
    private String accion;
    private int idSeleccionado;

    public FrmGestionEmpleados() {
        initComponents();
        
        this.setSize(900, 600);
        this.setMinimumSize(new java.awt.Dimension(800, 500));
        this.EMPLEADO_NEGOCIO = new EmpleadoNegocio();
        
        this.definirCabecerasTabla();
        this.cargarCombos(); 

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
        this.listarEmpleados();
            this.gestionarEstadoFormulario("INICIO");
    }

    private void definirCabecerasTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellidos");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Rol");
        modeloTabla.addColumn("Estado");

        this.tablaEmpleados.setModel(modeloTabla);

        this.tablaEmpleados.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tablaEmpleados.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tablaEmpleados.getColumnModel().getColumn(2).setPreferredWidth(120);
        this.tablaEmpleados.getColumnModel().getColumn(3).setPreferredWidth(150);
        this.tablaEmpleados.getColumnModel().getColumn(4).setPreferredWidth(180);
        this.tablaEmpleados.getColumnModel().getColumn(5).setPreferredWidth(80);
        this.tablaEmpleados.getColumnModel().getColumn(6).setPreferredWidth(70);

    }

    private void cargarCombos() {
        DefaultComboBoxModel<String> modeloRol = (DefaultComboBoxModel<String>) cmbRol.getModel();
        modeloRol.removeAllElements();
        modeloRol.addElement("ADMIN");
        modeloRol.addElement("VENDEDOR");

        DefaultComboBoxModel<String> modeloActivo = (DefaultComboBoxModel<String>) cmbActivo.getModel();
        modeloActivo.removeAllElements();
        modeloActivo.addElement("Activo");
        modeloActivo.addElement("Inactivo");
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
                limpiarFormulario();
                bloquearControles(false);
                txtPassword.setEnabled(true);
                break;

            case "EDITAR":
                this.accion = "editar";
                btnNuevo.setEnabled(false);
                btnGuardar.setEnabled(true);
                btnGuardar.setText("Actualizar");
                btnEditar.setEnabled(false);
                btnDesactivar.setEnabled(false);
                btnCancelar.setEnabled(true);
                bloquearControles(false);
                txtPassword.setEnabled(false);
                break;

            case "SELECCIONADO":
                btnNuevo.setEnabled(true);
                btnGuardar.setEnabled(false);
                btnEditar.setEnabled(true);
                btnDesactivar.setEnabled(true);
                btnCancelar.setEnabled(false);
                bloquearControles(true);
                break;
        }
    }

    private void bloquearControles(boolean bloquear) {
        txtDni.setEnabled(!bloquear);
        txtNombre.setEnabled(!bloquear);
        txtApellidos.setEnabled(!bloquear);
        txtEmail.setEnabled(!bloquear);
        cmbRol.setEnabled(!bloquear);
        cmbActivo.setEnabled(!bloquear);

        if (this.accion.equals("guardar")) {
            txtPassword.setEnabled(!bloquear);
        } else {
            txtPassword.setEnabled(false);
        }
    }

    private void limpiarFormulario() {
        txtDni.setText("");
        txtNombre.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
        cmbActivo.setSelectedIndex(0);

        bloquearControles(true);
    }

    private void listarEmpleados() {
        try {
            modeloTabla.setRowCount(0);
            List<Empleado> lista = this.EMPLEADO_NEGOCIO.listar();

            if (lista.isEmpty()) {
                return;
            }

            for (Empleado emp : lista) {
                Object[] fila = new Object[7];
                fila[0] = emp.getEmpleadoId();
                fila[1] = emp.getDni();
                fila[2] = emp.getNombre();
                fila[3] = emp.getApellidos();
                fila[4] = emp.getEmail();
                fila[5] = emp.getRol();
                fila[6] = emp.isActivo() ? "Activo" : "Inactivo";
                modeloTabla.addRow(fila);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al listar empleados: " + ex.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFormulario = new javax.swing.JPanel();
        lblDni = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblApellidos = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblRol = new javax.swing.JLabel();
        cmbRol = new javax.swing.JComboBox<>();
        lblActivo = new javax.swing.JLabel();
        cmbActivo = new javax.swing.JComboBox<>();
        panelAcciones = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaEmpleados = new javax.swing.JTable();

        panelFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        lblDni.setText("DNI:");

        lblNombre.setText("Nombre:");

        lblApellidos.setText("Apellidos:");

        lblEmail.setText("Email:");

        lblPassword.setText("Contraseña:");

        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        lblRol.setText("Rol:");

        cmbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "VENDEDOR", " " }));

        lblActivo.setText("Estado");

        cmbActivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

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
                .addContainerGap()
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEditar)
                .addGap(18, 18, 18)
                .addComponent(btnDesactivar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnEditar)
                    .addComponent(btnDesactivar)
                    .addComponent(btnCancelar))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(lblDni)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addComponent(lblEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addComponent(lblRol)
                                .addGap(18, 18, 18)
                                .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActivo)
                            .addComponent(lblPassword))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelFormularioLayout.createSequentialGroup()
                            .addComponent(lblApellidos)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtPassword))
                    .addComponent(cmbActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDni)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApellidos)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRol)
                    .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActivo)
                    .addComponent(cmbActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaEmpleadosMousePressed(evt);
            }
        });
        scrollTabla.setViewportView(tablaEmpleados);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollTabla)
            .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (txtDni.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo DNI es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtDni.requestFocus();
            return;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Email es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        String passwordPlano = new String(txtPassword.getPassword());

        if (this.accion.equals("guardar") && passwordPlano.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Contraseña es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        Empleado emp = new Empleado();
        emp.setDni(txtDni.getText().trim());
        emp.setNombre(txtNombre.getText().trim());
        emp.setApellidos(txtApellidos.getText().trim());
        emp.setEmail(txtEmail.getText().trim());
        emp.setRol((String) cmbRol.getSelectedItem());
        emp.setActivo(cmbActivo.getSelectedItem().equals("Activo"));

        String respuesta = null;

        if (this.accion.equals("guardar")) {

            respuesta = this.EMPLEADO_NEGOCIO.insertar(emp, passwordPlano);

        } else {
            emp.setEmpleadoId(this.idSeleccionado);
            respuesta = this.EMPLEADO_NEGOCIO.actualizar(emp);
        }

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Empleado guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.listarEmpleados();
            this.gestionarEstadoFormulario("INICIO");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.gestionarEstadoFormulario("EDITAR");
        txtDni.requestFocus();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea desactivar a este empleado?\nID: " + this.idSeleccionado + " - " + txtNombre.getText(),
                "Confirmar Desactivación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String respuesta = this.EMPLEADO_NEGOCIO.desactivar(this.idSeleccionado);

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Empleado desactivado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.listarEmpleados();
            this.gestionarEstadoFormulario("INICIO");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.gestionarEstadoFormulario("INICIO");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.gestionarEstadoFormulario("NUEVO");
        txtDni.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void tablaEmpleadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEmpleadosMousePressed
        if (modeloTabla.getRowCount() == 0) {
            return;
        }

        int fila = this.tablaEmpleados.getSelectedRow();
        if (fila < 0) {
            return;
        }

        try {
            this.idSeleccionado = (int) this.modeloTabla.getValueAt(fila, 0);

            txtDni.setText((String) modeloTabla.getValueAt(fila, 1));
            txtNombre.setText((String) modeloTabla.getValueAt(fila, 2));
            txtApellidos.setText((String) modeloTabla.getValueAt(fila, 3));
            txtEmail.setText((String) modeloTabla.getValueAt(fila, 4));
            cmbRol.setSelectedItem((String) modeloTabla.getValueAt(fila, 5));
            cmbActivo.setSelectedItem((String) modeloTabla.getValueAt(fila, 6));
            txtPassword.setText("");

            this.gestionarEstadoFormulario("SELECCIONADO");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tablaEmpleadosMousePressed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbActivo;
    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JLabel lblActivo;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRol;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaEmpleados;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
