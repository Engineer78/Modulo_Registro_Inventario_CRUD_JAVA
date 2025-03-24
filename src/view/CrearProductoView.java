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

import controller.CrearProductoController;
import model.Categoria;
import model.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Vista para crear un nuevo producto.
 */
public class CrearProductoView extends javax.swing.JFrame {

    private transient Blob imagenBlob; // Transient para que no intente serializarse
    private JTextField[] camposTexto = new JTextField[10];

    private CrearProveedorView ventanaProveedor = null; // Ventanas para crear proveedor y categoría
    private CrearCategoriaView ventanaCategoria = null;

    private Map<String, Integer> categoriaMap = new HashMap<>();
    private Map<String, Integer> proveedorMap = new HashMap<>();

    private JComboBox<String> proveedorComboBox;
    private JComboBox<String> categoriaComboBox;
    private boolean restableciendoComboBox = false; // Bandera para evitar eventos innecesarios

    private CrearProductoController controller; // Referencia al controlador

    // Constantes para textos predictivos
    private static final String TEXTO_PREDICTIVO_CODIGO_PRODUCTO = "Código del producto";
    private static final String TEXTO_PREDICTIVO_NOMBRE_PRODUCTO = "Nombre del producto";
    private static final String TEXTO_PREDICTIVO_CANTIDAD_PRODUCTO = "Cantidad del producto";
    private static final String TEXTO_PREDICTIVO_VALOR_UNITARIO = "Valor unitario";
    private static final String TEXTO_PREDICTIVO_VALOR_TOTAL = "Valor total";
    private static final String TEXTO_PREDICTIVO_BUSCAR_PROVEEDOR = "Buscar Proveedor";
    private static final String TEXTO_PREDICTIVO_BUSCAR_CATEGORIA = "Buscar Categoría";

