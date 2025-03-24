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

import com.mysql.cj.jdbc.Blob;
import controller.ConsultarProductoController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.FocusEvent;
import model.Categoria;
import model.Proveedor;

import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import model.PaginacionTabla;
import model.Producto;

public class ConsultarProductoView extends javax.swing.JFrame {

    private JTextField[] camposTexto = new JTextField[4];
    private Map<String, Integer> categoriaMap = new HashMap<>();
    private Map<String, Integer> proveedorMap = new HashMap<>();

    private JComboBox<String> proveedorComboBox;
    private JComboBox<String> categoriaComboBox;
    private boolean restableciendoComboBox = false; // Bandera para evitar eventos innecesarios
    private boolean isUpdatingText = false; // Bandera global
    private DocumentListener documentListener;
    private boolean isUpdating = false; // Bandera global

    // Atributo global en la clase ConsultarProductoView
    private int columnaImagen;

    private ConsultarProductoController controller; // Referencia al controlador

    // Constantes para textos predictivos
    private static final String TEXTO_PREDICTIVO_BUSCAR_PROVEEDOR = "Buscar por Proveedor";
    private static final String TEXTO_PREDICTIVO_BUSCAR_CATEGORIA = "Buscar por Categoría";
    private static final String TEXTO_PREDICTIVO_BUSCAR_CÓDIGO = "Buscar Código";
    private static final String TEXTO_PREDICTIVO_BUSCAR_NOMBRE = "Buscar Nombre";

