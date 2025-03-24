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

import model.Proveedor;
import java.sql.SQLException;
import java.util.List; // Importa List para el método obtenerTodosProveedores

public interface ProveedorDAO {
    void crearProveedor(Proveedor proveedor) throws SQLException;
    Proveedor obtenerProveedorPorId(int id) throws SQLException;
    List<Proveedor> obtenerTodosProveedores() throws SQLException;
    void actualizarProveedor(Proveedor proveedor) throws SQLException;
    void eliminarProveedor(int id) throws SQLException;
}
