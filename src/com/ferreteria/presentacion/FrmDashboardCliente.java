package com.ferreteria.presentacion;

import com.ferreteria.entidades.Cliente;
import com.ferreteria.entidades.DetalleVenta;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Servicio;
import com.ferreteria.entidades.Venta;
import com.ferreteria.negocio.ProductoNegocio;
import com.ferreteria.negocio.VentaNegocio;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 * Dashboard principal para clientes de la ferretería.
 * Interfaz donde los clientes pueden navegar productos y realizar compras.
 * 
 * Funcionalidades principales:
 * - Catálogo de productos con búsqueda y filtros
 * - Carrito de compras con gestión de items
 * - Cálculo automático de totales y subtotales
 * - Procesamiento de compras con validación de stock
 * - Visualización de información del cliente logueado
 * 
 * Maneja diferentes tipos de productos:
 * - ProductoUnitario: se venden por unidades enteras
 * - ProductoAGranel: se venden por peso/medida con decimales
 * - Servicio: disponibilidad ilimitada
 */
public class FrmDashboardCliente extends javax.swing.JFrame {

    private final Cliente clienteLogueado;
    private final ProductoNegocio PRODUCTO_NEGOCIO;
    private final VentaNegocio VENTA_NEGOCIO;
    private DefaultTableModel modeloTablaProductos;
    private DefaultTableModel modeloTablaCarrito;
    private List<ItemVendible> listaProductos;
    private List<DetalleVenta> carrito;

