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

package controller;

import dao.ProductoDAO;
import model.Categoria;
import model.Producto;
import model.Proveedor;
import view.CrearProductoView;
import view.CrearCategoriaView;
import view.CrearProveedorView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la creación de productos.
 */
public class CrearProductoController {

    private CrearProductoView view;
    private ProductoDAO productoDAO;
    private List<Categoria> originalCategorias;
    private List<Proveedor> originalProveedores;
    private Map<String, Integer> categoriaMap; // Mapa para categorías
    private Map<String, Integer> proveedorMap; // Mapa para proveedores

    /**
     * Constructor que inicializa el controlador con la conexión a la base de
     * datos.
     */
    public CrearProductoController(Connection connection) {
        this.productoDAO = new ProductoDAO(connection);
    }

    /**
     * Método para devolver la vista (CrearProductoView)asociada
     */
    public CrearProductoView getView() {
        return this.view; // Retorna la instancia de la vista
    }

    /**
     * Asigna la vista al controlador y configura los ActionListeners.
     *
     * @param view La vista principal de creación de producto.
     */
    public void setView(CrearProductoView view) {
        this.view = view;

        // Asignar ActionListeners a los botones de la vista.
        this.view.getCargarImagenButton().addActionListener(e -> cargarImagen());
        this.view.getGuardarButton().addActionListener(e -> guardarProducto());
        this.view.getLimpiarButton().addActionListener(e -> limpiarFormulario());
        this.view.getSalirButton().addActionListener(e -> salir());
    }

    /**
     * Carga una imagen para el producto desde el sistema de archivos.
     */
    private void cargarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            view.cargarImagen(fileChooser.getSelectedFile());
        }
    }

    /**
     * Guarda el producto en la base de datos.
     */
    private void guardarProducto() {
        try {
            if (!view.validarCampos()) {
                return;
            }

            String codigo = view.getCodigoProductoField().getText();
            String nombre = view.getNombreProductoField().getText();
            int cantidad = Integer.parseInt(view.getCantidadProductoField().getText());
            double valorUnitario = Double.parseDouble(view.getValorUnitarioField().getText());
            double valorTotal = cantidad * valorUnitario;

            // Obtener las selecciones de los ComboBox
            int categoriaId = view.obtenerIdCategoriaSeleccionada();
            int proveedorId = view.obtenerIdProveedorSeleccionado();

            if (categoriaId == -1) {
                view.mostrarMensajeError("Debe seleccionar una categoría válida.");
                return;
            }
            if (proveedorId == -1) {
                view.mostrarMensajeError("Debe seleccionar un proveedor válido.");
                return;
            }

            // Crear el objeto Producto
            Producto producto = new Producto();
            producto.setCodigo(codigo);
            producto.setNombre(nombre);
            producto.setCantidad(cantidad);
            producto.setValorUnitario(valorUnitario);
            producto.setValorTotal(valorTotal);
            producto.setCategoriaId(categoriaId);
            producto.setProveedorId(proveedorId);
            producto.setImagen(view.getImagenBlob());

            // Guardar el producto y la relación con el proveedor
            productoDAO.guardarProductoConRelacion(producto, proveedorId, valorUnitario);

            JOptionPane.showMessageDialog(view, "Producto guardado con éxito.");
            limpiarFormulario();

        } catch (SQLException ex) {
            view.mostrarMensajeError("Error al guardar el producto: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            view.mostrarMensajeError("Error en los datos ingresados: " + ex.getMessage());
        }
    }

    /**
     * Limpia los datos del formulario y recarga las categorías y proveedores.
     */
    private void limpiarFormulario() {
        view.restaurarTextosPredictivos();
        view.limpiarImagen();
        cargarCategorias();
        cargarProveedores();
    }

    /**
     * Cierra la vista actual.
     */
    private void salir() {
        view.dispose();
    }

    /**
     * Carga las categorías desde el DAO y las envía a la vista.
     */
    public void cargarCategorias() {
        try {
            originalCategorias = productoDAO.obtenerCategorias();
            categoriaMap = productoDAO.getCategoriaMap();

            List<String> nombresCategorias = originalCategorias.stream()
                    .map(Categoria::getNombreCategoria)
                    .toList();

            view.cargarCategorias(nombresCategorias, categoriaMap);
        } catch (SQLException ex) {
            view.mostrarMensajeError("Error al cargar las categorías: " + ex.getMessage());
        }
    }

    /**
     * Carga los proveedores desde el DAO y los envía a la vista.
     */
    public void cargarProveedores() {
        try {
            originalProveedores = productoDAO.obtenerProveedores();
            proveedorMap = productoDAO.getProveedorMap();

            List<String> nombresProveedores = originalProveedores.stream()
                    .map(Proveedor::getNombre)
                    .toList();

            view.cargarProveedores(nombresProveedores, proveedorMap);
        } catch (SQLException ex) {
            view.mostrarMensajeError("Error al cargar los proveedores: " + ex.getMessage());
        }
    }

    /**
     * Filtra las categorías utilizando el método de la vista.
     */
    public void filtrarCategorias(String textoBusqueda) {
        view.filtrarCategorias(view.getjComboBoxCategoria(), textoBusqueda);
    }

    /**
     * Filtra los proveedores utilizando el método de la vista.
     */
    public void filtrarProveedores(String textoBusqueda) {
        view.filtrarProveedores(view.getjComboBoxProveedor(), textoBusqueda);
    }

    /**
     * Devuelve la lista original de categorías cargadas.
     *
     * @return Lista de categorías.
     */
    public List<Categoria> getOriginalCategorias() {
        return originalCategorias;
    }

    /**
     * Devuelve la lista original de proveedores cargados.
     *
     * @return Lista de proveedores.
     */
    public List<Proveedor> getOriginalProveedores() {
        return originalProveedores;
    }

    /**
     * Abre la ventana de creación de categorías.
     */
    public void abrirVentanaCategoria() {
        CrearCategoriaView ventanaCategoria = new CrearCategoriaView();
        ventanaCategoria.setVisible(true);

        ventanaCategoria.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                cargarCategorias();
            }
        });
    }

    /**
     * Abre la ventana de creación de proveedores.
     */
    public void abrirVentanaProveedor() {
        CrearProveedorView ventanaProveedor = new CrearProveedorView();
        ventanaProveedor.setVisible(true);

        ventanaProveedor.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                cargarProveedores();
            }
        });
    }

    public void iniciar() {
        view.setVisible(true); // Hace visible la vista
    }
}
