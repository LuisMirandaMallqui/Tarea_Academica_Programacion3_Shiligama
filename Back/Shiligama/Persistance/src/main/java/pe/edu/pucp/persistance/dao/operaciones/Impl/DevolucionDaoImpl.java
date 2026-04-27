package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.persistance.dao.operaciones.dao.DevolucionDao;
import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.model.operaciones.Devolucion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DevolucionDaoImpl extends DaoImplBase implements DevolucionDao {

    // -------------------------------------------------------------------------
    // DML con CallableStatement + parámetros nombrados
    // -------------------------------------------------------------------------

    @Override
    public int insertar(Devolucion devolucion) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_DEVOLUCION(?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_devolucion_id", Types.INTEGER);
            cs.setInt("_id_producto", devolucion.getIdProducto());
            cs.setInt("_id_trabajador", devolucion.getIdTrabajador());
            cs.setString("_estado_devolucion", devolucion.getEstadoDevolucion());
            cs.setInt("_cantidad", devolucion.getCantidad());
            cs.setString("_motivo", devolucion.getMotivo());
            cs.setTimestamp("_fecha_hora", devolucion.getFechaHora() != null
                    ? Timestamp.valueOf(devolucion.getFechaHora())
                    : Timestamp.valueOf(LocalDateTime.now()));
            cs.executeUpdate();
            devolucion.setIdDevolucion(cs.getInt("_devolucion_id"));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar devolucion: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(Devolucion devolucion) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_DEVOLUCION(?,?,?,?,?,?,?)}");
            cs.setInt("_devolucion_id", devolucion.getIdDevolucion());
            cs.setInt("_producto_id", devolucion.getIdProducto());
            cs.setInt("_trabajador_id", devolucion.getIdTrabajador());
            cs.setString("_estado_devolucion", devolucion.getEstadoDevolucion());
            cs.setInt("_cantidad", devolucion.getCantidad());
            cs.setString("_motivo", devolucion.getMotivo());
            cs.setTimestamp("_fecha_hora", Timestamp.valueOf(devolucion.getFechaHora()));
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar devolucion: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_DEVOLUCION(?)}");
            cs.setInt("_devolucion_id", id);
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar devolucion: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // SELECTs — usan SPs con CallableStatement directamente
    // -------------------------------------------------------------------------

    @Override
    public Devolucion buscarPorID(int id) {
        Devolucion d = null;
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL BUSCAR_DEVOLUCION_POR_ID(?)}");
            cs.setInt("_devolucion_id", id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                d = mapearDevolucion(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorID devolucion: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return d;
    }

    @Override
    public List<Devolucion> listarTodos() {
        List<Devolucion> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_DEVOLUCIONES_TODAS()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearDevolucion(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodos devoluciones: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public List<Devolucion> listarPorFechas(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        List<Devolucion> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_DEVOLUCIONES_POR_FECHAS(?,?)}");
            cs.setTimestamp("_fecha_inicio", Timestamp.valueOf(fechaInicio.atStartOfDay()));
            cs.setTimestamp("_fecha_fin", Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearDevolucion(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarPorFechas devoluciones: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet
    // -------------------------------------------------------------------------

    private Devolucion mapearDevolucion(ResultSet rs) throws SQLException {
        Devolucion d = new Devolucion();
        d.setIdDevolucion(rs.getInt("DEVOLUCION_ID"));
        d.setIdProducto(rs.getInt("PRODUCTO_ID"));
        d.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
        d.setEstadoDevolucion(rs.getString("ESTADO_DEVOLUCION"));
        d.setCantidad(rs.getInt("CANTIDAD"));
        d.setMotivo(rs.getString("MOTIVO"));
        d.setFechaHora(rs.getTimestamp("FECHA_HORA") != null
                ? rs.getTimestamp("FECHA_HORA").toLocalDateTime() : null);
        d.setActivo(rs.getBoolean("ACTIVO"));
        return d;
    }

    // -------------------------------------------------------------------------
    // Métodos abstractos de DaoImplBase (no usados, SELECTs via SPs)
    // -------------------------------------------------------------------------
    @Override
    protected String obtenerSQLParaObtenerPorId() {
        throw new UnsupportedOperationException("Este DAO usa SPs para SELECTs.");
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        throw new UnsupportedOperationException("Este DAO usa SPs para SELECTs.");
    }
}
