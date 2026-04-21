package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.persistance.daoImpl.DAOImplBase;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDtoDAO;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.model.venta.VentaDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class VentaDtoImpl extends DAOImplBase implements VentaDtoDAO {

    private VentaDto venta;

    public VentaDtoImpl(VentaDto venta) {
        this.venta = venta;
    }

    // -------------------------------------------------------------------------
    // DML
    // -------------------------------------------------------------------------

    @Override
    public int insertar(VentaDto venta) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_VENTA(?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, venta.getCliente().getIdCliente());
            cs.setInt(3, venta.getTrabajador().getIdTrabajador());
            cs.setInt(4, venta.getMetodoPago().getIdMetodoPago());
            cs.setString(5, venta.getCanalVenta().name());
            cs.setString(6, venta.getObservaciones());
            cs.execute();
            venta.setIdVenta(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar venta: " + ex.getMessage());
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

    // En el original, modificar llama a COMPLETAR_VENTA — se respeta esa decisión de negocio.
    @Override
    public int modificar(VentaDto venta) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL COMPLETAR_VENTA(?)}");
            cs.setInt(1, venta.getIdVenta());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al completar venta: " + ex.getMessage());
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

    // En el original, eliminar llama a ANULAR_VENTA — es una baja lógica, se respeta.
    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL ANULAR_VENTA(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al anular venta: " + ex.getMessage());
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
    // SELECT — PreparedStatement a través de los template methods de la base
    // -------------------------------------------------------------------------

    public VentaDto buscarPorID(int id) {
        this.venta = new VentaDto();
        this.venta.setIdVenta(id);
        this.obtenerPorId();
        return this.venta;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT v.VENTA_ID, v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO, "
                + "v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES, "
                + "v.CLIENTE_ID, v.TRABAJADOR_ID, "
                + "mp.METODO_PAGO_ID, mp.NOMBRE AS METODO_PAGO_NOMBRE "
                + "FROM venta v JOIN metodo_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID "
                + "WHERE v.VENTA_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.venta.getIdVenta());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.venta = mapearVenta();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.venta = null;
    }

    @Override
    public List<VentaDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT v.VENTA_ID, v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO, "
                + "v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES, "
                + "v.CLIENTE_ID, v.TRABAJADOR_ID, "
                + "mp.METODO_PAGO_ID, mp.NOMBRE AS METODO_PAGO_NOMBRE "
                + "FROM venta v JOIN metodo_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID "
                + "ORDER BY v.FECHA_HORA DESC";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearVenta());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para no duplicarlo entre buscar y listar
    // -------------------------------------------------------------------------

    private VentaDto mapearVenta() throws SQLException {
        VentaDto v = new VentaDto();
        v.setIdVenta(resultSet.getInt("VENTA_ID"));
        v.setFechaHora(resultSet.getTimestamp("FECHA_HORA").toLocalDateTime());
        v.setMontoTotal(resultSet.getDouble("MONTO_TOTAL"));
        v.setMontoDescuento(resultSet.getDouble("MONTO_DESCUENTO"));
        v.setCanalVenta(CanalVenta.valueOf(resultSet.getString("CANAL_VENTA")));
        v.setEstadoVenta(EstadoVenta.valueOf(resultSet.getString("ESTADO_VENTA")));
        v.setObservaciones(resultSet.getString("OBSERVACIONES"));

        ClienteDto cliente = new ClienteDto();
        cliente.setIdCliente(resultSet.getInt("CLIENTE_ID"));
        v.setCliente(cliente);

        TrabajadorDto trabajador = new TrabajadorDto();
        trabajador.setIdTrabajador(resultSet.getInt("TRABAJADOR_ID"));
        v.setTrabajador(trabajador);

        MetodoPagoDto metodoPago = new MetodoPagoDto();
        metodoPago.setIdMetodoPago(resultSet.getInt("METODO_PAGO_ID"));
        metodoPago.setNombre(resultSet.getString("METODO_PAGO_NOMBRE"));
        v.setMetodoPago(metodoPago);

        return v;
    }
}