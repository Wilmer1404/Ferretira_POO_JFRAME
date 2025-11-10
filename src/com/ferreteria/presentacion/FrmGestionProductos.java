package com.ferreteria.presentacion;

import com.ferreteria.entidades.Categoria;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Servicio;
import com.ferreteria.negocio.CategoriaNegocio;
import com.ferreteria.negocio.ProductoNegocio;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;


public class FrmGestionProductos extends javax.swing.JInternalFrame {
    
    private final ProductoNegocio PRODUCTO_NEGOCIO;
    private final CategoriaNegocio CATEGORIA_NEGOCIO;
    
    private DefaultTableModel modeloTabla;
    private String accion;
    private int idSeleccionado; 

    public FrmGestionProductos() {
        initComponents();
        
        this.setSize(900, 600);
        this.setMinimumSize(new java.awt.Dimension(800, 500));
        this.PRODUCTO_NEGOCIO = new ProductoNegocio();
        this.CATEGORIA_NEGOCIO = new CategoriaNegocio();
        
        this.definirCabecerasTabla();
        this.listarProductos();
        
        this.cargarComboTipoProducto();
        this.cargarComboCategorias();        
        this.gestionarEstadoFormulario("INICIO");        
        this.configurarSpinners();
        
        this.gestionarCamposPolimorficos();
    }
    private void definirCabecerasTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("SKU");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Categoría"); 
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Stock");
        modeloTabla.addColumn("Unidad");
        
        this.tablaProductos.setModel(modeloTabla);
        
