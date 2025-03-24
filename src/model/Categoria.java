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

/**
 * Modelo que representa una categoría.
 */
public class Categoria {
    private int idCategoria; // ID de la categoría
    private String nombreCategoria; // Nombre de la categoría

    /**
     * Constructor completo.
     *
     * @param idCategoria    El ID de la categoría.
     * @param nombreCategoria El nombre de la categoría.
     */
    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    /**
     * Constructor sin ID, útil para nuevas categorías.
     *
     * @param nombreCategoria El nombre de la categoría.
     */
    public Categoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    // Getters y setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public String getTexto() {
        return nombreCategoria;
    }
}