    /**
     * Crea el contsructor de la clase ConsultarProductoView
     *
     * @param controller
     */
    public ConsultarProductoView(ConsultarProductoController controller) { // Recibe el controlador
        this.controller = controller;
        
        // Configuración inicial del formulario
        initComponents();
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setTitle("Consulta de Producto"); //Abgrega un título  ala ventana

        restaurarTextosPredictivos();
        configurarTabla();

        
        // Configuracion para filtros y tablas
        configurarTabla();

        // Inicializa los campos de texto en el arreglo
        camposTexto[0] = jTextFieldFiltradoProveedor;
        camposTexto[1] = jTextFieldFiltradoCategoria;
        camposTexto[2] = jTextFieldBuscarCodigo;
        camposTexto[3] = jTextFieldBuscarNombre;

        // Aplica el FocusListener a cada campo de texto
        for (int i = 0; i < camposTexto.length; i++) {
            if (camposTexto[i] != null) {
                agregarFocusListener(camposTexto[i], obtenerTextoPredictivo(i));
            }
        }

        // ActionListener para los eventos de los jComboBox
        jComboBoxCategoria.addActionListener(e -> {
            if (!restableciendoComboBox && !isUpdating) { // Evita eventos durante la limpieza
                realizarBusqueda();
            }
        });

        jComboBoxProveedor.addActionListener(e -> {
            if (!restableciendoComboBox && !isUpdating) { // Evita eventos durante la limpieza
                realizarBusqueda();
            }
        });

        // Configuración para limitar caracteres en los JTextField
        configurarLimitesTexto(jTextFieldFiltradoProveedor, 50); // Ejemplo: máximo 50 caracteres
        configurarLimitesTexto(jTextFieldFiltradoCategoria, 50);

        // Listeners para buscar categorías o proveedores en tiempo real
        jTextFieldFiltradoCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarCategorias(jComboBoxCategoria, jTextFieldFiltradoCategoria.getText()); // Se llama al m
            }
        });

        jTextFieldFiltradoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarProveedores(jComboBoxProveedor, jTextFieldFiltradoProveedor.getText()); // Se llama al 
            }
        });
    }

    public void setController(ConsultarProductoController controller) {
        this.controller = controller;

        if (controller != null) {
            
            configurarFiltrosBusqueda();
        } else {
            System.err.println("Advertencia: El controlador es null y no se puede configurar.");
        }
    }

    /**
     * Agregar un FocusListener a los campos de texto para simular textos
     * predictivos.
     *
     * @param campoTexto El campo de texto.
     * @param textoPredictivo El texto predictivo.
     */
    private void agregarFocusListener(JTextField campoTexto, String textoPredictivo) {
        campoTexto.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Si el texto actual es igual al texto predictivo, lo limpiamos
                if (campoTexto.getText().equals(textoPredictivo)) {
                    campoTexto.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Si el campo queda vacío al perder el foco, restauramos el texto predictivo
                if (campoTexto.getText().isEmpty()) {
                    campoTexto.setText(textoPredictivo);
                }
            }
        });
    }

    public void realizarBusqueda() {
        if (isUpdating) {
            return; // Evita ejecutar la búsqueda si el sistema está en modo de limpieza
        }

        isUpdating = true; // Activa la bandera para evitar eventos en cascada

        try {
            // Verifica que el controlador esté inicializado
            if (this.controller == null) {
                System.err.println("Error: El controlador no está inicializado.");
                return;
            }

            // Obtiene los valores actuales de los filtros
            String codigo = jTextFieldBuscarCodigo.getText().trim();
            String nombre = jTextFieldBuscarNombre.getText().trim();

            // Ignora la búsqueda si los textos predictivos están presentes o si los campos están vacíos
            if (codigo.equals(TEXTO_PREDICTIVO_BUSCAR_CÓDIGO) || codigo.isEmpty()) {
                codigo = null;
            }
            if (nombre.equals(TEXTO_PREDICTIVO_BUSCAR_NOMBRE) || nombre.isEmpty()) {
                nombre = null;
            }

            Integer categoriaId = null;
            Integer proveedorId = null;

            // Valida las selecciones en los ComboBox
            if (jComboBoxCategoria.getSelectedIndex() > 0) {
                categoriaId = categoriaMap.get(jComboBoxCategoria.getSelectedItem().toString());
            }
            if (jComboBoxProveedor.getSelectedIndex() > 0) {
                proveedorId = proveedorMap.get(jComboBoxProveedor.getSelectedItem().toString());
            }

            // Evita la consulta si todos los filtros están vacíos
            if (codigo == null && nombre == null && categoriaId == null && proveedorId == null) {
                return;
            }

            // Ejecuta la consulta con los filtros aplicados
            controller.filtrarProductos((DefaultTableModel) jTable1.getModel(), codigo, nombre, categoriaId, proveedorId);
        } finally {
            isUpdating = false; // Restablece la bandera
        }
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
                return TEXTO_PREDICTIVO_BUSCAR_PROVEEDOR; // Texto predictivo para buscar provvedor
            case 1:
                return TEXTO_PREDICTIVO_BUSCAR_CATEGORIA; // Texto predictivo para buscar categoria
            case 2:
                return TEXTO_PREDICTIVO_BUSCAR_CÓDIGO; // Texto predictivo para buscar código
            case 3:
                return TEXTO_PREDICTIVO_BUSCAR_NOMBRE; // Texto predictivo para buscar nombre
            default:
                return ""; // Por defecto, texto vacío
        }
    }

    // Getters para los botones, campos de texto y comboBox
    public JButton getjButtonImprimir() { // Devuelve el botón para imprimir
        return jButtonImprimir;
    }

    public JButton getjButtonLimpiar() {// Devuelve el botón para limpiar los campos
        return jButtonLimpiar;
    }

    public JButton getjButtonSalir() {// Devuelve el botón para salir
        return jButtonSalir;
    }

    public JTextField getjTextFieldBuscarCodigo() {
        return jTextFieldBuscarCodigo;
    }

    public JTextField getjTextFieldBuscarNombre() {
        return jTextFieldBuscarNombre;
    }

    public JTable getjTable1() {
        return jTable1;
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

    // Método público para activar el JToggleButton
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
        restableciendoComboBox = true; // Activamos la bandera

        this.categoriaMap = categoriaMap; // Guardar el mapa internamente
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Añadir opciones iniciales al ComboBox
        model.addElement("Seleccione una Categoría");
        model.addElement("----------------------------------------------");

        // Añadir las categorías al modelo
        for (String categoria : categorias) {
            model.addElement(categoria);
        }

        SwingUtilities.invokeLater(() -> {
            jComboBoxCategoria.setModel(model);
            jComboBoxCategoria.setSelectedIndex(0); // Configura el índice para dejarlo sin selección
        });

        // Asignar el modelo al ComboBox en el hilo de la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            jComboBoxCategoria.setModel(model);
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
        restableciendoComboBox = true; // Activamos la bandera

        this.proveedorMap = proveedorMap; // Almacenar el mapa internamente
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Añadir opciones iniciales al ComboBox
        model.addElement("Seleccione un Proveedor");
        model.addElement("----------------------------------------------");

        // Añadir los proveedores al modelo
        for (String proveedor : proveedores) {
            model.addElement(proveedor);
        }

        SwingUtilities.invokeLater(() -> {
            jComboBoxProveedor.setModel(model);
            jComboBoxProveedor.setSelectedIndex(0); // Configura el índice para dejarlo sin selección
        });

        // Asignar el modelo al ComboBox en el hilo de la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            jComboBoxProveedor.setModel(model);
        });

        restableciendoComboBox = false; // Desactivamos la bandera
    }

    /**
     * Método para restaurar los textos predictivos.
     */
    public void restaurarTextosPredictivos() {
        // Configura los textos iniciales en los campos
        jTextFieldFiltradoProveedor.setText(TEXTO_PREDICTIVO_BUSCAR_PROVEEDOR);
        jTextFieldFiltradoCategoria.setText(TEXTO_PREDICTIVO_BUSCAR_CATEGORIA);
        jTextFieldBuscarCodigo.setText(TEXTO_PREDICTIVO_BUSCAR_CÓDIGO);
        jTextFieldBuscarNombre.setText(TEXTO_PREDICTIVO_BUSCAR_NOMBRE);

        // Restaura los textos predictivos en los campos de texto
        for (int i = 0; i < camposTexto.length; i++) {
            if (camposTexto[i] != null && camposTexto[i].getText().isEmpty()) {
                camposTexto[i].setText(obtenerTextoPredictivo(i));
            }
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
        modeloFiltrado.addElement("---------------------------------------");

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
        modeloFiltrado.addElement("---------------------------------------");

        // Filtrar proveedores según el texto de búsqueda
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getTexto().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                modeloFiltrado.addElement(proveedor.getTexto()); // Añade los proveedores coincidentes
            }
        }

        comboBox.setModel(modeloFiltrado); // Establece el modelo filtrado en el ComboBox
    }

    private void configurarTabla() {
        // Configurar columnas y modelo de la tabla
        DefaultTableModel modelo = new DefaultTableModel(new Object[][]{}, new String[]{
            "Selección", "Código", "Categoría", "Nombre Producto", "Existencias", "Valor Unitario", "Valor Total", "Imagen"
        }) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 ->
                        Boolean.class;
                    case 4 ->
                        Integer.class;
                    case 5, 6 ->
                        Double.class;
                    case 7 ->
                        Object.class; // Columna de imágenes
                    default ->
                        String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo la columna de selección es editable
            }
        };

        // Asignar el modelo a la tabla
        jTable1.setModel(modelo);

        // Validación para configurar el renderizador
        if (jTable1.getColumnModel().getColumnCount() < 8) {
            System.err.println("Error: No se pudo configurar el renderizador porque la tabla no tiene suficientes columnas.");
            return; // Termina el método si no hay suficientes columnas
        }

        // Configurar el MouseListener para manejar clics en la tabla
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = jTable1.rowAtPoint(e.getPoint());
                int columna = jTable1.columnAtPoint(e.getPoint());

                // Obtener el índice de la columna de imágenes
                int columnaImagen = jTable1.getColumnModel().getColumnIndex("Imagen");

                if (columna == columnaImagen) {
                    Object value = jTable1.getValueAt(fila, columna);
                    System.out.println("Valor en la celda: " + value);

                    // Crear la ventana modal
                    JDialog ventanaModal = new JDialog((Frame) null, "Vista de Imagen", true);
                    ventanaModal.setLayout(new BorderLayout());

                    // Si el valor es un Blob (imagen)
                    if (value instanceof Blob) {
                        try {
                            Blob imagenBlob = (Blob) value;
                            if (imagenBlob.length() > 0) { // Verificar si la longitud del Blob es mayor que 0
                                byte[] bytes = imagenBlob.getBytes(1, (int) imagenBlob.length()); // Convertir el Blob en un array de bytes
                                ImageIcon icono = new ImageIcon(bytes); // Crear el ImageIcon a partir de los bytes

                                // Crear una ventana modal para mostrar la imagen
                                JLabel etiquetaImagen = new JLabel(new ImageIcon(icono.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH)));
                                etiquetaImagen.setHorizontalAlignment(SwingConstants.CENTER);
                                ventanaModal.add(etiquetaImagen, BorderLayout.CENTER);
                            } else {
                                // Si el Blob está vacío, mostrar el texto "Imagen No Disponible"
                                JLabel textoNoDisponible = new JLabel("Imagen No Disponible", SwingConstants.CENTER);
                                textoNoDisponible.setFont(new Font("Arial", Font.BOLD, 16));
                                textoNoDisponible.setForeground(Color.RED);  // Hacer el texto más visible
                                ventanaModal.add(textoNoDisponible, BorderLayout.CENTER);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Si no hay imagen (no es un Blob), mostrar el texto "No imagen disponible"
                        JLabel textoNoDisponible = new JLabel("No imagen disponible", SwingConstants.CENTER);
                        textoNoDisponible.setFont(new Font("Arial", Font.BOLD, 16));
                        textoNoDisponible.setForeground(Color.RED);  // Hacer el texto más visible
                        ventanaModal.add(textoNoDisponible, BorderLayout.CENTER);
                    }

                    // Configurar el tamaño de la ventana y su ubicación
                    ventanaModal.setSize(450, 450);
                    ventanaModal.setLocationRelativeTo(null);

                    // Establecer la acción de cierre en la "X"
                    ventanaModal.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                    // Mostrar la ventana modal
                    ventanaModal.setVisible(true);
                }
            }
        });

        // Configurar el renderizador de la columna de imágenes
        jTable1.getColumnModel().getColumn(jTable1.getColumnModel().getColumnIndex("Imagen"))
                .setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    protected void setValue(Object value) {
                        try {
                            // Usar un icono por defecto para la columna con ruta relativa
                            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/view/Icono Ver Imagen.png"));
                            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
                            ImageIcon iconoRedimensionado = new ImageIcon(imagenEscalada);

                            setHorizontalAlignment(SwingConstants.CENTER);

                            // Si el valor es un Blob (imagen), mostrar el icono
                            if (value instanceof Blob) {
                                setIcon(iconoRedimensionado);
                                setText(null);
                            } else {
                                // Si no es una imagen, mostrar texto
                                setIcon(null);
                                setText("No imagen");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            setText("Error");
                            setIcon(null);
                            setHorizontalAlignment(SwingConstants.CENTER);
                        }
                    }
                });
    }

    private void actualizarTabla(List<Producto> productos) {
        List<Object[]> datosTabla = new ArrayList<>();

        for (Producto producto : productos) {
            datosTabla.add(new Object[]{
                false, // Selección (JCheckBox)
                producto.getCodigo(),
                producto.getCategoriaNombre(),
                producto.getNombre(),
                producto.getCantidad(),
                producto.getValorUnitario(),
                producto.getValorTotal(),
                producto.getImagen() // La imagen será renderizada posteriormente
            });
        }

        String[] columnas = {"Selección", "Código", "Categoría", "Nombre Producto", "Existencias", "Valor Unitario", "Valor Total", "Imagen"};

        // Crear modelo con soporte para paginación
        PaginacionTabla modelo = new PaginacionTabla(datosTabla, columnas);
        jTable1.setModel(modelo);

        // Configurar paginación (asociar botones de siguiente y anterior)
        configurarPaginacion(modelo);
    }

    private void configurarFiltrosBusqueda() {
        if (this.controller == null) {
            System.err.println("Advertencia: El controlador no está inicializado. No se pueden configurar los filtros.");
            return; // Salir del método si el controlador es null
        }

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> realizarBusqueda());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> realizarBusqueda());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> realizarBusqueda());
            }
        };

        jTextFieldBuscarCodigo.getDocument().addDocumentListener(documentListener);
        jTextFieldBuscarNombre.getDocument().addDocumentListener(documentListener);
    }

    private void configurarPaginacion(PaginacionTabla modelo) {
        int paginasTotales = modelo.getPaginasTotales();

        // Asigna los textos a los botones
        jButtonAnterior.setText("<"); // Botón para ir a la página anterior
        jButtonSiguiente.setText(">"); // Botón para ir a la página siguiente

        // Activa o desactiva los botones según la página actual
        jButtonSiguiente.setEnabled(modelo.getPaginaActual() < paginasTotales);
        jButtonAnterior.setEnabled(modelo.getPaginaActual() > 1);

        // Configura los eventos para cambiar de página
        jButtonSiguiente.addActionListener(e -> {
            modelo.setPaginaActual(modelo.getPaginaActual() + 1);
            configurarPaginacion(modelo); // Actualiza la paginación
        });

        jButtonAnterior.addActionListener(e -> {
            modelo.setPaginaActual(modelo.getPaginaActual() - 1);
            configurarPaginacion(modelo); // Actualiza la paginación
        });
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje El mensaje de error.
     */
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); // Muestra un cuadro de diálogo con el error
    }

    // Método para limitar el tamaño de los TextField
    private void configurarLimitesTexto(JTextField textField, int maxCaracteres) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= maxCaracteres) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String text, javax.swing.text.AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= maxCaracteres) {
                    super.insertString(fb, offset, text, attrs);
                }
            }
        });
    }

    public void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0); // Limpia todas las filas de la tabla
    }

    public JPanel crearPanelOpcionesImpresion() {
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));

        JLabel etiqueta = new JLabel("Seleccione qué desea imprimir:");
        panelOpciones.add(etiqueta);

        // Opciones de impresión con botones de selección
        JCheckBox opcionSeleccionTabla = new JCheckBox("Seleccionado en la tabla");
        JCheckBox opcionPorProveedor = new JCheckBox("Por proveedor");
        JCheckBox opcionPorCategoria = new JCheckBox("Por categoría");
        JCheckBox opcionTodoInventario = new JCheckBox("Todo el inventario");

        panelOpciones.add(opcionSeleccionTabla);
        panelOpciones.add(opcionPorProveedor);
        panelOpciones.add(opcionPorCategoria);
        panelOpciones.add(opcionTodoInventario);

        return panelOpciones;
    }

    public Producto getProductoDeFila(int fila) {
        Producto producto = new Producto();
        producto.setCodigo((String) jTable1.getValueAt(fila, 1)); // Código
        producto.setCategoriaNombre((String) jTable1.getValueAt(fila, 2)); // Categoría
        producto.setNombre((String) jTable1.getValueAt(fila, 3)); // Nombre Producto
        producto.setCantidad((Integer) jTable1.getValueAt(fila, 4)); // Existencias
        producto.setValorUnitario((Double) jTable1.getValueAt(fila, 5)); // Valor Unitario
        producto.setValorTotal((Double) jTable1.getValueAt(fila, 6)); // Valor Total

        // Manejar imagen como Object y convertirlo si es necesario
        Object valorImagen = jTable1.getValueAt(fila, 7);
        if (valorImagen instanceof Blob) {
            producto.setImagen((Blob) valorImagen);
        } else {
            producto.setImagen(null); // Si no es un Blob, dejamos la imagen como null
        }

        return producto;
    }

    public String mostrarDialogoInput(String mensaje) {
        return JOptionPane.showInputDialog(this, mensaje, "Entrada de Datos", JOptionPane.PLAIN_MESSAGE);
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
        jTextFieldFiltradoProveedor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldFiltradoCategoria = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxProveedor = new javax.swing.JComboBox<>();
        jComboBoxCategoria = new javax.swing.JComboBox<>();
        jButtonImprimir = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldBuscarCodigo = new javax.swing.JTextField();
        jTextFieldBuscarNombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonAnterior = new javax.swing.JButton();
        jButtonSiguiente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Módulo Registro de Inventario");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Hardware Store Inventory FFIG");

        jToggleButtonRegistrar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonRegistrar.setText("Registrar Producto");
        jToggleButtonRegistrar.setMaximumSize(new java.awt.Dimension(201, 32));
        jToggleButtonRegistrar.setMinimumSize(new java.awt.Dimension(201, 32));

        jToggleButtonConsultar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonConsultar.setText("Consultar Producto");
        jToggleButtonConsultar.setMaximumSize(new java.awt.Dimension(201, 32));
        jToggleButtonConsultar.setMinimumSize(new java.awt.Dimension(201, 32));

        jToggleButtonActualizar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonActualizar.setText("Actualizar Producto");
        jToggleButtonActualizar.setMaximumSize(new java.awt.Dimension(201, 32));
        jToggleButtonActualizar.setMinimumSize(new java.awt.Dimension(201, 32));

        jToggleButtonEliminar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jToggleButtonEliminar.setText("Eliminar Producto");
        jToggleButtonEliminar.setMaximumSize(new java.awt.Dimension(201, 32));
        jToggleButtonEliminar.setMinimumSize(new java.awt.Dimension(201, 32));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Buscar por Proveedor");

        jTextFieldFiltradoProveedor.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        jTextFieldFiltradoProveedor.setText("Buscar por Proveedor");
        jTextFieldFiltradoProveedor.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Buscar por Categoría");

        jTextFieldFiltradoCategoria.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        jTextFieldFiltradoCategoria.setText("Buscar por Categoría");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Seleccionar un Proveedor");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Seleccionar una Categoría");

        jComboBoxProveedor.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        jComboBoxProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un Proveedor" }));

        jComboBoxCategoria.setFont(new java.awt.Font("Segoe UI", 3, 13)); // NOI18N
        jComboBoxCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sleccione una Categoría" }));

        jButtonImprimir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButtonImprimir.setText("Imprimir");

        jButtonLimpiar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButtonLimpiar.setText("Limpiar");

        jButtonSalir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jButtonSalir.setText("Salir");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel13.setText("Para realizar la consulta puede hacrelo por proveedor o categoría");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel14.setText("Consulte directamente por código o nombre");

        jTextFieldBuscarCodigo.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jTextFieldBuscarCodigo.setText("Buscar Código");

        jTextFieldBuscarNombre.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jTextFieldBuscarNombre.setText("Buscar Nombre");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Código Producto");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Nombre Producto");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Selección", "Código", "Categoría", "Nombre Producto", "Existencias", "Valor Unitario", "Valor Total", "Imagen"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setShowGrid(true);
        jScrollPane2.setViewportView(jTable1);

        jButtonAnterior.setText("<");

        jButtonSiguiente.setText(">");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jToggleButtonRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButtonConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jToggleButtonActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jToggleButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButtonImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldFiltradoProveedor, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4)
                                            .addComponent(jTextFieldFiltradoCategoria)
                                            .addComponent(jLabel6)
                                            .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addGap(12, 12, 12)
                                            .addComponent(jTextFieldBuscarCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextFieldBuscarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButtonAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(47, 47, 47)
                                            .addComponent(jButtonSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(32, 32, 32))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(301, 301, 301)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
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
                    .addComponent(jToggleButtonRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButtonConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButtonActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldFiltradoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldFiltradoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jTextFieldBuscarCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jTextFieldBuscarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonImprimir)
                    .addComponent(jButtonLimpiar)
                    .addComponent(jButtonSalir))
                .addContainerGap(60, Short.MAX_VALUE))
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
    private javax.swing.JButton jButtonAnterior;
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonSiguiente;
    private javax.swing.JComboBox<String> jComboBoxCategoria;
    private javax.swing.JComboBox<String> jComboBoxProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldBuscarCodigo;
    private javax.swing.JTextField jTextFieldBuscarNombre;
    private javax.swing.JTextField jTextFieldFiltradoCategoria;
    private javax.swing.JTextField jTextFieldFiltradoProveedor;
    private javax.swing.JToggleButton jToggleButtonActualizar;
    private javax.swing.JToggleButton jToggleButtonConsultar;
    private javax.swing.JToggleButton jToggleButtonEliminar;
    private javax.swing.JToggleButton jToggleButtonRegistrar;
    // End of variables declaration//GEN-END:variables

    public Object getTablaProductos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
