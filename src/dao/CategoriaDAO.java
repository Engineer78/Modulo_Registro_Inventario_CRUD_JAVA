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

package dao;

import model.Categoria;

/**
 * Interfaz que define los métodos para la gestión de categorías en la base de datos.
 */
public interface CategoriaDAO {

    /**
     * Guarda una nueva categoría en la base de datos.
     *
     * @param categoria La instancia de la categoría a guardar.
     * @return `true` si la operación fue exitosa; `false` en caso contrario.
     */
    boolean guardarCategoria(Categoria categoria);
}