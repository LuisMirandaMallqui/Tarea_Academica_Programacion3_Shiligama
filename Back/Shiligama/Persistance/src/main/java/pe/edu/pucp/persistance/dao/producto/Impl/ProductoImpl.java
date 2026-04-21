package pe.edu.pucp.persistance.dao.producto.Impl;

import pe.edu.pucp.persistance.dao.producto.dao.ProductoDAO;
import pe.edu.pucp.persistance.daoImpl.DAOImplBase;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.producto.CategoriaDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

public class ProductoImpl extends DAOImplBase implements ProductoDAO {

    private ProductoDto producto;

    public ProductoImpl(ProductoDto producto) {
        this.producto = producto;
    }

    @Override
    public int insertar(ProductoDto producto) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{call INSERTAR_PRODUCTO(?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, producto.getCategoria().getIdCategoria());
            cs.setString(3, producto.getNombre());
            cs.setString(4, producto.getDescripcion());
            cs.setDouble(5, producto.getPrecioUnitario());
            cs.setInt(6, producto.getStock());
            cs.setInt(7, producto.getStockMinimo());
            cs.setString(8, producto.getUnidadMedida());
            cs.setString(9, producto.getCodigoBarras());
            cs.setString(10, producto.getImagenUrl());
            cs.executeUpdate();
            producto.setIdProducto(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar producto: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(ProductoDto producto) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{call MODIFICAR_PRODUCTO(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, producto.getIdProducto());
            cs.setInt(2, producto.getCategoria().getIdCategoria());
            cs.setString(3, producto.getNombre());
            cs.setString(4, producto.getDescripcion());
            cs.setDouble(5, producto.getPrecioUnitario());
            cs.setInt(6, producto.getStockMinimo());
            cs.setString(7, producto.getUnidadMedida());
            cs.setString(8, producto.getCodigoBarras());
            cs.setString(9, producto.getImagenUrl());
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar producto: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{call ELIMINAR_PRODUCTO(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar producto: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // SELECT — usan PreparedStatement a través de los template methods de la base
    // -------------------------------------------------------------------------

    // buscarPorID delega en obtenerPorId() de la base, que maneja la conexión,
    // llama a los métodos de abajo y cierra todo al final.
    public ProductoDto buscarPorID(int id) {
        this.producto = new ProductoDto();
        this.producto.setIdProducto(id);
        this.obtenerPorId();
        return this.producto;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT p.PRODUCTO_ID, p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, "
                + "p.STOCK, p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, "
                + "p.IMAGEN_URL, p.ACTIVO, p.FECHA_REGISTRO, "
                + "c.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE "
                + "FROM producto p JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID "
                + "WHERE p.PRODUCTO_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.producto.getIdProducto());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.producto = mapearProducto();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.producto = null;
    }

    // listarTodos() está implementado en la base — solo necesita el SQL y el mapeo por fila.
    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT p.PRODUCTO_ID, p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, "
                + "p.STOCK, p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, "
                + "p.IMAGEN_URL, p.ACTIVO, p.FECHA_REGISTRO, "
                + "c.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE "
                + "FROM producto p JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID "
                + "WHERE p.ACTIVO = 1";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearProducto());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para no duplicar entre buscarPorID y listarTodos
    // -------------------------------------------------------------------------

    private ProductoDto mapearProducto() throws SQLException {
        ProductoDto p = new ProductoDto();
        p.setIdProducto(resultSet.getInt("PRODUCTO_ID"));
        p.setNombre(resultSet.getString("NOMBRE"));
        p.setDescripcion(resultSet.getString("DESCRIPCION"));
        p.setPrecioUnitario(resultSet.getDouble("PRECIO_UNITARIO"));
        p.setStock(resultSet.getInt("STOCK"));
        p.setStockMinimo(resultSet.getInt("STOCK_MINIMO"));
        p.setUnidadMedida(resultSet.getString("UNIDAD_MEDIDA"));
        p.setCodigoBarras(resultSet.getString("CODIGO_BARRAS"));
        p.setImagenUrl(resultSet.getString("IMAGEN_URL"));
        p.setEstado(resultSet.getBoolean("ACTIVO"));

        CategoriaDto categoria = new CategoriaDto();
        categoria.setIdCategoria(resultSet.getInt("CATEGORIA_ID"));
        categoria.setNombre(resultSet.getString("CATEGORIA_NOMBRE"));
        p.setCategoria(categoria);

        Timestamp fechaRegistro = resultSet.getTimestamp("FECHA_REGISTRO");
        if (fechaRegistro != null) {
            p.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }

        return p;
    }
}