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
import util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementación de la interfaz `CategoriaDAO`.
 * Proporciona la lógica para interactuar con la base de datos.
 */
public class CategoriaDAOImpl implements CategoriaDAO {
    private ConexionBD conexionBD;

    /**
     * Constructor de la clase `CategoriaDAOImpl`.
     * Inicializa la conexión con la base de datos.
     */
    public CategoriaDAOImpl() {
        conexionBD = new ConexionBD();
    }

    /**
     * Guarda una categoría en la base de datos.
     *
     * @param categoria La instancia de la categoría a guardar.
     * @return `true` si la operación fue exitosa; `false` en caso contrario.
     */
    @Override
    public boolean guardarCategoria(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre_categoria) VALUES (?)";
        try (Connection connection = conexionBD.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establece el valor del nombre de la categoría en el query
            pstmt.setString(1, categoria.getNombreCategoria());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            return false;
        }
    }
}

