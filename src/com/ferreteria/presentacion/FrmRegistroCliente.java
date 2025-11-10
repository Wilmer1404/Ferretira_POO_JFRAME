package com.ferreteria.presentacion;

import com.ferreteria.entidades.Cliente;
import com.ferreteria.negocio.ClienteNegocio;
import javax.swing.JOptionPane;

public class FrmRegistroCliente extends javax.swing.JFrame {

    private final ClienteNegocio CLIENTE_NEGOCIO;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmRegistroCliente.class.getName());

    public FrmRegistroCliente() {
        initComponents();
        // 1. Inicializar la capa de negocio
        this.CLIENTE_NEGOCIO = new ClienteNegocio();

        // 2. Centrar la ventana
        this.setLocationRelativeTo(null);

        // 3. Poner título
        this.setTitle("Registro de Nuevo Cliente");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblDni = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblApellidos = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblPassword2 = new javax.swing.JLabel();
        txtPasswordConfirmar = new javax.swing.JPasswordField();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitulo.setText("Registro de Nuevo Cliente");

        lblDni.setText("DNI:");

        lblNombre.setText("Nombre:");

        lblApellidos.setText("Apellidos:");

        lblEmail.setText("Email:");

        lblTelefono.setText("Teléfono:");

        lblDireccion.setText("Dirección:");

        lblPassword.setText("Contraseña:");

        txtPassword.setText("jPasswordField1");

        lblPassword2.setText("Confirmar Contraseña:");

        txtPasswordConfirmar.setText("jPasswordField1");

        btnRegistrar.setText("Registrarse");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar y Volver");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCancelar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblPassword2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnRegistrar, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtPasswordConfirmar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(56, 56, 56)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(1, 1, 1)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(lblApellidos)
                                                        .addComponent(lblNombre, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblDni, javax.swing.GroupLayout.Alignment.LEADING)))
                                                .addComponent(lblEmail))
                                            .addGap(21, 21, 21)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE))
                                                .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtApellidos, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(txtNombre)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblTelefono)
                                                .addComponent(lblPassword)
                                                .addComponent(lblDireccion))
                                            .addGap(9, 9, 9)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtDireccion)
                                                .addComponent(txtPassword)
                                                .addComponent(txtTelefono)))))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(106, 106, 106)
                            .addComponent(lblTitulo))))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDni)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidos)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDireccion)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword2)
                    .addComponent(txtPasswordConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // 1. Obtener datos de los campos
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String pass1 = new String(txtPassword.getPassword());
        String pass2 = new String(txtPasswordConfirmar.getPassword());

        // 2. Validaciones de la Vista (campos vacíos)
        if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || pass1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos DNI, Nombre, Apellidos, Email y Contraseña son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validaciones de la Vista (contraseñas coinciden)
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        // 4. Crear el objeto Cliente
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setApellidos(apellidos);
        cliente.setEmail(email);
        cliente.setTelefono(telefono);
        cliente.setDireccionPrincipal(direccion); // Asegúrate que tu entidad Cliente tenga este método
        cliente.setPasswordHash(pass1); // Pasamos la contraseña (la capa de Negocio debería "hashearla")

        // 5. Enviar a la capa de Negocio para registrar
        String respuesta = this.CLIENTE_NEGOCIO.registrar(cliente);

        // 6. Interpretar la respuesta
        if (respuesta == null) {
            // Éxito
            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ahora puede iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Cerrar esta ventana y volver al Login
            this.dispose();
            FrmLogin loginForm = new FrmLogin();
            loginForm.setVisible(true);
        } else {
            // Error (mostramos el mensaje de la capa de negocio)
            JOptionPane.showMessageDialog(this, respuesta, "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // Cierra esta ventana de registro
        this.dispose();
        
        // Crea y muestra la ventana de Login
        FrmLogin loginForm = new FrmLogin();
        loginForm.setVisible(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmRegistroCliente().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPassword2;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPasswordConfirmar;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
