package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.venta.dao.DetalleVentaDao;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.venta.DetalleVentaDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class DetalleVentaDaoImpl extends DaoImplBase implements DetalleVentaDao {

    private DetalleVentaDto detalleVenta;

    public DetalleVentaDaoImpl() {
        this.detalleVenta = null;
    }

    public DetalleVentaDaoImpl(DetalleVentaDto detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    // -------------------------------------------------------------------------
    // Metodos CRUD importantes
    // -------------------------------------------------------------------------
    @Override
    public int insertar(DetalleVentaDto detalleVenta) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_DETALLE_VENTA(?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, detalleVenta.getIdPadreVenta());
            cs.setInt(3, detalleVenta.getProducto().getIdProducto());
            cs.setInt(4, detalleVenta.getCantidad());
            cs.execute();
            detalleVenta.setIdDetalleVenta(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar detalle de venta: " + ex.getMessage());
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
    public int modificar(DetalleVentaDto detalleVenta) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_DETALLE_VENTA(?, ?)}");
            cs.setInt(1, detalleVenta.getIdDetalleVenta());
            cs.setInt(2, detalleVenta.getCantidad());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar detalle de venta: " + ex.getMessage());
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_DETALLE_VENTA(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar detalle de venta: " + ex.getMessage());
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
    // Para operaciones SELECT se hace uso de PreparedStatement
    // -------------------------------------------------------------------------
    public DetalleVentaDto buscarPorID(int id) {
        this.detalleVenta = new DetalleVentaDto();
        this.detalleVenta.setIdDetalleVenta(id);
        this.obtenerPorId();
        return this.detalleVenta;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT dv.DETALLE_VENTA_ID, dv.VENTA_ID, dv.CANTIDAD, "
                + "dv.PRECIO_UNITARIO, dv.SUBTOTAL, "
                + "p.PRODUCTO_ID, p.NOMBRE AS PRODUCTO_NOMBRE "
                + "FROM detalles_venta dv JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID "
                + "WHERE dv.DETALLE_VENTA_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.detalleVenta.getIdDetalleVenta());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.detalleVenta = mapearDetalleVenta();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.detalleVenta = null;
    }

    @Override
    public List<DetalleVentaDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT dv.DETALLE_VENTA_ID, dv.VENTA_ID, dv.CANTIDAD, "
                + "dv.PRECIO_UNITARIO, dv.SUBTOTAL, "
                + "p.PRODUCTO_ID, p.NOMBRE AS PRODUCTO_NOMBRE "
                + "FROM detalles_venta dv JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearDetalleVenta());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para buscarPorID y listarTodos
    // -------------------------------------------------------------------------
    private DetalleVentaDto mapearDetalleVenta() throws SQLException {
        DetalleVentaDto detalle = new DetalleVentaDto();
        detalle.setIdDetalleVenta(resultSet.getInt("DETALLE_VENTA_ID"));
        detalle.setIdPadreVenta(resultSet.getInt("VENTA_ID"));
        detalle.setCantidad(resultSet.getInt("CANTIDAD"));
        detalle.setPrecioUnitario(resultSet.getDouble("PRECIO_UNITARIO"));
        detalle.setSubtotal(resultSet.getDouble("SUBTOTAL"));

        ProductoDto producto = new ProductoDto();
        producto.setIdProducto(resultSet.getInt("PRODUCTO_ID"));
        producto.setNombre(resultSet.getString("PRODUCTO_NOMBRE"));
        detalle.setProducto(producto);

        return detalle;
    }
}