        this.tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(200);
        this.tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(100);
        this.tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(80);
    }
    
    private void cargarComboTipoProducto() {
        cmbTipoProducto.removeAllItems();
        cmbTipoProducto.addItem("UNITARIO");
        cmbTipoProducto.addItem("GRANEL");
        cmbTipoProducto.addItem("SERVICIO");
        cmbTipoProducto.addActionListener(e -> gestionarCamposPolimorficos());
    }

    private void cargarComboCategorias() {
        DefaultComboBoxModel<Object> modeloCombo = (DefaultComboBoxModel<Object>) cmbCategoria.getModel();
        modeloCombo.removeAllElements();
        
        List<Categoria> categorias = this.CATEGORIA_NEGOCIO.listar();
        
        for (Categoria c : categorias) {
            modeloCombo.addElement(c); 
        }
        
        cmbCategoria.setModel(modeloCombo);
        cmbCategoria.setSelectedIndex(-1);
    }
    
    private void configurarSpinners() {
        SpinnerNumberModel modelPrecio = new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.50);
        spinPrecio.setModel(modelPrecio);
        SpinnerNumberModel modelStock = new SpinnerNumberModel(0.0, 0.0, 10000.0, 1.0);
        spinStock.setModel(modelStock);
    }


    private void gestionarCamposPolimorficos() {
        String tipo = (String) cmbTipoProducto.getSelectedItem();
        if (tipo == null) return; 

        switch (tipo) {
            case "UNITARIO":
                lblPrecio.setText("Precio Unitario:");
                lblStock.setText("Stock Actual:");
                lblUnidadMedida.setText("Unidad:");
                spinPrecio.setEnabled(true);
                spinStock.setEnabled(true);
                txtUnidadMedida.setEnabled(false);
                ((SpinnerNumberModel)spinStock.getModel()).setStepSize(1.0);
                txtUnidadMedida.setText("Unidad");
                break;
            case "GRANEL":
                lblPrecio.setText("Precio por Medida:");
                lblStock.setText("Stock Medido:");
                lblUnidadMedida.setText("Unidad Medida:");
                spinPrecio.setEnabled(true);
                spinStock.setEnabled(true);
                txtUnidadMedida.setEnabled(true); 
                ((SpinnerNumberModel)spinStock.getModel()).setStepSize(0.5);
                txtUnidadMedida.setText("");
                break;
            case "SERVICIO":
                lblPrecio.setText("Tarifa Servicio:");
                lblStock.setText("Stock:");
                lblUnidadMedida.setText("Unidad:");
                spinPrecio.setEnabled(true);
                spinStock.setEnabled(false); 
                txtUnidadMedida.setEnabled(false);
                spinStock.setValue(0.0);
                txtUnidadMedida.setText("N/A");
                break;
        }
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
                limpiarYBloquearCampos(true);
                break;
            case "NUEVO":
            case "EDITAR":
                btnNuevo.setEnabled(false);
                btnGuardar.setEnabled(true);
                btnEditar.setEnabled(false);
                btnDesactivar.setEnabled(false);
                btnCancelar.setEnabled(true);
                limpiarYBloquearCampos(false);
                gestionarCamposPolimorficos();
                break;
            case "SELECCIONADO":
                btnNuevo.setEnabled(true);
                btnGuardar.setEnabled(false);
                btnEditar.setEnabled(true);
                btnDesactivar.setEnabled(true);
                btnCancelar.setEnabled(false);
                limpiarYBloquearCampos(true);
                break;
        }
    }

    private void limpiarYBloquearCampos(boolean bloquear) {
        txtNombre.setText("");
        txtSku.setText("");
        txtDescripcion.setText("");
        spinPrecio.setValue(0.0);
        spinStock.setValue(0.0);
        txtUnidadMedida.setText("");
        cmbTipoProducto.setSelectedIndex(0);
        cmbCategoria.setSelectedIndex(-1);
        
        txtNombre.setEnabled(!bloquear);
        txtSku.setEnabled(!bloquear);
        txtDescripcion.setEnabled(!bloquear);
        cmbTipoProducto.setEnabled(!bloquear);
        cmbCategoria.setEnabled(!bloquear);
    }

    private void listarProductos() {
        try {
            modeloTabla.setRowCount(0);
            List<ItemVendible> lista = this.PRODUCTO_NEGOCIO.listar();

            if (lista.isEmpty()) return;

            for (ItemVendible item : lista) {
                Object[] fila = new Object[8]; 
                fila[0] = item.getProductoId();
                fila[1] = item.getSku();
                fila[2] = item.getNombre();
                
                fila[3] = (item.getCategoria() != null) ? item.getCategoria().getNombre() : "S/C";
                
                fila[4] = item.getClass().getSimpleName();
                fila[5] = item.calcularSubtotal(1);
                fila[6] = (item instanceof Servicio) ? "Infinito" : item.obtenerStock();
                fila[7] = item.obtenerUnidadParaGUI();
                
                modeloTabla.addRow(fila);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al listar productos: " + ex.getMessage(), 
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFormulario = new javax.swing.JPanel();
        lblTipoProducto = new javax.swing.JLabel();
        cmbTipoProducto = new javax.swing.JComboBox<>();
        lblCategoria = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblSku = new javax.swing.JLabel();
        txtSku = new javax.swing.JTextField();
        lblPrecio = new javax.swing.JLabel();
        spinPrecio = new javax.swing.JSpinner();
        lblStock = new javax.swing.JLabel();
        spinStock = new javax.swing.JSpinner();
        lblUnidadMedida = new javax.swing.JLabel();
        txtUnidadMedida = new javax.swing.JTextField();
        lblDescripcion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        panelAcciones = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();

        panelFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Producto"));

        lblTipoProducto.setText("Tipo Producto:");

        cmbTipoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoProductoActionPerformed(evt);
            }
        });

        lblCategoria.setText("Categoría:");

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        lblNombre.setText("Nombre:");

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        lblSku.setText("SKU:");

        lblPrecio.setText("Precio:");

        spinPrecio.setModel(new javax.swing.SpinnerNumberModel(0, null, null, 0));

        lblStock.setText("Stock:");

        lblUnidadMedida.setText("Unidad Medida:");

        txtUnidadMedida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnidadMedidaActionPerformed(evt);
            }
        });

        lblDescripcion.setText("Descripción:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        jScrollPane1.setViewportView(jScrollPane2);

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(lblUnidadMedida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUnidadMedida))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(lblTipoProducto)
                        .addGap(18, 18, 18)
                        .addComponent(cmbTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCategoria)
                            .addComponent(lblPrecio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinPrecio)
                            .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSku)
                            .addComponent(lblStock))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinStock, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(txtSku))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(lblDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipoProducto)
                    .addComponent(cmbTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategoria)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSku)
                    .addComponent(txtSku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrecio)
                    .addComponent(spinPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStock)
                    .addComponent(spinStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblUnidadMedida)
                        .addComponent(txtUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDescripcion))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAcciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnDesactivar.setText("Desactivar");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

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
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnEditar)
                .addGap(18, 18, 18)
                .addComponent(btnDesactivar)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollTabla.setViewportView(tablaProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(scrollTabla)
            .addComponent(panelAcciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtUnidadMedidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnidadMedidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnidadMedidaActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.accion = "guardar"; 
        btnGuardar.setText("Guardar");
        this.gestionarEstadoFormulario("NUEVO");
        txtNombre.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if (txtSku.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo SKU es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtSku.requestFocus();
            return;
        }
        if (cmbCategoria.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una Categoría.", "Validación", JOptionPane.WARNING_MESSAGE);
            cmbCategoria.requestFocus();
            return;
        }
        
        ItemVendible item;
        String tipo = (String) cmbTipoProducto.getSelectedItem();
        
        switch (tipo) {
            case "UNITARIO":
                ProductoUnitario pu = new ProductoUnitario();
                pu.setPrecioUnitario(((Number)spinPrecio.getValue()).doubleValue());
                pu.setStockActual(((Number)spinStock.getValue()).intValue());
                item = pu;
                break;
            case "GRANEL":
                ProductoAGranel pg = new ProductoAGranel();
                pg.setPrecioPorMedida(((Number)spinPrecio.getValue()).doubleValue());
                pg.setStockMedido(((Number)spinStock.getValue()).doubleValue());
                pg.setUnidadMedida(txtUnidadMedida.getText().trim());
                item = pg;
                break;
            case "SERVICIO":
                Servicio s = new Servicio();
                s.setTarifaServicio(((Number)spinPrecio.getValue()).doubleValue());
                item = s;
                break;
            default:
                JOptionPane.showMessageDialog(this, "Tipo de producto no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }
        
        item.setNombre(txtNombre.getText().trim());
        item.setSku(txtSku.getText().trim());
        item.setDescripcion(txtDescripcion.getText());
        item.setActivo(true);
        item.setCategoria((Categoria)cmbCategoria.getSelectedItem());
        
        
        String respuesta = null;
        
        if (this.accion.equals("guardar")) {
            respuesta = this.PRODUCTO_NEGOCIO.insertar(item);
        } else { 
            item.setProductoId(this.idSeleccionado);
            respuesta = this.PRODUCTO_NEGOCIO.actualizar(item);
        }
        
        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Producto guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.listarProductos();
            this.gestionarEstadoFormulario("INICIO");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.accion = "editar";
        btnGuardar.setText("Actualizar");
        this.gestionarEstadoFormulario("EDITAR");
        txtNombre.requestFocus();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        if (this.idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Seguro que desea desactivar este producto?\nID: " + this.idSeleccionado + " - " + txtNombre.getText(), 
                "Confirmar Desactivación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return; 
        }
        
        String respuesta = this.PRODUCTO_NEGOCIO.desactivar(this.idSeleccionado);
        
        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Producto desactivado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.listarProductos();
            this.gestionarEstadoFormulario("INICIO");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.gestionarEstadoFormulario("INICIO");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cmbTipoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipoProductoActionPerformed

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoriaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<Object> cmbCategoria;
    private javax.swing.JComboBox<String> cmbTipoProducto;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblSku;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTipoProducto;
    private javax.swing.JLabel lblUnidadMedida;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JSpinner spinPrecio;
    private javax.swing.JSpinner spinStock;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSku;
    private javax.swing.JTextField txtUnidadMedida;
    // End of variables declaration//GEN-END:variables
}
