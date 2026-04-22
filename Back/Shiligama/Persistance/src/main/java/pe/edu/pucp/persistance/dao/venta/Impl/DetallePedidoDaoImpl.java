package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.venta.dao.DetallePedidoDao;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.venta.DetallePedidoDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class DetallePedidoDaoImpl extends DaoImplBase implements DetallePedidoDao {

    private DetallePedidoDto detallePedido;

    public DetallePedidoDaoImpl() {
        this.detallePedido = null;
    }

    public DetallePedidoDaoImpl(DetallePedidoDto detallePedido) {
        this.detallePedido = detallePedido;
    }

    // -------------------------------------------------------------------------
    // Metodos CRUD importantes
    // -------------------------------------------------------------------------

    @Override
    public int insertar(DetallePedidoDto detallePedido) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_DETALLE_PEDIDO(?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, detallePedido.getIdPadrePedido());
            cs.setInt(3, detallePedido.getProducto().getIdProducto());
            cs.setInt(4, detallePedido.getCantidad());
            cs.execute();
            detallePedido.setIdDetallePedido(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar detalle de pedido: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(DetallePedidoDto detallePedido) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_DETALLE_PEDIDO(?, ?)}");
            cs.setInt(1, detallePedido.getIdDetallePedido());
            cs.setInt(2, detallePedido.getCantidad());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar detalle de pedido: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_DETALLE_PEDIDO(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar detalle de pedido: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Para operaciones SELECT se hace uso de PreparedStatement
    // -------------------------------------------------------------------------

    public DetallePedidoDto buscarPorID(int id) {
        this.detallePedido = new DetallePedidoDto();
        this.detallePedido.setIdDetallePedido(id);
        this.obtenerPorId();
        return this.detallePedido;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT dp.DETALLE_PEDIDO_ID, dp.PEDIDO_ID, dp.CANTIDAD, "
                + "dp.PRECIO_UNITARIO, dp.SUBTOTAL, "
                + "p.PRODUCTO_ID, p.NOMBRE AS PRODUCTO_NOMBRE "
                + "FROM detalles_pedido dp JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID "
                + "WHERE dp.DETALLE_PEDIDO_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.detallePedido.getIdDetallePedido());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.detallePedido = mapearDetallePedido();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.detallePedido = null;
    }

    @Override
    public List<DetallePedidoDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT dp.DETALLE_PEDIDO_ID, dp.PEDIDO_ID, dp.CANTIDAD, "
                + "dp.PRECIO_UNITARIO, dp.SUBTOTAL, "
                + "p.PRODUCTO_ID, p.NOMBRE AS PRODUCTO_NOMBRE "
                + "FROM detalles_pedido dp JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearDetallePedido());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para buscarPorID y listarTodos
    // -------------------------------------------------------------------------

    private DetallePedidoDto mapearDetallePedido() throws SQLException {
        DetallePedidoDto detalle = new DetallePedidoDto();
        detalle.setIdDetallePedido(resultSet.getInt("DETALLE_PEDIDO_ID"));
        detalle.setIdPadrePedido(resultSet.getInt("PEDIDO_ID"));
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