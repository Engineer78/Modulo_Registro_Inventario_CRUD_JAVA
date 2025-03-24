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
import model.Producto;
import model.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data Access Object para la entidad Producto. Gestiona las operaciones sobre
 * las tablas producto, categoria y proveedor.
 */
public class ProductoDAO {

    private final Connection connection;

    /**
     * Constructor de ProductoDAO. Recibe la conexión a la base de datos.
     *
     * @param connection La conexión a la base de datos.
     */
    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Guarda un producto en la base de datos y su relación con un proveedor en
     * la tabla producto_proveedor. Maneja la transacción completa.
     *
     * @param producto El producto a guardar.
     * @param idProveedor El ID del proveedor asociado.
     * @param precioCompra El precio de compra del producto.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public void guardarProductoConRelacion(Producto producto, int idProveedor, double precioCompra) throws SQLException {
       
        String queryProducto = "INSERT INTO producto (codigo_producto, nombre_producto, cantidad_producto, valorUnitario_producto, valorTotal_producto, imagen, categoria_id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String queryProductoProveedor = "INSERT INTO producto_proveedor (precio_compra, producto_id_producto, proveedor_id_proveedor) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Inicia la transacción

            // Inserción en la tabla producto
            try (PreparedStatement stmtProducto = connection.prepareStatement(queryProducto, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtProducto.setString(1, producto.getCodigo());
                stmtProducto.setString(2, producto.getNombre());
                stmtProducto.setInt(3, producto.getCantidad());
                stmtProducto.setDouble(4, producto.getValorUnitario());
                stmtProducto.setDouble(5, producto.getValorTotal());
                if (producto.getImagen() == null) {
                    // Asignar null o lanzar un error, según la lógica 
                    stmtProducto.setNull(6, java.sql.Types.BLOB);
                } else {
                    
                    stmtProducto.setBlob(6, producto.getImagen());
                }
                stmtProducto.setInt(7, producto.getCategoriaId());

                stmtProducto.executeUpdate();

                // Obtener el ID autogenerado del producto
                ResultSet generatedKeys = stmtProducto.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idProducto = generatedKeys.getInt(1);

                    // Inserción en la tabla producto_proveedor
                    try (PreparedStatement stmtRelacion = connection.prepareStatement(queryProductoProveedor)) {
                        stmtRelacion.setDouble(1, precioCompra);
                        stmtRelacion.setInt(2, idProducto);
                        stmtRelacion.setInt(3, idProveedor);
                        stmtRelacion.executeUpdate();
                    }
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el producto.");
                }
            }

            connection.commit(); // Confirma la transacción
        } catch (SQLException e) {
            connection.rollback(); // Revierte la transacción si ocurre un error
            throw e;
        } finally {
            connection.setAutoCommit(true); // Restaura el comportamiento predeterminado
        }
    }

    /**
     * Verificar si el código de producto ya existe en la base de datos.
     *
     * @param codigo El código de producto a verificar.
     * @return true si el código ya existe, false de lo contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public boolean codigoProductoExiste(String codigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM producto WHERE codigo_producto = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean existe = rs.getInt(1) > 0;
                   
                    return existe;
                }
                return false;
            }
        }
    }

    /**
     * Verificar si el nombre de la categoría ya existe en la base de datos.
     *
     * @param nombreCategoria El nombre de la categoría a verificar.
     * @return true si el nombre ya existe, false de lo contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public boolean nombreCategoriaExiste(String nombreCategoria) throws SQLException {
        // Validación nombreCategoria
        if (nombreCategoria == null || nombreCategoria.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío.");
        }

        String sql = "SELECT COUNT(*) FROM categoria WHERE nombre_categoria = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombreCategoria);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean existe = rs.getInt(1) > 0;
                    return existe;
                }
                return false;
            }
        }
    }

    /**
     * Obtiene todas las categorías de la base de datos.
     *
     * @return Una lista de objetos Categoria.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public List<Categoria> obtenerCategorias() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String query = "SELECT id_categoria, nombre_categoria FROM categoria";

        try (PreparedStatement pstmt = connection.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre_categoria")));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las categorías: " + e.getMessage());
            throw e;
        }

        return categorias;
    }

    /**
     * Obtiene todos los proveedores de la base de datos.
     *
     * @return Una lista de objetos Proveedor.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public List<Proveedor> obtenerProveedores() throws SQLException {
        List<Proveedor> proveedores = new ArrayList<>();
        String query = "SELECT id_proveedor, nombre_proveedor FROM proveedor";

        try (PreparedStatement pstmt = connection.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                proveedores.add(new Proveedor(rs.getInt("id_proveedor"), rs.getString("nombre_proveedor")));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los proveedores: " + e.getMessage());
            throw e;
        }

        return proveedores;
    }

    /**
     * Obtiene un mapa que relaciona los nombres de las categorías con sus IDs.
     *
     * @return Un mapa (nombre -> ID) de categorías.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public Map<String, Integer> getCategoriaMap() throws SQLException {
        Map<String, Integer> categoriaMap = new HashMap<>();
        String query = "SELECT id_categoria, nombre_categoria FROM categoria";

        try (PreparedStatement pstmt = connection.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categoriaMap.put(rs.getString("nombre_categoria"), rs.getInt("id_categoria"));
            }
        } catch (SQLException e) {
            System.err.println("Error al generar el mapa de categorías: " + e.getMessage());
            throw e;
        }
        return categoriaMap;
    }

    /**
     * Obtiene un mapa que relaciona los nombres de los proveedores con sus IDs.
     *
     * @return Un mapa (nombre -> ID) de proveedores.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public Map<String, Integer> getProveedorMap() throws SQLException {
        Map<String, Integer> proveedorMap = new HashMap<>();
        String query = "SELECT id_proveedor, nombre_proveedor FROM proveedor";

        try (PreparedStatement pstmt = connection.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                proveedorMap.put(rs.getString("nombre_proveedor"), rs.getInt("id_proveedor"));
            }
        } catch (SQLException e) {
            System.err.println("Error al generar el mapa de proveedores: " + e.getMessage());
            throw e;
        }

        // Depuración: imprime el mapa completo
        return proveedorMap;
    }

    /**
     * Obtiene productos filtrados por categoría y/o proveedor.
     *
     * @param categoriaId ID de la categoría (opcional).
     * @param proveedorId ID del proveedor (opcional).
     * @return Lista de productos que cumplen con los filtros.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public List<Producto> obtenerProductosFiltrados(Integer categoriaId, Integer proveedorId) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
        SELECT p.codigo_producto, c.nombre_categoria, p.nombre_producto, p.cantidad_producto,
               p.valorUnitario_producto, p.valorTotal_producto, p.imagen
        FROM producto p
        JOIN categorias c ON p.id_categoria = c.id_categoria
        JOIN producto_proveedor pp ON p.codigo_producto = pp.producto_id_producto
    """);

        // Agregar filtros dinámicamente
        List<Object> parametros = new ArrayList<>();
        if (categoriaId != null) {
            query.append(" WHERE c.id_categoria = ?");
            parametros.add(categoriaId);
        }
        if (proveedorId != null) {
            query.append(categoriaId != null ? " AND" : " WHERE");
            query.append(" pp.proveedor_id_proveedor = ?");
            parametros.add(proveedorId);
        }

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setCodigo(rs.getString("codigo_producto"));
                    producto.setCategoriaNombre(rs.getString("nombre_categoria"));
                    producto.setNombre(rs.getString("nombre_producto"));
                    producto.setCantidad(rs.getInt("cantidad_producto"));
                    producto.setValorUnitario(rs.getDouble("valorUnitario_producto"));
                    producto.setValorTotal(rs.getDouble("valorTotal_producto"));
                    producto.setImagen(rs.getBlob("imagen"));
                    productos.add(producto);
                }
            }
        }

        return productos;
    }

    /**
     * Filtra los productos por código y/o nombre.
     *
     * @param filtroCodigo Código del producto (opcional, puede ser null o
     * vacío).
     * @param filtroNombre Nombre del producto (opcional, puede ser null o
     * vacío).
     * @return Lista de productos que cumplen los filtros.
     * @throws SQLException En caso de error en la consulta.
     */
    public List<Producto> filtrarProductos(String filtroCodigo, String filtroNombre, Integer categoriaId, Integer proveedorId) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
        SELECT p.codigo_producto, c.nombre_categoria, p.nombre_producto, p.cantidad_producto,
               p.valorUnitario_producto, p.valorTotal_producto, p.imagen
        FROM producto p
        JOIN categoria c ON p.categoria_id_categoria = c.id_categoria
    """);

        // Añadir JOIN y filtro de proveedor si está presente
        if (proveedorId != null) {
            query.append(" INNER JOIN producto_proveedor pp ON p.id_producto = pp.producto_id_producto AND pp.proveedor_id_proveedor = ?");
        } else {
            query.append(" LEFT JOIN producto_proveedor pp ON p.id_producto = pp.producto_id_producto");
        }

        query.append(" WHERE (p.codigo_producto LIKE ? OR ? IS NULL OR ? = '')")
                .append("   AND (p.nombre_producto LIKE ? OR ? IS NULL OR ? = '')")
                .append("   AND (c.id_categoria = ? OR ? IS NULL)");

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            int parameterIndex = 1; // Inicializa el índice del parámetro

            // Filtro para el proveedor (si está presente)
            if (proveedorId != null) {
                ps.setObject(parameterIndex++, proveedorId);
            }

            // Filtros para el código del producto
            ps.setString(parameterIndex++, filtroCodigo != null && !filtroCodigo.isEmpty() ? "%" + filtroCodigo + "%" : null);
            ps.setString(parameterIndex++, filtroCodigo);
            ps.setString(parameterIndex++, filtroCodigo);

            // Filtros para el nombre del producto
            ps.setString(parameterIndex++, filtroNombre != null && !filtroNombre.isEmpty() ? "%" + filtroNombre + "%" : null);
            ps.setString(parameterIndex++, filtroNombre);
            ps.setString(parameterIndex++, filtroNombre);

            // Filtro para la categoría (opcional)
            ps.setObject(parameterIndex++, categoriaId);
            ps.setObject(parameterIndex++, categoriaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto();
                    producto.setCodigo(rs.getString("codigo_producto"));
                    producto.setCategoriaNombre(rs.getString("nombre_categoria"));
                    producto.setNombre(rs.getString("nombre_producto"));
                    producto.setCantidad(rs.getInt("cantidad_producto"));
                    producto.setValorUnitario(rs.getDouble("valorUnitario_producto"));
                    producto.setValorTotal(rs.getDouble("valorTotal_producto"));
                    producto.setImagen(rs.getBlob("imagen"));
                    productos.add(producto);
                }
            }
        }
        return productos;
    }

    public List<Producto> filtrarProductosPorProveedor(String proveedor) throws SQLException {
        String sql = "SELECT * FROM productos WHERE proveedor = ?";
        List<Producto> productos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, proveedor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCategoriaNombre(rs.getString("categoria"));
                producto.setProveedorNombre(rs.getString("proveedor")); // Usando el nuevo método
                producto.setCantidad(rs.getInt("cantidad"));
                productos.add(producto);
            }
        }
        return productos;
    }

    public List<Producto> filtrarProductosPorCategoria(String categoria) throws SQLException {
        // Consulta SQL para filtrar productos por categoría
        String sql = "SELECT * FROM producto WHERE nombre_categoria = ?";
        List<Producto> productos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Pasar el valor de la categoría al parámetro de la consulta
            stmt.setString(1, categoria);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crear un objeto Producto y llenarlo con los datos de la base de datos
                    Producto producto = new Producto();
                    producto.setCodigo(rs.getString("codigo_producto"));
                    producto.setNombre(rs.getString("nombre_producto"));
                    producto.setCategoriaNombre(rs.getString("nombre_categoria"));
                    producto.setCantidad(rs.getInt("cantidad_producto"));
                    producto.setValorUnitario(rs.getDouble("valor_unitario"));
                    producto.setValorTotal(rs.getDouble("valor_total"));

                    // Agregar el producto a la lista
                    productos.add(producto);
                }
            }
        }

        return productos; // Devolver la lista de productos filtrados
    }

    public List<Producto> obtenerTodosLosProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String query = """
    SELECT p.codigo_producto, c.nombre_categoria, p.nombre_producto, p.cantidad_producto,
           p.valorUnitario_producto, p.valorTotal_producto, p.imagen
    FROM producto p
    JOIN categoria c ON p.categoria_id_categoria = c.id_categoria
    """;

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setCodigo(rs.getString("codigo_producto"));
                producto.setCategoriaNombre(rs.getString("nombre_categoria"));
                producto.setNombre(rs.getString("nombre_producto"));
                producto.setCantidad(rs.getInt("cantidad_producto"));
                producto.setValorUnitario(rs.getDouble("valorUnitario_producto"));
                producto.setValorTotal(rs.getDouble("valorTotal_producto"));
                producto.setImagen(rs.getBlob("imagen"));
                productos.add(producto);
            }
        }

        return productos;
    }
}