    /**
     * Constructor de la clase CrearProductoView.
     *
     * @param controller El controlador de la vista.
     */
    public CrearProductoView(CrearProductoController controller) { // Recibe el controlador
        this.controller = controller;
        initComponents(); // Inicializa los componentes (generado por el diseñador de formularios)

        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setTitle("Registro de Producto"); //Abgrega un título  ala ventana

        // Configuración inicial del formulario
        restaurarTextosPredictivos();

        // Configuración visual del campo "Valor Total"
        txtValorTotal.setEditable(false);
        txtValorTotal.setBackground(new java.awt.Color(215, 215, 215));
        txtValorTotal.setForeground(java.awt.Color.BLACK);

        // Inicializa los campos de texto en el arreglo
        camposTexto[0] = txtCodigoProducto;
        camposTexto[1] = txtNombreProducto;
        camposTexto[2] = txtCantidadProducto;
        camposTexto[3] = txtValorUnitario;
        camposTexto[4] = txtValorTotal;
        camposTexto[5] = txtBuscarProveedor;
        camposTexto[6] = txtBuscarCategoria;

        // Aplica el FocusListener a cada campo de texto
        for (int i = 0; i < camposTexto.length; i++) {
            if (camposTexto[i] != null) {
                agregarFocusListener(camposTexto[i], obtenerTextoPredictivo(i));
            }
        }

        // Listeners para buscar categorías o proveedores en tiempo real
        txtBuscarCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarCategorias(jComboBoxCategoria, txtBuscarCategoria.getText()); // Se llama al método de filtrado
            }
        });

        txtBuscarProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarProveedores(jComboBoxProveedor, txtBuscarProveedor.getText()); // Se llama al método de filtrado
            }
        });

        // Listeners para calcular el valor total dinámicamente
        txtCantidadProducto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarValorTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarValorTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarValorTotal();
            }
        });

        txtValorUnitario.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarValorTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarValorTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarValorTotal();
            }
        });

        // Listeners para los JComboBox para manejar la creación de nuevas categorías/proveedores
        jComboBoxCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) jComboBoxCategoria.getSelectedItem();
                if (seleccion != null && "Crear Nueva Categoría".equals(seleccion)) {
                    abrirVentanaCategoria(); // Abre la ventana para crear una nueva categoría
                }
            }
        });

        jComboBoxProveedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) jComboBoxProveedor.getSelectedItem();
                if (seleccion != null && "Crear Nuevo Proveedor".equals(seleccion)) {
                    abrirVentanaProveedor(); // Abre la ventana para crear un nuevo proveedor
                }
            }
        });
    }

    /**
     * Agrega un FocusListener a los campos de texto para simular textos
     * predictivos.
     *
     * @param campoTexto El campo de texto.
     * @param textoPredictivo El texto predictivo.
     */
    private void agregarFocusListener(JTextField campoTexto, String textoPredictivo) {
        campoTexto.setText(textoPredictivo);
        campoTexto.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoTexto.getText().equals(textoPredictivo)) {
                    campoTexto.setText(""); // Limpia el campo cuando gana el foco
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoTexto.getText().isEmpty()) {
                    campoTexto.setText(textoPredictivo); // Restaura el texto predictivo si está vacío
                }
            }
        });
    }

    /**
     * Devuelve el texto predictivo según el índice del campo.
     *
     * @param index El índice del campo.
     * @return El texto predictivo.
     */
    private String obtenerTextoPredictivo(int index) {
        switch (index) {
            case 0:
                return TEXTO_PREDICTIVO_CODIGO_PRODUCTO; // Texto predictivo para el código del producto
            case 1:
                return TEXTO_PREDICTIVO_NOMBRE_PRODUCTO; // Texto predictivo para el nombre del producto
            case 2:
                return TEXTO_PREDICTIVO_CANTIDAD_PRODUCTO; // Texto predictivo para la cantidad
            case 3:
                return TEXTO_PREDICTIVO_VALOR_UNITARIO; // Texto predictivo para el valor unitario
            case 4:
                return TEXTO_PREDICTIVO_VALOR_TOTAL; // Texto predictivo para el valor total
            case 5:
                return TEXTO_PREDICTIVO_BUSCAR_PROVEEDOR; // Texto predictivo para buscar proveedor
            case 6:
                return TEXTO_PREDICTIVO_BUSCAR_CATEGORIA; // Texto predictivo para buscar categoría
            default:
                return ""; // Por defecto, texto vacío
        }
    }

    // Getters para los botones y campos de texto
    /**
     * Obtiene el botón de cargar imagen.
     *
     * @return JButton
     */
    public JButton getCargarImagenButton() {
        return jButton1; // Devuelve el botón para cargar imágenes
    }

    /**
     * Obtiene el botón de guardar.
     *
     * @return JButton
     */
    public JButton getGuardarButton() {
        return jButton2; // Devuelve el botón para guardar
    }

    /**
     * Obtiene el botón de limpiar.
     *
     * @return JButton
     */
    public JButton getLimpiarButton() {
        return jButton3; // Devuelve el botón para limpiar los campos
    }

    /**
     * Obtiene el botón de salir.
     *
     * @return JButton
     */
    public JButton getSalirButton() {
        return jButton4; // Devuelve el botón para salir
    }

    /**
     * Obtiene el campo de texto del código del producto.
     *
     * @return JTextField
     */
    public JTextField getCodigoProductoField() {
        return txtCodigoProducto; // Devuelve el campo de texto para el código del producto
    }

    /**
     * Obtiene el campo de texto del nombre del producto.
     *
     * @return JTextField
     */
    public JTextField getNombreProductoField() {
        return txtNombreProducto; // Devuelve el campo de texto para el nombre del producto
    }

    /**
     * Obtiene el campo de texto de la cantidad.
     *
     * @return JTextField
     */
    public JTextField getCantidadProductoField() {
        return txtCantidadProducto; // Devuelve el campo de texto para la cantidad del producto
    }

    /**
     * Obtiene el campo de texto del valor unitario.
     *
     * @return JTextField
     */
    public JTextField getValorUnitarioField() {
        return txtValorUnitario; // Devuelve el campo de texto para el valor unitario del producto
    }

    /**
     * Obtiene el campo de texto del valor total.
     *
     * @return JTextField
     */
    public JTextField getValorTotalField() {
        return txtValorTotal; // Devuelve el campo de texto para el valor total del producto
    }

    /**
     * Obtiene el combo box de categoría.
     *
     * @return JComboBox<String>
     */
    public JComboBox<String> getjComboBoxCategoria() {
        return jComboBoxCategoria; // Devuelve el ComboBox de categorías
    }

    /**
     * Obtiene el combo box de proveedor.
     *
     * @return JComboBox<String>
     */
    public JComboBox<String> getjComboBoxProveedor() {
        return jComboBoxProveedor; // Devuelve el ComboBox de proveedores
    }

    /**
     * Obtiene la etiqueta de la imagen.
     *
     * @return JLabel
     */
    public JLabel getjLabelImagen() {
        return jLabelImagen; // Devuelve el JLabel para mostrar la imagen
    }

    public JToggleButton getjToggleButtonRegistrar() {
        return jToggleButtonRegistrar;
    }

    public JToggleButton getjToggleButtonConsultar() {
        return jToggleButtonConsultar;
    }

    public JToggleButton getjToggleButtonActualizar() {
        return jToggleButtonActualizar;
    }

    public JToggleButton getjToggleButtonEliminar() {
        return jToggleButtonEliminar;
    }

    /**
     * Carga las categorías en el ComboBox de categorías.
     *
     * @param categorias La lista de categorías a cargar.
     * @param categoriaMap El mapa de categorías para asociar nombres con IDs.
     */
    public void cargarCategorias(List<String> categorias, Map<String, Integer> categoriaMap) {
        // Depuración: Verificar si las categorías llegan correctamente
        //System.out.println("Categorías recibidas: " + categorias);

        restableciendoComboBox = true; // Activamos la bandera

        this.categoriaMap = categoriaMap; // Guardar el mapa internamente
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Añadir opciones iniciales al ComboBox
        model.addElement("Seleccione una Categoría");
        model.addElement("Crear Nueva Categoría");
        model.addElement("----------------------------------------------------");

        // Añadir las categorías al modelo
        for (String categoria : categorias) {
            model.addElement(categoria);
        }

        // Depuración: Verificar el modelo antes de asignarlo
        //System.out.println("Modelo de categorías: " + model);
        // Asignar el modelo al ComboBox en el hilo de la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            jComboBoxCategoria.setModel(model);
            //System.out.println("ComboBox de categorías actualizado.");
        });

        restableciendoComboBox = false; // Desactivamos la bandera
    }

    /**
     * Carga los proveedores en el ComboBox de proveedores.
     *
     * @param proveedores La lista de proveedores a cargar.
     * @param proveedorMap El mapa de proveedores para asociar nombres con IDs.
     */
    public void cargarProveedores(List<String> proveedores, Map<String, Integer> proveedorMap) {
        // Depuración: Verificar si los proveedores llegan correctamente
        //System.out.println("Proveedores recibidos: " + proveedores);

        restableciendoComboBox = true; // Activamos la bandera

        this.proveedorMap = proveedorMap; // Almacenar el mapa internamente
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Añadir opciones iniciales al ComboBox
        model.addElement("Seleccione un Proveedor");
        model.addElement("Crear Nuevo Proveedor");
        model.addElement("----------------------------------------------------");

        // Añadir los proveedores al modelo
        for (String proveedor : proveedores) {
            model.addElement(proveedor);
        }

        // Depuración: Verificar el modelo antes de asignarlo
        //System.out.println("Modelo de proveedores: " + model);
        // Asignar el modelo al ComboBox en el hilo de la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            jComboBoxProveedor.setModel(model);
            //System.out.println("ComboBox de proveedores actualizado.");
        });

        restableciendoComboBox = false; // Desactivamos la bandera
    }

    /**
     * Obtiene la imagen en formato Blob.
     *
     * @return Blob
     */
    public Blob getImagenBlob() {
        return imagenBlob; // Devuelve la imagen actual en formato Blob
    }

    /**
     * Establece la imagen en formato Blob.
     *
     * @param imagenBlob La imagen en formato Blob.
     */
    public void setImagenBlob(Blob imagenBlob) {
        this.imagenBlob = imagenBlob; // Establece la imagen en formato Blob
    }

    /**
     * Carga una imagen desde un archivo y la muestra en el jLabelImagen.
     *
     * @param file El archivo de imagen a cargar.
     */
    public void cargarImagen(File file) {
        try {
            Blob localImagenBlob = convertirImagenABlob(file); // Convierte el archivo a Blob
            setImagenBlob(localImagenBlob); // Almacena el Blob en la variable
            mostrarImagen(file); // Muestra la imagen en el JLabel
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al convertir la imagen a Blob: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra una imagen en el jLabelImagen.
     *
     * @param file El archivo de imagen a mostrar.
     */
    private void mostrarImagen(File file) {
        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
        Image image = imageIcon.getImage().getScaledInstance(jLabelImagen.getWidth(), jLabelImagen.getHeight(), Image.SCALE_SMOOTH);
        jLabelImagen.setIcon(new ImageIcon(image)); // Configura el JLabel con la imagen ajustada
    }

    /**
     * Convierte una imagen a un objeto Blob.
     *
     * @param file El archivo de imagen a convertir.
     * @return Blob
     * @throws IOException Si ocurre un error al leer el archivo.
     * @throws SQLException Si ocurre un error al crear el Blob.
     */
    private Blob convertirImagenABlob(File file) throws IOException, SQLException {
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int readNum;
            while ((readNum = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, readNum);
            }

            byte[] bytes = bos.toByteArray();
            return new javax.sql.rowset.serial.SerialBlob(bytes); // Devuelve el Blob generado
        }
    }

    /**
     * Valida que los campos obligatorios estén completos.
     *
     * @return boolean
     */
    public boolean validarCampos() {
        if (txtCodigoProducto.getText().isEmpty() || txtNombreProducto.getText().isEmpty()
                || txtCantidadProducto.getText().isEmpty() || txtValorUnitario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos requeridos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false; // Devuelve false si algún campo requerido está vacío
        }
        return true; // Devuelve true si todos los campos están completos
    }

    /**
     * Método para restaurar los textos predictivos.
     */
    public void restaurarTextosPredictivos() {
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
        txtValorUnitario.setText("");
        txtValorTotal.setText("");
        txtBuscarProveedor.setText("");
        txtBuscarCategoria.setText("");

        // Restaura los textos predictivos en los campos de texto
        for (int i = 0; i < camposTexto.length; i++) {
            if (camposTexto[i] != null && camposTexto[i].getText().isEmpty()) {
                camposTexto[i].setText(obtenerTextoPredictivo(i));
            }
        }
        // No se debe recargar los combos aquí
    }

    /**
     * Limpia la imagen y establece la imagen predeterminada.
     */
    public void limpiarImagen() {
        URL imageUrl = getClass().getResource("/view/imagen1.jpg"); // Carga la imagen desde el paquete view o resources
        if (imageUrl != null) {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage().getScaledInstance(jLabelImagen.getWidth(), jLabelImagen.getHeight(), Image.SCALE_SMOOTH);
            jLabelImagen.setIcon(new ImageIcon(image)); // Configura el JLabel con la imagen predeterminada
            imagenBlob = null; // Indica que no hay una imagen personalizada establecida
        } else {
            System.err.println("No se encontró la imagen imagen1.jpg en el paquete view");
        }
    }

    /**
     * Filtra un JComboBox de tipo Categoría.
     *
     * @param comboBox El JComboBox a filtrar.
     * @param textoBusqueda El texto a buscar.
     */
    public void filtrarCategorias(JComboBox<String> comboBox, String textoBusqueda) {
        DefaultComboBoxModel<String> modeloFiltrado = new DefaultComboBoxModel<>();
        List<Categoria> categorias = controller.getOriginalCategorias(); // Obtiene la lista original de categorías

        if (categorias == null || categorias.isEmpty()) {
            return; // Si no hay categorías, detiene la ejecución
        }

        // Añadir opciones especiales
        modeloFiltrado.addElement("Seleccione una Categoría");
        modeloFiltrado.addElement("Crear Nueva Categoría");
        modeloFiltrado.addElement("----------------------------------------------------");

        // Filtrar categorías según el texto de búsqueda
        for (Categoria categoria : categorias) {
            if (categoria.getTexto().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                modeloFiltrado.addElement(categoria.getTexto()); // Añade las categorías coincidentes
            }
        }

        comboBox.setModel(modeloFiltrado); // Establece el modelo filtrado en el ComboBox
    }

    /**
     * Filtra un JComboBox de tipo Proveedor.
     *
     * @param comboBox El JComboBox a filtrar.
     * @param textoBusqueda El texto a buscar.
     */
    public void filtrarProveedores(JComboBox<String> comboBox, String textoBusqueda) {
        DefaultComboBoxModel<String> modeloFiltrado = new DefaultComboBoxModel<>();
        List<Proveedor> proveedores = controller.getOriginalProveedores(); // Obtiene la lista original de proveedores

        if (proveedores == null || proveedores.isEmpty()) {
            return; // Si no hay proveedores, detiene la ejecución
        }

        // Añadir opciones especiales
        modeloFiltrado.addElement("Seleccione un Proveedor");
        modeloFiltrado.addElement("Crear Nuevo Proveedor");
        modeloFiltrado.addElement("----------------------------------------------------");

        // Filtrar proveedores según el texto de búsqueda
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getTexto().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                modeloFiltrado.addElement(proveedor.getTexto()); // Añade los proveedores coincidentes
            }
        }

        comboBox.setModel(modeloFiltrado); // Establece el modelo filtrado en el ComboBox
    }

    /**
     * Métodos para verificar que las ventanas de CrearProveedorView y
     * CrearCategoriaView no se abran en múltiples instancias.
     */
    private void abrirVentanaProveedor() {
        if (ventanaProveedor == null || !ventanaProveedor.isVisible()) {
            ventanaProveedor = new CrearProveedorView(); // Crea la ventana de proveedor
            ventanaProveedor.setVisible(true);

            // Escucha el cierre de la ventana para recargar los proveedores
            ventanaProveedor.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    controller.cargarProveedores(); // Recarga el ComboBox con los datos actualizados
                }
            });
        } else {
            ventanaProveedor.toFront(); // Lleva la ventana al frente si ya está abierta
        }
    }

    private void abrirVentanaCategoria() {
        if (ventanaCategoria == null || !ventanaCategoria.isVisible()) {
            ventanaCategoria = new CrearCategoriaView(); // Crea la ventana de categoría
            ventanaCategoria.setVisible(true);

            // Escucha el cierre de la ventana para recargar las categorías
            ventanaCategoria.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    controller.cargarCategorias(); // Recarga el ComboBox con los datos actualizados
                }
            });
        } else {
            ventanaCategoria.toFront(); // Lleva la ventana al frente
        }
    }

    /**
     * Método para calcular el valor total y mostrarlo en el campo
     * correspondiente.
     */
    private void actualizarValorTotal() {
        try {
            int cantidad = Integer.parseInt(txtCantidadProducto.getText());
            double valorUnitario = Double.parseDouble(txtValorUnitario.getText());
            double valorTotal = cantidad * valorUnitario; // Calcula el valor total
            txtValorTotal.setText(String.format("%.2f", valorTotal)); // Muestra el valor formateado
        } catch (NumberFormatException e) {
            txtValorTotal.setText(""); // Deja el campo vacío si los valores no son válidos
        }
    }

    /**
     * Método para formatear texto en formato tipo oración.
     *
     * @param texto El texto a formatear.
     * @return String
     */
    private String capitalizarTexto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto; // Retorna el texto como está si es nulo o vacío
        }
        String[] palabras = texto.toLowerCase().split(" ");
        StringBuilder textoFormateado = new StringBuilder();

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                textoFormateado.append(Character.toUpperCase(palabra.charAt(0)))
                        .append(palabra.substring(1))
                        .append(" ");
            }
        }
        return textoFormateado.toString().trim(); // Retorna el texto formateado
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje El mensaje de error.
     */
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); // Muestra un cuadro de diálogo con el error
    }

    /**
     * Getter para acceder al controlador desde fuera de la clase.
     *
     * @return CrearProductoController
     */
    public CrearProductoController getController() {
        return this.controller; // Devuelve el controlador asociado a la vista
    }

    private void jComboBoxCategoriaActionPerformed(java.awt.event.ActionEvent evt) {
        if (!restableciendoComboBox && jComboBoxCategoria.getSelectedItem() != null) {
            String seleccionado = (String) jComboBoxCategoria.getSelectedItem();
            if ("Crear Nueva Categoría".equals(seleccionado)) {
                controller.abrirVentanaCategoria(); // Llama al controlador para abrir la ventana de nueva categoría
                jComboBoxCategoria.setSelectedIndex(0); // Restaura la selección al valor por defecto
            }
        }
    }

    private void jComboBoxProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        if (!restableciendoComboBox && jComboBoxProveedor.getSelectedItem() != null) {
            String seleccionado = (String) jComboBoxProveedor.getSelectedItem();
            if ("Crear Nuevo Proveedor".equals(seleccionado)) {
                controller.abrirVentanaProveedor(); // Llama al controlador para abrir la ventana de nuevo proveedor
                jComboBoxProveedor.setSelectedIndex(0); // Restaura la selección al valor por defecto
            }
        }
    }

    public int obtenerIdCategoriaSeleccionada() {
        String categoriaSeleccionada = (String) jComboBoxCategoria.getSelectedItem();
        //System.out.println("ID Categoría obtenido desde la vista: " + categoriaSeleccionada);
        return categoriaMap.getOrDefault(categoriaSeleccionada, -1); // Devuelve -1 si no se encuentra el ID
    }

    public int obtenerIdProveedorSeleccionado() {
        String proveedorSeleccionado = (String) jComboBoxProveedor.getSelectedItem();
        //System.out.println("ID Categoría obtenido desde la vista: " + proveedorSeleccionado);
        return proveedorMap.getOrDefault(proveedorSeleccionado, -1); // Devuelve -1 si no se encuentra el ID
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
        jLabel2 = new javax.swing.JLabel();
        jToggleButtonRegistrar = new javax.swing.JToggleButton();
        jToggleButtonConsultar = new javax.swing.JToggleButton();
        jToggleButtonActualizar = new javax.swing.JToggleButton();
        jToggleButtonEliminar = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarProveedor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxProveedor = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtBuscarCategoria = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxCategoria = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtValorUnitario = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtValorTotal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabelImagen = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Módulo Registro de Inventario");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Hardware Store Inventory FFIG");

        jToggleButtonRegistrar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonRegistrar.setText("Registrar Producto");

        jToggleButtonConsultar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonConsultar.setText("Consultar Producto");

        jToggleButtonActualizar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonActualizar.setText("Actualizar Producto");

        jToggleButtonEliminar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonEliminar.setText("Eliminar Producto");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel3.setText("Ingrese la Información solicitada para crear un registro");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Buscar Proveedor");

        txtBuscarProveedor.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtBuscarProveedor.setText("Buscar Proveedor");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Seleccionar / Crear Proveedor");

        jComboBoxProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un Proveedor" }));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Buscar Categoría");

        txtBuscarCategoria.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtBuscarCategoria.setText("Buscar Categoría");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Seleccionar / Crear Categoría");

        jComboBoxCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una Categoría" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Código Producto");

        txtCodigoProducto.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtCodigoProducto.setText("Código Producto (Obligatorio)");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Nombre del Producto");

        txtNombreProducto.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtNombreProducto.setText("Nombre Producto (Obligatorio)");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Cantidad");

        txtCantidadProducto.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtCantidadProducto.setText("Cantidad (Obligatorio)");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Valor Unitario por Producto");

        txtValorUnitario.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtValorUnitario.setText("Valor Unitario Producto (Obligatorio)");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Valor Total del Producto");

        txtValorTotal.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        txtValorTotal.setText("Valor Total Producto");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Imagen del Producto");

        jLabelImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Imagen1.jpg"))); // NOI18N
        jLabelImagen.setText("jLabel14");

        jButton1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton1.setText("Cargar Imagen");

        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton2.setText("Guardar");

        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton3.setText("Limpiar");

        jButton4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButton4.setText("Salir");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(49, 49, 49)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtBuscarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel12))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13)))
                                .addComponent(jLabel3)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jToggleButtonRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jToggleButtonConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jToggleButtonActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jToggleButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButtonRegistrar)
                    .addComponent(jToggleButtonConsultar)
                    .addComponent(jToggleButtonEliminar)
                    .addComponent(jToggleButtonActualizar))
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9))
                            .addComponent(jLabelImagen))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addGap(65, 65, 65))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBoxCategoria;
    private javax.swing.JComboBox<String> jComboBoxProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButtonActualizar;
    private javax.swing.JToggleButton jToggleButtonConsultar;
    private javax.swing.JToggleButton jToggleButtonEliminar;
    private javax.swing.JToggleButton jToggleButtonRegistrar;
    private javax.swing.JTextField txtBuscarCategoria;
    private javax.swing.JTextField txtBuscarProveedor;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtValorTotal;
    private javax.swing.JTextField txtValorUnitario;
    // End of variables declaration//GEN-END:variables
}
