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

package InventarioCRUD;

import controller.CrearProductoController;
import controller.ConsultarProductoController;

import view.CrearProductoView;
import view.ConsultarProductoView;


import util.ConexionBD;

import javax.swing.SwingUtilities;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Instancia de la clase ConexionBD
            ConexionBD conexionBD = new ConexionBD();
            Connection connection = conexionBD.getConnection(); // Conexión a la base de datos

            if (connection != null) {
                // **PASO 1: Crear la Vista de ConsultarProducto**
                ConsultarProductoView consultarView = new ConsultarProductoView(null);
                
                // **PASO 2: Crear el Controlador de ConsultarProducto**
                ConsultarProductoController consultarController = new ConsultarProductoController(connection, consultarView);
                consultarView.setController(consultarController); //Asocia el controlador a la vista
                
                consultarView.setController(consultarController);
                
                // **PASO 3: (continuación): Asignar la vista al controlador **
                consultarController.setView(consultarView);

                // **PASO 4: Crear el Controlador de CrearProducto**
                CrearProductoController crearController = new CrearProductoController(connection);

                // **PASO 5: Crear la Vista de CrearProducto**
                CrearProductoView crearView = new CrearProductoView(crearController);
                crearController.setView(crearView);

                // **PASO 6: Configurar navegación entre vistas**
                configurarNavegacion(crearController, consultarController);

                // **PASO 7: Inicializar datos en CrearProducto**
                crearController.cargarCategorias();
                crearController.cargarProveedores();

                // **PASO 8: Iniciar la aplicación con CrearProductoView**
                crearController.iniciar();
            } else {
                System.err.println("No se pudo establecer la conexión a la base de datos. Por favor, verifica tu configuración.");
            }
        });
    }

    private static void configurarNavegacion(
            CrearProductoController crearController,
            ConsultarProductoController consultarController
      ) {
        // Botón en CrearProductoView para ir a ConsultarProductoView
        crearController.getView().getjToggleButtonConsultar().addActionListener(e -> {
            consultarController.iniciar();
            crearController.getView().getjToggleButtonConsultar().setSelected(false);
            crearController.getView().dispose();
        });
        // Botón en ConsultarProductoView para regresar a CrearProductoView
        consultarController.getView().getjToggleButtonRegistrar().addActionListener(e -> {
            crearController.cargarCategorias(); // Recarga las categorías actualizadas
            crearController.getView().getjToggleButtonConsultar().setSelected(false);
            crearController.iniciar(); // Muestra la vista de Consultar Producto
            consultarController.getView().dispose(); // Cierra la vista actual
        });

    }
}
