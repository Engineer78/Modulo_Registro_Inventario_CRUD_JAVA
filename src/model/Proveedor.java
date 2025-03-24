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
 * Representa un proveedor.
 */
public class Proveedor {
    private int id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor con id y nombre
    public Proveedor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor con todos los atributos excepto id
    public Proveedor(String nombre, String nit, String direccion, String telefono) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Constructor con todos los atributos
    public Proveedor(int id, String nombre, String nit, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTexto() {
        return nombre; // Devuelve el nombre del proveedor, que se muestra en la vista
    }
    
    @Override
    public String toString() {
        return nombre; // Representa al proveedor usando el nombre, útil para ComboBoxes
    }
}