    private double totalCarrito = 0.0;
    private int idProductoSeleccionado = -1;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDashboardCliente.class.getName());

    public FrmDashboardCliente(Cliente cliente) {
        initComponents();

        this.clienteLogueado = cliente;
        this.PRODUCTO_NEGOCIO = new ProductoNegocio();
        this.VENTA_NEGOCIO = new VentaNegocio();
        this.carrito = new ArrayList<>();
        lblBienvenida.setText("¡Bienvenido a la Tienda, " + cliente.getNombre() + "!");
        this.definirCabecerasProductos();
        this.definirCabecerasCarrito();
        SpinnerNumberModel modelSpinner = new SpinnerNumberModel(1.0, 1.0, 100.0, 1.0);
        spinCantidad.setModel(modelSpinner);
        this.listarProductos("");

        btnAgregarCarrito.setEnabled(false);
        btnComprar.setEnabled(false);

        this.setLocationRelativeTo(null);
        this.setTitle("Tienda FerreteriaApp - Sesión iniciada como: " + cliente.getEmail());
    }

    private void definirCabecerasProductos() {
        modeloTablaProductos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTablaProductos.addColumn("ID");
        modeloTablaProductos.addColumn("Nombre");
        modeloTablaProductos.addColumn("Descripción");
        modeloTablaProductos.addColumn("Precio (S/)");
        modeloTablaProductos.addColumn("Stock");
        modeloTablaProductos.addColumn("Unidad");

        this.tablaProductos.setModel(modeloTablaProductos);

        this.tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(150);
        this.tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(250);
    }

    private void definirCabecerasCarrito() {
        modeloTablaCarrito = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTablaCarrito.addColumn("ID");
        modeloTablaCarrito.addColumn("Producto");
        modeloTablaCarrito.addColumn("Cant.");
        modeloTablaCarrito.addColumn("P. Unit.");
        modeloTablaCarrito.addColumn("Subtotal");

        this.jTable1.setModel(modeloTablaCarrito);

        this.jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
        this.jTable1.getColumnModel().getColumn(2).setPreferredWidth(40);
    }

    private void listarProductos(String terminoBusqueda) {
        try {
            modeloTablaProductos.setRowCount(0);

            if (terminoBusqueda == null || terminoBusqueda.trim().isEmpty()) {
                this.listaProductos = this.PRODUCTO_NEGOCIO.listar();
            } else {
                this.listaProductos = this.PRODUCTO_NEGOCIO.buscar(terminoBusqueda);
            }

            if (this.listaProductos.isEmpty()) {
                if (terminoBusqueda != null && !terminoBusqueda.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontraron productos para '" + terminoBusqueda + "'.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                }
                return;
            }

            for (ItemVendible item : this.listaProductos) {
                Object[] fila = new Object[6];
                fila[0] = item.getProductoId();
                fila[1] = item.getNombre();
                fila[2] = item.getDescripcion();
                fila[3] = item.calcularSubtotal(1);
                fila[4] = (item instanceof Servicio) ? "Infinito" : item.obtenerStock();
                fila[5] = item.obtenerUnidadParaGUI();

                modeloTablaProductos.addRow(fila);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaCarrito() {
        modeloTablaCarrito.setRowCount(0);
        this.totalCarrito = 0.0;

        for (DetalleVenta det : this.carrito) {
            Object[] fila = new Object[5];
            fila[0] = det.getItem().getProductoId();
            fila[1] = det.getItem().getNombre();
            fila[2] = det.getCantidad();
            fila[3] = det.getPrecioHistorico();
            fila[4] = det.getSubtotal();

            modeloTablaCarrito.addRow(fila);
            this.totalCarrito += det.getSubtotal();
        }
        lblTotal.setText(String.format("Total: S/ %.2f", this.totalCarrito));

        btnComprar.setEnabled(this.totalCarrito > 0);
    }

    private void agregarAlCarrito() {
        if (this.idProductoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la lista.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemVendible itemSeleccionado = null;
        for (ItemVendible item : this.listaProductos) {
            if (item.getProductoId() == this.idProductoSeleccionado) {
                itemSeleccionado = item;
                break;
            }
        }

        if (itemSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Error al encontrar el producto seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double cantidad;
        try {
            cantidad = ((Number) spinCantidad.getValue()).doubleValue();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!itemSeleccionado.validarStock(cantidad)) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Stock actual: " + itemSeleccionado.obtenerStock(), "Error de Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetalleVenta detalle = new DetalleVenta();
        detalle.setItem(itemSeleccionado);
        detalle.setCantidad(cantidad);
        detalle.setPrecioHistorico(itemSeleccionado.calcularSubtotal(1));
        detalle.setSubtotal(itemSeleccionado.calcularSubtotal(cantidad));

        this.carrito.add(detalle);

        actualizarTablaCarrito();

        tablaProductos.clearSelection();
        spinCantidad.setValue(1.0);
        btnAgregarCarrito.setEnabled(false);
        this.idProductoSeleccionado = -1;
    }

    private void procesarCompra() {
        if (this.carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Confirmar compra por un total de S/ %.2f?", this.totalCarrito),
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(this.clienteLogueado);
        nuevaVenta.setDetalles(this.carrito);
        nuevaVenta.setTotal(this.totalCarrito);
        nuevaVenta.setFechaVenta(LocalDateTime.now());
        nuevaVenta.setMetodoPago("Tarjeta");
        nuevaVenta.setReferenciaTransaccion("TXN-" + System.currentTimeMillis());

        String respuesta = this.VENTA_NEGOCIO.insertar(nuevaVenta);

        if (respuesta == null) {
            JOptionPane.showMessageDialog(this, "¡Gracias por su compra! Venta registrada con éxito.", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);

            this.carrito.clear();
            this.totalCarrito = 0.0;
            actualizarTablaCarrito();
            this.listarProductos("");
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Error de Venta", JOptionPane.ERROR_MESSAGE);

            if (respuesta.contains("Stock insuficiente") || respuesta.contains("ya no existe")) {
                this.listarProductos("");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSuperior = new javax.swing.JPanel();
        lblBienvenida = new javax.swing.JLabel();
        txtBuscarProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        splitPaneTienda = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        spinCantidad = new javax.swing.JSpinner();
        btnAgregarCarrito = new javax.swing.JButton();
        scrollProductos = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        panelCarrito = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelTotal = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        btnComprar = new javax.swing.JButton();
        tablaCarrito = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblBienvenida.setText("Bienvenido a la Tienda");

        txtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoActionPerformed(evt);
            }
        });

        btnBuscarProducto.setText("Buscar");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBienvenida)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarProducto)))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBienvenida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarProducto))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        getContentPane().add(panelSuperior, java.awt.BorderLayout.NORTH);

        splitPaneTienda.setDividerLocation(250);
        splitPaneTienda.setAutoscrolls(true);
        splitPaneTienda.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        splitPaneTienda.setName(""); // NOI18N

        jPanel1.setLayout(new java.awt.BorderLayout());

        btnAgregarCarrito.setText("Agregar al Carrito");
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAgregarCarrito)
                    .addComponent(spinCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(spinCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregarCarrito)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

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

        splitPaneTienda.setLeftComponent(jPanel1);
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        panelCarrito.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Mi Carrito");
        panelCarrito.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        lblTotal.setText("Total: S/ 0.00");

        btnComprar.setText("Comprar Ahora");
        btnComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTotalLayout = new javax.swing.GroupLayout(panelTotal);
        panelTotal.setLayout(panelTotalLayout);
        panelTotalLayout.setHorizontalGroup(
            panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotal)
                    .addComponent(btnComprar))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        panelTotalLayout.setVerticalGroup(
            panelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotalLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnComprar)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        panelCarrito.add(panelTotal, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaCarrito.setViewportView(jTable1);

        panelCarrito.add(tablaCarrito, java.awt.BorderLayout.CENTER);

        splitPaneTienda.setRightComponent(panelCarrito);

        getContentPane().add(splitPaneTienda, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        agregarAlCarrito();
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

    private void btnComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprarActionPerformed
        procesarCompra();
    }//GEN-LAST:event_btnComprarActionPerformed

    private void tablaProductosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductosMousePressed
        int fila = this.tablaProductos.getSelectedRow();
        if (fila < 0) {
            return;
        }

        try {
            this.idProductoSeleccionado = (int) this.modeloTablaProductos.getValueAt(fila, 0);

            btnAgregarCarrito.setEnabled(true);

            Object stockObj = modeloTablaProductos.getValueAt(fila, 4);

            if (stockObj instanceof Number) {
                double stock = ((Number) stockObj).doubleValue();

                if (stock <= 0) {
                    btnAgregarCarrito.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "No hay stock de este producto.", "Stock Agotado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SpinnerNumberModel model = (SpinnerNumberModel) spinCantidad.getModel();
                model.setMaximum(stock);

                if (model.getNumber().doubleValue() > stock) {
                    model.setValue(stock);
                }

            } else {
                SpinnerNumberModel model = (SpinnerNumberModel) spinCantidad.getModel();
                model.setMaximum(100.0); // Límite arbitrario para servicios
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tablaProductosMousePressed

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed
        btnBuscarProducto.doClick();
    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        String termino = txtBuscarProducto.getText().trim();
        this.listarProductos(termino);

        tablaProductos.clearSelection();
        spinCantidad.setValue(1.0);
        btnAgregarCarrito.setEnabled(false);
        this.idProductoSeleccionado = -1;
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    /**
     * @param args the command line arguments
     */
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            logger.log(java.util.logging.Level.WARNING, "FrmDashboardCliente no se puede ejecutar directamente, se debe llamar desde FrmLogin.");
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnComprar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel panelCarrito;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JPanel panelTotal;
    private javax.swing.JScrollPane scrollProductos;
    private javax.swing.JSpinner spinCantidad;
    private javax.swing.JSplitPane splitPaneTienda;
    private javax.swing.JScrollPane tablaCarrito;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscarProducto;
    // End of variables declaration//GEN-END:variables
}
