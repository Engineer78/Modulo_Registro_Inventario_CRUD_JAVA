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

import java.sql.Blob;

public class Producto {

    private int id;
    private String codigo;
    private String nombre;
    private int cantidad;
    private double valorUnitario;
    private double valorTotal;
    private Blob imagen;
    private int categoriaId; // ID de la categoría
    private String categoriaNombre; // Nombre de la Categoria
    private int proveedorId; // ID del proveedor
    private String proveedorNombre;

    /**
     * Constructor por defecto.
     */
    public Producto() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param id El ID del producto.
     * @param codigo El código del producto.
     * @param nombre El nombre del producto.
     * @param cantidad La cantidad del producto.
     * @param valorUnitario El valor unitario del producto.
     * @param valorTotal El valor total del producto.
     * @param imagen La imagen del producto.
     * @param categoriaId El ID de la categoría del producto.
     * @param proveedorId El ID del proveedor del producto.
     */
    public Producto(int id, String codigo, String nombre, int cantidad, double valorUnitario, double valorTotal, Blob imagen, int categoriaId, int proveedorId) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.imagen = imagen;
        this.categoriaId = categoriaId;
        this.proveedorId = proveedorId;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }
    
    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public int getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(int proveedorId) {
        this.proveedorId = proveedorId;
    }
    
    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
 }
