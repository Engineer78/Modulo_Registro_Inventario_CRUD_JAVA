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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz ProveedorDAO para gestionar 
 * las operaciones CRUD de la tabla "proveedor".
 */
public class ProveedorDAOImpl implements ProveedorDAO {

    // Conexión a la base de datos
    private Connection conexion;

    /**
     * Constructor de la clase que inicializa la conexión.
     *
     * @param conexion la conexión activa a la base de datos
     */
    public ProveedorDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Inserta un nuevo proveedor en la base de datos.
     *
     * @param proveedor el proveedor a crear
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    @Override
    public void crearProveedor(Proveedor proveedor) throws SQLException {
        // Sentencia SQL para insertar un nuevo proveedor
        String sql = "INSERT INTO proveedor (nombre_proveedor, nit_proveedor, telefono_proveedor, direccion_proveedor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Configuración de los parámetros de la consulta
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getNit());
            pstmt.setString(4, proveedor.getDireccion()); // Dirección mal colocada (debería ser 3)
            pstmt.setString(3, proveedor.getTelefono());
            pstmt.executeUpdate(); // Ejecución de la consulta
        }
    }

    /**
     * Obtiene un proveedor específico basado en su ID.
     *
     * @param id el ID del proveedor a buscar
     * @return el proveedor encontrado o null si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public Proveedor obtenerProveedorPorId(int id) throws SQLException {
        // Sentencia SQL para obtener un proveedor por ID
        String sql = "SELECT id_proveedor, nombre_proveedor, nit_proveedor, direccion_proveedor, telefono_proveedor FROM proveedor WHERE id_proveedor = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id); // Configuración del parámetro ID
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Creación de un objeto Proveedor a partir del resultado
                    return new Proveedor(
                            rs.getInt("id_proveedor"),
                            rs.getString("nombre_proveedor"),
                            rs.getString("nit_proveedor"),
                            rs.getString("direccion_proveedor"),
                            rs.getString("telefono_proveedor")
                    );
                }
            }
        }
        return null; // Retorna null si no encuentra el proveedor
    }

    /**
     * Obtiene una lista con todos los proveedores registrados en la base de datos.
     *
     * @return lista de proveedores
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public List<Proveedor> obtenerTodosProveedores() throws SQLException {
        List<Proveedor> lista = new ArrayList<>(); // Lista para almacenar los proveedores
        // Sentencia SQL para obtener todos los proveedores
        String sql = "SELECT id_proveedor, nombre_proveedor, nit_proveedor, direccion_proveedor, telefono_proveedor FROM proveedor";
        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Creación de un objeto Proveedor por cada fila
                Proveedor proveedor = new Proveedor(
                        rs.getInt("id_proveedor"),
                        rs.getString("nombre_proveedor"),
                        rs.getString("nit_proveedor"),
                        rs.getString("direccion_proveedor"),
                        rs.getString("telefono_proveedor")
                );
                lista.add(proveedor); // Agregar a la lista
            }
        }
        return lista; // Retorna la lista completa
    }

    /**
     * Actualiza los datos de un proveedor existente en la base de datos.
     *
     * @param proveedor el proveedor con los datos actualizados
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public void actualizarProveedor(Proveedor proveedor) throws SQLException {
        // Sentencia SQL para actualizar un proveedor
        String sql = "UPDATE proveedor SET nombre_proveedor = ?, nit_proveedor = ?, direccion_proveedor = ?, telefono_proveedor = ? WHERE id_proveedor = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            // Configuración de los parámetros de la consulta
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setInt(5, proveedor.getId());
            stmt.executeUpdate(); // Ejecución de la consulta
        }
    }

    /**
     * Elimina un proveedor de la base de datos basado en su ID.
     *
     * @param id el ID del proveedor a eliminar
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public void eliminarProveedor(int id) throws SQLException {
        // Sentencia SQL para eliminar un proveedor
        String sql = "DELETE FROM proveedor WHERE id_proveedor = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id); // Configuración del parámetro ID
            pstmt.executeUpdate(); // Ejecución de la consulta
        }
    }
}

