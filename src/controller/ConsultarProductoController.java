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

import dao.CategoriaDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import model.Categoria;
import model.Proveedor;
import model.Producto;
import view.ConsultarProductoView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;


public class ConsultarProductoController {

    private ConsultarProductoView view;
    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO;
    private CategoriaDAO categoriaDAO;
    private List<Categoria> originalCategorias;
    private List<Proveedor> originalProveedores;
    private Map<String, Integer> categoriaMap; // Mapa para categorías
    private Map<String, Integer> proveedorMap; // Mapa para proveedores

    private static final String TEXTO_PREDICTIVO_BUSCAR_CÓDIGO = "Buscar Código";
    private static final String TEXTO_PREDICTIVO_BUSCAR_NOMBRE = "Buscar Nombre";
    private boolean isUpdating = false; // Bandera para controlar la actualización

    /**
     * Constructor que inicializa el controlador con la conexión a la base de
     * datos.
     */
    public ConsultarProductoController(Connection connection, ConsultarProductoView view) {
        this.productoDAO = new ProductoDAO(connection);
        this.view = view;

        // Llama a los métodos de carga al inicializar el controlador
        cargarCategorias();
        cargarProveedores();
    }

    public ConsultarProductoView getView() {
        return this.view; // Retorna la instancia de la vista
    }

    /**
     * Asignar la vista al controlador y configura los ActionListeners.
     *
     * @param view La vista principal de creación de producto.
     */
    public void setView(ConsultarProductoView view) {
        this.view = view;
       
        // Si la vista no es null, configura los listeners de los botones
        if (view != null) {
            // Aquí se configuran listeners para los botones
            view.getjButtonLimpiar().addActionListener(e
                    -> limpiarFormulario());
            view.getjButtonSalir().addActionListener(e -> salir());
        }
    }

    /**
     * Constructor del controlador de consulta de productos.
     *
     * @param proveedorDAO Instancia de ProveedorDAO.
     * @param categoriaDAO Instancia de CategoriaDAO.
     */
    public ConsultarProductoController(ProveedorDAO proveedorDAO, CategoriaDAO categoriaDAO) {
        this.proveedorDAO = proveedorDAO;
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Limpia los datos del formulario y recarga las categorías y proveedores.
     */
    private void limpiarFormulario() {
        // Activa una bandera para evitar disparar eventos
        isUpdating = true;

        try {
            // Limpia los textos predictivos y los filtros
            view.getjComboBoxCategoria().setSelectedIndex(0);
            view.getjComboBoxProveedor().setSelectedIndex(0);
            view.getjTextFieldBuscarCodigo().setText(TEXTO_PREDICTIVO_BUSCAR_CÓDIGO);
            view.getjTextFieldBuscarNombre().setText(TEXTO_PREDICTIVO_BUSCAR_NOMBRE);

            // Restaurar textos predictivos en los campos de texto
            view.restaurarTextosPredictivos();

            // Limpia la tabla
            view.limpiarTabla();

            cargarCategorias();
            cargarProveedores();
        } finally {
            // Restablecer la bandera
            isUpdating = false;
        }
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
        // Validación antes del bloque try
        if (this.view == null) {
            System.err.println("Error: La vista no está inicializada.");
            return; // Evita ejecutar el resto del método
        }
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
        // Validación antes del bloque try
        if (this.view == null) {
            System.err.println("Error: La vista no está inicializada.");
            return; // Evita ejecutar el resto del método
        }
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
     * Filtra los productos según los criterios de categoría, proveedor, código
     * o nombre.
     *
     * @param modelo Modelo de la tabla donde se mostrarán los datos.
     * @param filtroCodigo Código del producto (opcional).
     * @param filtroNombre Nombre del producto (opcional).
     * @param categoriaId ID de la categoría (opcional).
     * @param proveedorId ID del proveedor (opcional).
     */
    public void filtrarProductos(DefaultTableModel modelo, String filtroCodigo, String filtroNombre,
            Integer categoriaId, Integer proveedorId) {

        modelo.setRowCount(0); // Limpia la tabla antes de agregar nuevos resultados

        try {
            List<Producto> productos = productoDAO.filtrarProductos(filtroCodigo, filtroNombre, categoriaId, proveedorId);

            if (productos == null || productos.isEmpty()) {
                modelo.setRowCount(0);
                return;
            }

            modelo.setRowCount(0);
            for (Producto producto : productos) {
                modelo.addRow(new Object[]{
                    false,
                    producto.getCodigo(),
                    producto.getCategoriaNombre(),
                    producto.getNombre(),
                    producto.getCantidad(),
                    producto.getValorUnitario(),
                    producto.getValorTotal(),
                    producto.getImagen()
                });
            }
        } catch (SQLException ex) {
            view.mostrarMensajeError("Error al filtrar productos: " + ex.getMessage());
        }
    }

    public void iniciar() {
        //System.out.println("Iniciando vista: Consultar Producto");
        view.setVisible(true); // Hace visible la vista
    }
}
