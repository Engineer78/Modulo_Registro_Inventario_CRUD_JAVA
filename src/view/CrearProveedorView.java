/*
 * Proyecto Formativo SENA: Hardware Store Inventory FFIG
 * Módulo: Registro de Inventario
 * Desarrollo de CRUD en JAVA usando MVC y DTO
 * Versión 1.0
 * Centro de Comercio y Turismo Armenía, Regional Quindío
 * Aprendices: 
 * David Ricargo Graffe Rodríguez, Ficha: 2879724
 * Joaquín Humberto Jiménez Rosas, Ficha: 2879723
 * Juan David Gallego López, Ficha: 2879723
 * @Todos los derechos reservados 2024 - 2025
 */

package view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import util.ConexionBD;
import java.sql.PreparedStatement;
import java.sql.SQLException; // También necesitas importar SQLException
import dao.ProveedorDAO; // Importa la interfaz
import dao.ProveedorDAOImpl; // Importa la implementación
import model.Proveedor; // Importa la clase modelo

/**
 *
 * @author JJime
 */
public class CrearProveedorView extends javax.swing.JFrame {

    private ConexionBD conexionBD;
    private JTextField[] camposTexto = new JTextField[4];

    // Constantes para textos predictivos
    private static final String TEXTO_PREDICTIVO_NOMBRE_PROVEEDOR = "Nombre del Proveedor";
    private static final String TEXTO_PREDICTIVO_NIT_PROVEEDOR = "Nit del Proveedor";
    private static final String TEXTO_PREDICTIVO_DIRECCION_PROVEEDOR = "Direccion del Proveedor";
    private static final String TEXTO_PREDICTIVO_TELEFONO_PROVEEDOR = "Telefono del Proveedor";

    /**
     * Creates new form CrearProveedorView
     */
    public CrearProveedorView() {
        initComponents();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        conexionBD = new ConexionBD(); // Instancia de la conexión a la base de datos

        // Configuración inicial del formulario
        restaurarTextosPredictivos();

        // Inicializa los campos de texto en el arreglo
        camposTexto[0] = txtNombreProveedor;
        camposTexto[1] = txtNitProveedor;
        camposTexto[2] = txtDireccionProveedor;
        camposTexto[3] = txtTelefonoProveedor;
        

        // Se aplica el FocusListener a cada campo de texto usando un bucle
        for (int i = 0; i < camposTexto.length; i++) {
            if (camposTexto[i] != null) {
                agregarFocusListener(camposTexto[i], obtenerTextoPredictivo(i));
            }
        }
        // Restaurar textos predictivos inicialmente
        restaurarTextosPredictivos();

        // Solicitar el foco para el panel principal
        getContentPane().requestFocusInWindow(); // Esto es importante
    }

