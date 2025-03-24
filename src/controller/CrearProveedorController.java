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

import dao.ProveedorDAO;
import model.Proveedor;
import view.CrearProveedorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CrearProveedorController {

    private CrearProveedorView vista;
    private ProveedorDAO proveedorDAO;

    // El constructor recibe la vista y la instancia del DAO (ya creada y conectada)
    public CrearProveedorController(CrearProveedorView vista, ProveedorDAO proveedorDAO) {
        this.vista = vista;
        this.proveedorDAO = proveedorDAO;
        initController();
    }

    // Método que asigna los listeners a los botones de la vista
    private void initController() {
        vista.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProveedor();
            }
        });

        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
        vista.getBtnSalir().addActionListener(e -> vista.dispose());
    }

    // Método encargado de validar y guardar el proveedor
    private void guardarProveedor() {
        if (!validarCampos()) {
            return;
        }

        // Recupera datos desde la vista
        String nombre = vista.getNombreProveedor().trim();
        String nit = vista.getNitProveedor().trim();
        String direccion = vista.getDireccionProveedor().trim();
        String telefono = vista.getTelefonoProveedor().trim();

        // Crea el objeto modelo Proveedor
        Proveedor proveedor = new Proveedor(nombre, nit, direccion, telefono);

        try {
            // Llama al DAO para guardar el proveedor en la BD
            proveedorDAO.crearProveedor(proveedor);
            JOptionPane.showMessageDialog(vista, "Proveedor guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vista.limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al guardar el proveedor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Método auxiliar para validar que los campos obligatorios estén llenos y
    // no contengan los textos predictivos
    private boolean validarCampos() {
        String nombre = vista.getNombreProveedor().trim();
        String nit = vista.getNitProveedor().trim();
        String direccion = vista.getDireccionProveedor().trim();
        String telefono = vista.getTelefonoProveedor().trim();

        if (nombre.isEmpty() || nombre.equals("Nombre del Proveedor")) {
            JOptionPane.showMessageDialog(vista, "El nombre del proveedor es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nit.isEmpty() || nit.equals("Nit del Proveedor")) {
            JOptionPane.showMessageDialog(vista, "El NIT del proveedor es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (direccion.isEmpty() || direccion.equals("Direccion del Proveedor")) {
            JOptionPane.showMessageDialog(vista, "La dirección del proveedor es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (telefono.isEmpty() || telefono.equals("Telefono del Proveedor")) {
            JOptionPane.showMessageDialog(vista, "El teléfono del proveedor es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
