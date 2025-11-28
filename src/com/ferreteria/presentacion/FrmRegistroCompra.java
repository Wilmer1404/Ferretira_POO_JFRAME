package com.ferreteria.presentacion;

import com.ferreteria.entidades.Compra;
import com.ferreteria.entidades.DetalleCompra;
import com.ferreteria.entidades.Empleado;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.Proveedor;
import com.ferreteria.negocio.CompraNegocio;
import com.ferreteria.negocio.ProductoNegocio;
import com.ferreteria.negocio.ProveedorNegocio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class FrmRegistroCompra extends javax.swing.JInternalFrame {

    private final Empleado empleadoLogueado;
    private final ProveedorNegocio PROVEEDOR_NEGOCIO;
    private final ProductoNegocio PRODUCTO_NEGOCIO;
    private final CompraNegocio COMPRA_NEGOCIO;

    private DefaultTableModel modeloTablaProductos;
    private DefaultTableModel modeloTablaCompra;
    private List<ItemVendible> listaProductosCache;
    private List<DetalleCompra> carritoCompra;
    private double totalCompra = 0.0;
    private ItemVendible itemSeleccionado = null;

    public FrmRegistroCompra(Empleado empleadoLogueado) {
        initComponents();

        this.empleadoLogueado = empleadoLogueado;

        this.PROVEEDOR_NEGOCIO = new ProveedorNegocio();
        this.PRODUCTO_NEGOCIO = new ProductoNegocio();
        this.COMPRA_NEGOCIO = new CompraNegocio();

        this.listaProductosCache = new ArrayList<>();
        this.carritoCompra = new ArrayList<>();

        this.setTitle("Registrar Nueva Compra - Usuario: " + this.empleadoLogueado.getEmail());
        this.setSize(950, 650);

        this.definirCabecerasTablas();
        this.configurarSpinners();

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
        this.cargarProveedores();
        this.listarProductos(txtBuscarProducto.getText().trim());
    }

    private void cargarProveedores() {
        try {
            Object seleccionActual = cmbProveedor.getSelectedItem();

            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            List<Proveedor> proveedores = this.PROVEEDOR_NEGOCIO.listar();

            Proveedor prompt = new Proveedor();
            prompt.setProveedorId(0);
            prompt.setRazonSocial("Seleccione un Proveedor");
            modeloCombo.addElement(prompt);
            for (Proveedor p : proveedores) {
                modeloCombo.addElement(p);
            }
            this.cmbProveedor.setModel(modeloCombo);

            if (seleccionActual != null && (seleccionActual instanceof Proveedor)) {
                Proveedor provActual = (Proveedor) seleccionActual;
                if (provActual.getProveedorId() != 0) { 
                    cmbProveedor.setSelectedItem(seleccionActual);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void definirCabecerasTablas() {
        modeloTablaProductos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        modeloTablaProductos.addColumn("ID");
        modeloTablaProductos.addColumn("Nombre");
        modeloTablaProductos.addColumn("Stock Actual");
        modeloTablaProductos.addColumn("Unidad");
        this.tablaProductos.setModel(modeloTablaProductos);
        this.tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(200);

        modeloTablaCompra = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        modeloTablaCompra.addColumn("ID");
        modeloTablaCompra.addColumn("Producto");
        modeloTablaCompra.addColumn("Cantidad");
        modeloTablaCompra.addColumn("Costo Unit. (S/)");
        modeloTablaCompra.addColumn("Subtotal (S/)");
        this.tablaCompra.setModel(modeloTablaCompra);
    }

    private void configurarSpinners() {
        SpinnerNumberModel modelCantidad = new SpinnerNumberModel(1.0, 0.1, 1000.0, 0.5);
        this.spinCantidad.setModel(modelCantidad);

        SpinnerNumberModel modelCosto = new SpinnerNumberModel(0.0, 0.0, 10000.0, 1.0);
        this.spinCosto.setModel(modelCosto);
    }

    private void listarProductos(String terminoBusqueda) {
        try {
            modeloTablaProductos.setRowCount(0);
            this.itemSeleccionado = null;

            this.listaProductosCache = this.PRODUCTO_NEGOCIO.buscar(terminoBusqueda);

            if (this.listaProductosCache.isEmpty() && !terminoBusqueda.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron productos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }

            for (ItemVendible item : this.listaProductosCache) {
                Object[] fila = new Object[4];
                fila[0] = item.getProductoId();
                fila[1] = item.getNombre();
                fila[2] = item.obtenerStock();
                fila[3] = item.obtenerUnidadParaGUI();
                modeloTablaProductos.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaCompra() {
        modeloTablaCompra.setRowCount(0);
        this.totalCompra = 0.0;

        for (DetalleCompra det : this.carritoCompra) {
            Object[] fila = new Object[5];
            fila[0] = det.getItem().getProductoId();
            fila[1] = det.getItem().getNombre();
            fila[2] = det.getCantidad();
            fila[3] = String.format("%.2f", det.getPrecioCompra());
            fila[4] = String.format("%.2f", det.getSubtotal());

            modeloTablaCompra.addRow(fila);
            this.totalCompra += det.getSubtotal();
        }

        lblTotalCompra.setText(String.format("Total Costo: S/ %.2f", this.totalCompra));
    }

    private void limpiarFormularioCompleto() {
        this.totalCompra = 0.0;
        this.carritoCompra.clear();
        this.itemSeleccionado = null;
        actualizarTablaCompra();

        this.cmbProveedor.setSelectedIndex(0);
        this.txtObservaciones.setText("");
        this.txtBuscarProducto.setText("");
        this.spinCantidad.setValue(1.0);
        this.spinCosto.setValue(0.0);

        this.listarProductos("");
    }

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        this.listarProductos(txtBuscarProducto.getText().trim());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        splitPanePrincipal = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        panelBusqueda = new javax.swing.JPanel();
        txtBuscarProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        scrollProductos = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        panelAgregar = new javax.swing.JPanel();
        lblCantidad = new javax.swing.JLabel();
        spinCantidad = new javax.swing.JSpinner();
        lblCostoUnitario = new javax.swing.JLabel();
        spinCosto = new javax.swing.JSpinner();
        btnAgregar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        scrollCompra = new javax.swing.JScrollPane();
        tablaCompra = new javax.swing.JTable();
        lblTotalCompra = new javax.swing.JLabel();
        btnQuitarItem = new javax.swing.JButton();
        panelCabecera = new javax.swing.JPanel();
        lblProveedor = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        lblObservaciones = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        panelAcciones = new javax.swing.JPanel();
        btnRegistrarCompra = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        splitPanePrincipal.setDividerLocation(200);

        jPanel1.setLayout(new java.awt.BorderLayout());

        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/buscar.png"))); // NOI18N
        btnBuscarProducto.setText("Buscar");

        javax.swing.GroupLayout panelBusquedaLayout = new javax.swing.GroupLayout(panelBusqueda);
        panelBusqueda.setLayout(panelBusquedaLayout);
        panelBusquedaLayout.setHorizontalGroup(
            panelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBusquedaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtBuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarProducto)
                .addContainerGap())
        );
        panelBusquedaLayout.setVerticalGroup(
            panelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBusquedaLayout.createSequentialGroup()
                .addGroup(panelBusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProducto))
                .addGap(0, 18, Short.MAX_VALUE))
        );

        jPanel1.add(panelBusqueda, java.awt.BorderLayout.NORTH);

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
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaProductosMousePressed(evt);
            }
        });
        scrollProductos.setViewportView(tablaProductos);

        jPanel1.add(scrollProductos, java.awt.BorderLayout.CENTER);

        lblCantidad.setText("Cantidad:");

        lblCostoUnitario.setText("Costo Unitario:");

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/agregar.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAgregarLayout = new javax.swing.GroupLayout(panelAgregar);
        panelAgregar.setLayout(panelAgregarLayout);
        panelAgregarLayout.setHorizontalGroup(
            panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAgregarLayout.createSequentialGroup()
                        .addComponent(lblCantidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAgregarLayout.createSequentialGroup()
                        .addComponent(lblCostoUnitario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAgregar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        panelAgregarLayout.setVerticalGroup(
            panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgregarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCantidad)
                    .addComponent(spinCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCostoUnitario)
                    .addComponent(spinCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregar)
                .addContainerGap())
        );

        jPanel1.add(panelAgregar, java.awt.BorderLayout.SOUTH);

        splitPanePrincipal.setLeftComponent(jPanel1);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Productos a Registrar en Inventario");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 93, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.NORTH);

        tablaCompra.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollCompra.setViewportView(tablaCompra);

        lblTotalCompra.setText("Total Costo: S/ 0.00");

        btnQuitarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/eliminar.png"))); // NOI18N
        btnQuitarItem.setText("QuitarItem");
        btnQuitarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnQuitarItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblTotalCompra))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(scrollCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalCompra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnQuitarItem)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        splitPanePrincipal.setRightComponent(jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPanePrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPanePrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        panelCabecera.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la Compra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        lblProveedor.setText("Proveedor:");

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProveedorActionPerformed(evt);
            }
        });

        lblObservaciones.setText("Observaciones:");

        javax.swing.GroupLayout panelCabeceraLayout = new javax.swing.GroupLayout(panelCabecera);
        panelCabecera.setLayout(panelCabeceraLayout);
        panelCabeceraLayout.setHorizontalGroup(
            panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblObservaciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelCabeceraLayout.setVerticalGroup(
            panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProveedor)
                    .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblObservaciones)
                    .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelCabecera, java.awt.BorderLayout.NORTH);

        btnRegistrarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/guardar.png"))); // NOI18N
        btnRegistrarCompra.setText("Confirmar y Guardar Compra");
        btnRegistrarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarCompraActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ferreteria/imagenes/cancelar.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(btnRegistrarCompra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(75, Short.MAX_VALUE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrarCompra)
                    .addComponent(jButton2))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        getContentPane().add(panelAcciones, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (this.itemSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la lista izquierda.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double cantidad = ((Number) spinCantidad.getValue()).doubleValue();
        double costo = ((Number) spinCosto.getValue()).doubleValue();

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (costo <= 0) {
            JOptionPane.showMessageDialog(this, "Debe especificar el Precio de Compra (Costo).", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetalleCompra detalle = new DetalleCompra();
        detalle.setItem(this.itemSeleccionado);
        detalle.setCantidad(cantidad);
        detalle.setPrecioCompra(costo);
        detalle.setSubtotal(cantidad * costo);

        this.carritoCompra.add(detalle);
        this.actualizarTablaCompra();

        this.itemSeleccionado = null;
        this.tablaProductos.clearSelection();
        this.spinCosto.setValue(0.0);
        this.spinCantidad.setValue(1.0);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnQuitarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarItemActionPerformed
        int fila = this.tablaCompra.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un item del carrito de compra.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.carritoCompra.remove(fila);
        this.actualizarTablaCompra();
    }//GEN-LAST:event_btnQuitarItemActionPerformed

    private void btnRegistrarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarCompraActionPerformed
        Object itemProveedor = cmbProveedor.getSelectedItem();
        if (itemProveedor == null || !(itemProveedor instanceof Proveedor) || ((Proveedor) itemProveedor).getProveedorId() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Proveedor.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.carritoCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito de compra está vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Compra nuevaCompra = new Compra();
        nuevaCompra.setProveedor((Proveedor) cmbProveedor.getSelectedItem());
        nuevaCompra.setEmpleado(this.empleadoLogueado);
        nuevaCompra.setDetalles(this.carritoCompra);
        nuevaCompra.setTotal(this.totalCompra);
        nuevaCompra.setObservaciones(txtObservaciones.getText().trim());
        nuevaCompra.setFechaCompra(LocalDateTime.now());

        String respuesta = this.COMPRA_NEGOCIO.registrarCompra(nuevaCompra);

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "Compra registrada y stock actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormularioCompleto();
            this.listarProductos(txtBuscarProducto.getText().trim());
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la compra:\n" + respuesta, "Error de Transacción", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarCompraActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tablaProductosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductosMousePressed
        int fila = this.tablaProductos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        try {
            int idProducto = (int) this.modeloTablaProductos.getValueAt(fila, 0);
            this.itemSeleccionado = this.listaProductosCache.stream()
                    .filter(item -> item.getProductoId() == idProducto)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar producto: " + e.getMessage());
            this.itemSeleccionado = null;
        }
    }//GEN-LAST:event_tablaProductosMousePressed

    private void cmbProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProveedorActionPerformed

    }//GEN-LAST:event_cmbProveedorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnQuitarItem;
    private javax.swing.JButton btnRegistrarCompra;
    private javax.swing.JComboBox<Object> cmbProveedor;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCostoUnitario;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblProveedor;
    private javax.swing.JLabel lblTotalCompra;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelAgregar;
    private javax.swing.JPanel panelBusqueda;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JScrollPane scrollCompra;
    private javax.swing.JScrollPane scrollProductos;
    private javax.swing.JSpinner spinCantidad;
    private javax.swing.JSpinner spinCosto;
    private javax.swing.JSplitPane splitPanePrincipal;
    private javax.swing.JTable tablaCompra;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