    /**
     * Agrega un FocusListener a los campos de texto para simular textos
     * predictivos.
     */
    private void agregarFocusListener(JTextField campoTexto, String textoPredictivo) {
        campoTexto.setText(textoPredictivo);
        campoTexto.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoTexto.getText().equals(textoPredictivo)) {
                    campoTexto.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoTexto.getText().isEmpty()) {
                    campoTexto.setText(textoPredictivo);
                }
            }
        });
    }

    /**
     * Devuelve el texto predictivo según el índice del campo.
     */
    private String obtenerTextoPredictivo(int index) {
        switch (index) {
            case 0:
                return TEXTO_PREDICTIVO_NOMBRE_PROVEEDOR;
            case 1:
                return TEXTO_PREDICTIVO_NIT_PROVEEDOR;
            case 2:
                return TEXTO_PREDICTIVO_DIRECCION_PROVEEDOR;
            case 3:
                return TEXTO_PREDICTIVO_TELEFONO_PROVEEDOR;
            default:
                return "";
        }
    }

    /**
     * método para restaurar los textos predictivos
     */
    private void restaurarTextosPredictivos() {
        for (int i = 0; i < camposTexto.length; i++) {
            if (camposTexto[i] != null && camposTexto[i].getText().isEmpty()) {
                camposTexto[i].setText(obtenerTextoPredictivo(i));
            }
        }
    }

    // Getters para los campos
    public String getNombreProveedor() {
        return txtNombreProveedor.getText().trim();
    }

    public String getNitProveedor() {
        return txtNitProveedor.getText().trim();
    }

    public String getDireccionProveedor() {
        return txtDireccionProveedor.getText().trim();
    }

    public String getTelefonoProveedor() {
        return txtTelefonoProveedor.getText().trim();
    }

    // Getters para los botones para asignar sus listeners en el controlador
    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }

    /**
     * Método para validar los campos de texto
     */
    private boolean validarCampos() {
        String nombre = txtNombreProveedor.getText().trim();
        String nit = txtNitProveedor.getText().trim();
        String direccion = txtDireccionProveedor.getText().trim();
        String telefono = txtTelefonoProveedor.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del proveedor es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (nit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El NIT del proveedor es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección del proveedor es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El teléfono del proveedor es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validaciones de formato (puedes usar expresiones regulares aquí)
        if (!nombre.matches("^[\\p{L}0-9\\s\\p{Punct}]+$")) {
            JOptionPane.showMessageDialog(this, "El nombre del proveedor contiene caracteres inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!nit.matches("^[0-9-]+$")) {
            JOptionPane.showMessageDialog(this, "El NIT del proveedor contiene caracteres inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!direccion.matches("^[\\p{L}0-9\\s#°-]+$")) {
            JOptionPane.showMessageDialog(this, "La dirección del proveedor contiene caracteres inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!telefono.matches("^[0-9+()-]+$")) {
            JOptionPane.showMessageDialog(this, "El teléfono del proveedor contiene caracteres inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void guardarProveedor() {
        if (validarCampos()) {
            String nombre = txtNombreProveedor.getText().trim();
            String nit = txtNitProveedor.getText().trim();
            String direccion = txtDireccionProveedor.getText().trim();
            String telefono = txtTelefonoProveedor.getText().trim();

            // Convertir nombre a formato de oración
            nombre = convertirAFormatoOracion(nombre);

            // Enviar datos a la base de datos
            try {
                conexionBD.getConnection();
                Proveedor proveedor = new Proveedor(nombre, nit, direccion, telefono);
                ProveedorDAO proveedorDAO = new ProveedorDAOImpl(conexionBD.getConnection());
                proveedorDAO.crearProveedor(proveedor);
                conexionBD.closeConnection();
                JOptionPane.showMessageDialog(this, "Proveedor guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public void limpiarCampos() {
        txtNombreProveedor.setText("");
        txtNitProveedor.setText("");
        txtDireccionProveedor.setText("");
        txtTelefonoProveedor.setText("");
        restaurarTextosPredictivos(); //Restaura los textos predictivos
    }

    /**
     * Método para convertir el nombre del provedor tipo oración y dejarlo en
     * minúscula
     *
     * @param texto
     * @return
     */
    private String convertirAFormatoOracion(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        StringBuilder resultado = new StringBuilder();
        String[] palabras = texto.toLowerCase().split("\\s+");
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0))).append(palabra.substring(1)).append(" ");
            }
        }
        return resultado.toString().trim();
    }

    private void regresarVentanaPrincipal() {
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombreProveedor = new javax.swing.JTextField();
        txtNitProveedor = new javax.swing.JTextField();
        txtDireccionProveedor = new javax.swing.JTextField();
        txtTelefonoProveedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Crear Nuevo Proveedor");

        txtNombreProveedor.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtNombreProveedor.setText("Nombre Proveedor (Obligatorio)");

        txtNitProveedor.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtNitProveedor.setText("NIT Proveedor (Obligatorio)");

        txtDireccionProveedor.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtDireccionProveedor.setText("Dirección Proveedor (Obligatorio)");

        txtTelefonoProveedor.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtTelefonoProveedor.setText("Teléfono Proveedor (Obligatorio)");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel2.setText("Nombre Proveedor");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel3.setText("Nit Proveedor");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel4.setText("Dirección Proveedor");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel5.setText("Teléfono Proveedor");

        btnSalir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNombreProveedor)
                    .addComponent(txtNitProveedor)
                    .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(txtTelefonoProveedor)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNitProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimpiar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalir)))
                .addContainerGap(89, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarProveedor();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        regresarVentanaPrincipal();
    }//GEN-LAST:event_btnSalirActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CrearProveedorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrearProveedorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrearProveedorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrearProveedorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrearProveedorView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDireccionProveedor;
    private javax.swing.JTextField txtNitProveedor;
    private javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtTelefonoProveedor;
    // End of variables declaration//GEN-END:variables
}
