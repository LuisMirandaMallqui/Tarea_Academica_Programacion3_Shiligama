package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.persistance.dao.venta.dao.MetodoPagoDao;
import pe.edu.pucp.persistance.daoImpl.DaoImplBase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDaoImpl extends DaoImplBase implements MetodoPagoDao {
    private MetodoPagoDto metodoPago;

    public MetodoPagoDaoImpl() {
        metodoPago = null;
    }

    public MetodoPagoDaoImpl(MetodoPagoDto metodoPago) {
        this.metodoPago = metodoPago;
    }

    // -------------------------------------------------------------------------
    // Metodos CRUD importantes
    // -------------------------------------------------------------------------

    @Override
    public int insertar(MetodoPagoDto metodoPago) {
        int resultado = 0;
        try {
            this.iniciarTransaccion(); // realiza de forma interna getInstance().getConnection() para mantenerlo agnostico a la base de datos;
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_METODO_PAGO(?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, metodoPago.getNombre());
            cs.execute();
            metodoPago.setIdMetodoPago(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar método de pago: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion(); // aca es donde se hace ahora el cs.close y con.close
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(MetodoPagoDto metodoPago) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_METODO_PAGO(?, ?)}");
            cs.setInt(1, metodoPago.getIdMetodoPago());
            cs.setString(2, metodoPago.getNombre());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar método de pago: " + ex.getMessage());
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_METODO_PAGO(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar método de pago: " + ex.getMessage());
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

    public MetodoPagoDto buscarPorID(int id) {
        this.metodoPago = new MetodoPagoDto();
        this.metodoPago.setIdMetodoPago(id);
        this.obtenerPorId();
        return this.metodoPago;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT METODO_PAGO_ID, NOMBRE, ACTIVO "
                + "FROM metodos_pago "
                + "WHERE METODO_PAGO_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.metodoPago.getIdMetodoPago());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.metodoPago = mapearMetodoPago();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.metodoPago = null;
    }

    @Override
    public List<MetodoPagoDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        // Solo los activos tiene sentido mostrar en pantalla al momento de registrar una venta
        return "SELECT METODO_PAGO_ID, NOMBRE, ACTIVO "
                + "FROM metodos_pago "
                + "WHERE ACTIVO = 1";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearMetodoPago());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para buscarPorID y listarTodos
    // -------------------------------------------------------------------------
    private MetodoPagoDto mapearMetodoPago() throws SQLException {
        MetodoPagoDto mp = new MetodoPagoDto();
        mp.setIdMetodoPago(resultSet.getInt("METODO_PAGO_ID"));
        mp.setNombre(resultSet.getString("NOMBRE"));
        mp.setEstado(resultSet.getBoolean("ACTIVO"));
        return mp;
    }
}
