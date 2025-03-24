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

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para manejar la conexión a la base de datos MySQL.
 */
public class ConexionBD {

    private static final String bd = "inventariocrudbd"; // Nombre de la base de datos.
    private static final String url = "jdbc:mysql://localhost:3306/" + bd + "?useUnicode=true&use" +
            "JDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
            "serverTimezone=UTC"; // URL de conexión a la base de datos.
    private static final String usuario = "root"; // Nombre de usuario de la base de datos.
    private static final String pwd = ""; // Contraseña de la base de datos.
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Nombre del driver JDBC.
    private Connection con = null; // Objeto Connection para la conexión.

    /**
     * Método para establecer la conexión a la base de datos.
     *
     * @return La conexión a la base de datos, o null si falla.
     */
    public Connection getConnection() {
    if (con == null) {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, usuario, pwd);
         
        } catch (ClassNotFoundException | SQLException e) {
           Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    return con; // Devuelve la misma conexión si ya está abierta
}

    /**
     * Método para cerrar la conexión a la base de datos.
     */
    public void closeConnection() {
        if (con != null) {
            try {
                con.close(); // Cierra la conexión.
                con = null; // Establece la conexión a null.
                
            } catch (SQLException e) {
                Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e); // Registra el error.
            }
        }
    }
}