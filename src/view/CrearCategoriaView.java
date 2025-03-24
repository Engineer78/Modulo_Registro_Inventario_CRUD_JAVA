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

/**
 *
 * @author JJime
 */
public class CrearCategoriaView extends javax.swing.JFrame {

    private ConexionBD conexionBD;
    private JTextField[] campoTexto = new JTextField[1];

    // Constantes para textos predictivos
    private static final String TEXTO_PREDICTIVO_NOMBRE_CATEGORIA = "Nombre de la Categoría";

    /**
     * Creates new form CrearCategoriaView
     */
    public CrearCategoriaView() {
        initComponents();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        conexionBD = new ConexionBD(); // Instancia de la conexión a la base de datos

        // Configuración inicial del formulario
        restaurarTextoPredictivo();

        // Inicializa los campos de texto en el arreglo
        campoTexto[0] = txtNombreCategoria;

        // Se aplica el FocusListener a cada campo de texto usando un bucle
        for (int i = 0; i < campoTexto.length; i++) {
            if (campoTexto[i] != null) {
                agregarFocusListener(campoTexto[i], obtenerTextoPredictivo(i));
            }
        }
        // Restaurar textos predictivos inicialmente
        restaurarTextoPredictivo();

        // Solicitar el foco para el panel principal
        getContentPane().requestFocusInWindow(); // Esto es importante
    }

    /**
     * Agrega un FocusListener a los campos de texto para simular textos
     * predictivos.
     */
    private void agregarFocusListener(JTextField campoTexto, String textoPredictivo) {
        campoTexto.setText(textoPredictivo); // Texto inicial
        campoTexto.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoTexto.getText().equals(textoPredictivo)) {
                    campoTexto.setText(""); // Limpia el campo al ganar foco
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoTexto.getText().isEmpty()) {
                    campoTexto.setText(textoPredictivo); // Restaura el texto si está vacío
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
                return TEXTO_PREDICTIVO_NOMBRE_CATEGORIA;
            default:
                return "";
        }
    }

    /**
     * método para restaurar los textos predictivos
     */
    private void restaurarTextoPredictivo() {
        for (int i = 0; i < campoTexto.length; i++) {
            if (campoTexto[i] != null && campoTexto[i].getText().isEmpty()) {
                campoTexto[i].setText(obtenerTextoPredictivo(i));
            }
        }
    }

    /**
     * Me+étodo para validar los campos de texto
     */
    private boolean validarCampos() {
        String nombre = txtNombreCategoria.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de la categoría es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validaciones de formato (puedes usar expresiones regulares aquí)
        if (!nombre.matches("^[\\p{L}0-9\\s\\p{Punct}]+$")) {
            JOptionPane.showMessageDialog(this, "El nombre de la categoría contiene caracteres inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void guardarCategoria() {
        //Lógica para guardar el proveedor en la BD
        if (validarCampos()) {
            String nombre = txtNombreCategoria.getText().trim();

            // Convertir nombre a formato de oración
            nombre = convertirAFormatoOracion(nombre);

            try {
                conexionBD.getConnection(); // Asegúrar de que la conexión esté abierta
                //System.out.println("Valor de txtNombreCategoria: " + txtNombreCategoria.getText().trim());
                String sql = "INSERT INTO categoria (nombre_categoria) VALUES (?)";
                PreparedStatement pstmt = conexionBD.getConnection().prepareStatement(sql);
                pstmt.setString(1, nombre);
                pstmt.executeUpdate();
                conexionBD.closeConnection(); // Cierra la conexión
                JOptionPane.showMessageDialog(this, "Categoría guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cierra la ventana
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la categoría: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        txtNombreCategoria.setText("");
        restaurarTextoPredictivo(); //Restaura los textos predictivos
    }

    /**
     * Método para convertir el nombre de la categoría en tipo oración y dejarla
     * en minúscula
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
        txtNombreCategoria = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Crear Nueva Categoría");

        txtNombreCategoria.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtNombreCategoria.setText("Nombre Categoría (Obligatorio)");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel2.setText("Nombre Categoría");

        btnGuardar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(46, 46, 46))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpiar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir)
                .addContainerGap(54, Short.MAX_VALUE))
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
        guardarCategoria();
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
            java.util.logging.Logger.getLogger(CrearCategoriaView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrearCategoriaView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrearCategoriaView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrearCategoriaView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrearCategoriaView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtNombreCategoria;
    // End of variables declaration//GEN-END:variables
}
