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
import dao.CategoriaDAOImpl;
import model.Categoria;

/**
 * Controlador para la gestión de categorías.
 * Maneja la interacción entre la vista y la capa de persistencia.
 */
public class CategoriaController {
    private CategoriaDAO categoriaDAO;

    /**
     * Constructor de la clase `CategoriaController`.
     * Inicializa la implementación del DAO de categoría.
     */
    public CategoriaController() {
        categoriaDAO = new CategoriaDAOImpl();
    }

    /**
     * Crea una nueva categoría validando el nombre proporcionado.
     *
     * @param nombre El nombre de la categoría a crear.
     * @return `true` si la categoría fue creada correctamente; `false` en caso contrario.
     */
    public boolean crearCategoria(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        // Crear una instancia de categoría y delegar la operación al DAO
        Categoria categoria = new Categoria(nombre.trim());
        return categoriaDAO.guardarCategoria(categoria);
    }
}

