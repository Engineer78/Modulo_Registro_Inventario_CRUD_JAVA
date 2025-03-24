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

package model;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Clase que extiende DefaultTableModel y proporciona una implementación de paginación
 * para manejar grandes conjuntos de datos en una tabla.
 */
public class PaginacionTabla extends DefaultTableModel {

    // Lista de datos originales sin paginación
    private final List<Object[]> datosOriginales;
    // Página actual que se está visualizando
    private int paginaActual = 1;
    // Número de filas que se muestran por página
    private final int filasPorPagina = 15;

    /**
     * Constructor que inicializa el modelo de la tabla y aplica la paginación.
     *
     * @param datos Lista de arreglos de objetos con los datos a mostrar en la tabla.
     * @param columnas Arreglo de cadenas que representa los nombres de las columnas de la tabla.
     */
    public PaginacionTabla(List<Object[]> datos, String[] columnas) {
        super(columnas, 0); // Inicializa el modelo con las columnas y sin filas
        this.datosOriginales = datos; // Almacena los datos originales
        actualizarDatos(); // Aplica la paginación para mostrar los datos de la primera página
    }

    /**
     * Obtiene el número de la página actual que se está visualizando en la tabla.
     *
     * @return Número de la página actual.
     */
    public int getPaginaActual() {
        return this.paginaActual;
    }

    /**
     * Establece la página actual y actualiza los datos que se muestran en la tabla.
     *
     * @param paginaActual Número de la página a establecer.
     */
    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual; // Actualiza la página actual
        actualizarDatos(); // Refresca los datos en la tabla según la nueva página
    }

    /**
     * Calcula el total de páginas disponibles según la cantidad de filas por página y
     * el tamaño de los datos originales.
     *
     * @return Número total de páginas.
     */
    public int getPaginasTotales() {
        // Divide el tamaño total de los datos por el número de filas por página y redondea hacia arriba
        return (int) Math.ceil((double) datosOriginales.size() / filasPorPagina);
    }

    /**
     * Actualiza los datos de la tabla según la página actual.
     */
    private void actualizarDatos() {
        // Calcula el índice de inicio de los datos para la página actual
        int inicio = (paginaActual - 1) * filasPorPagina;
        // Calcula el índice de fin de los datos para la página actual
        int fin = Math.min(inicio + filasPorPagina, datosOriginales.size());

        setRowCount(0); // Limpia las filas actuales de la tabla
        // Agrega las filas correspondientes a la página actual
        for (int i = inicio; i < fin; i++) {
            addRow(datosOriginales.get(i)); // Agrega cada fila desde la lista de datos originales
        }
    }
}

