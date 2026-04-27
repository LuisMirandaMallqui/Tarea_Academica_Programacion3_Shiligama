package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.venta.PedidoDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class PedidoDaoImpl extends DaoImplBase implements PedidoDao {

    private PedidoDto pedido;

    public PedidoDaoImpl() {
        this.pedido = null;
    }

    public PedidoDaoImpl(PedidoDto pedido) {
        this.pedido = pedido;
    }

    // -------------------------------------------------------------------------
    // Los CRUD importantes
    // -------------------------------------------------------------------------

    @Override
    public int insertar(PedidoDto pedido) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_PEDIDO(?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, pedido.getCliente().getIdCliente());
            cs.setString(3, pedido.getDireccionEntrega());
            cs.setString(4, pedido.getModalidadVenta().name());
            cs.setString(5, pedido.getObservaciones());
            cs.execute();
            pedido.setIdPedido(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar pedido: " + ex.getMessage());
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

    // modificar aquí equivale a cambiar el estado del pedido (RECIBIDO → EN_PROCESO → ATENDIDO, etc.)
    @Override
    public int modificar(PedidoDto pedido) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_ESTADO_PEDIDO(?, ?)}");
            cs.setInt(1, pedido.getIdPedido());
            cs.setString(2, pedido.getEstadoPedido().name());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar estado de pedido: " + ex.getMessage());
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_PEDIDO(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar pedido: " + ex.getMessage());
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

    public PedidoDto buscarPorID(int id) {
        this.pedido = new PedidoDto();
        this.pedido.setIdPedido(id);
        this.obtenerPorId();
        return this.pedido;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT PEDIDO_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, "
                + "DIRECCION_ENTREGA, MODALIDAD_ENTREGA, OBSERVACIONES, CLIENTE_ID "
                + "FROM pedidos "
                + "WHERE PEDIDO_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.pedido.getIdPedido());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.pedido = mapearPedido();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.pedido = null;
    }

    @Override
    public List<PedidoDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT PEDIDO_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, "
                + "DIRECCION_ENTREGA, MODALIDAD_ENTREGA, OBSERVACIONES, CLIENTE_ID "
                + "FROM pedidos "
                + "ORDER BY FECHA_HORA DESC";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearPedido());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para buscarPorID y listarTodos
    // -------------------------------------------------------------------------

    private PedidoDto mapearPedido() throws SQLException {
        PedidoDto p = new PedidoDto();
        p.setIdPedido(resultSet.getInt("PEDIDO_ID"));
        p.setFechaHora(resultSet.getTimestamp("FECHA_HORA").toLocalDateTime());
        p.setMontoTotal(resultSet.getDouble("MONTO_TOTAL"));
        p.setEstadoPedido(EstadoPedido.valueOf(resultSet.getString("ESTADO_PEDIDO")));
        p.setDireccionEntrega(resultSet.getString("DIRECCION_ENTREGA"));
        p.setModalidadVenta(ModalidadVenta.valueOf(resultSet.getString("MODALIDAD_ENTREGA")));
        p.setObservaciones(resultSet.getString("OBSERVACIONES"));

        ClienteDto cliente = new ClienteDto();
        cliente.setIdCliente(resultSet.getInt("CLIENTE_ID"));
        p.setCliente(cliente);

        return p;
    }
}