package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.persistance.dao.operaciones.dao.MovimientoInventarioDao;
import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.model.operaciones.MovimientoInventario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDaoImpl extends DaoImplBase implements MovimientoInventarioDao {

    // -------------------------------------------------------------------------
    // DML — solo insertar (log inmutable)
    // -------------------------------------------------------------------------

    @Override
    public int insertar(MovimientoInventario mov) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL REGISTRAR_MOVIMIENTO_INVENTARIO(?,?,?,?,?,?)}");
            cs.registerOutParameter("_movimiento_id", Types.INTEGER);
            cs.setInt("_producto_id", mov.getIdProducto());
            cs.setInt("_trabajador_id", mov.getIdTrabajador());
            cs.setString("_tipo_movimiento", mov.getTipoMovimiento());
            cs.setInt("_cantidad", mov.getCantidad());
            cs.setString("_motivo", mov.getMotivo());
            cs.executeUpdate();
            mov.setIdMovimiento(cs.getInt("_movimiento_id"));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar movimiento inventario: " + ex.getMessage());
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
    public int modificar(MovimientoInventario mov) {
        System.err.println("No aplica para log inmutable");
        return 0;
    }

    @Override
    public int eliminar(int id) {
        System.err.println("No aplica para log inmutable");
        return 0;
    }

    // -------------------------------------------------------------------------
    // SELECTs — usan SPs con CallableStatement directamente
    // -------------------------------------------------------------------------

    @Override
    public MovimientoInventario buscarPorID(int id) {
        MovimientoInventario m = null;
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL BUSCAR_MOVIMIENTO_POR_ID(?)}");
            cs.setInt("_movimiento_id", id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                m = mapearMovimiento(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorID movimiento: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return m;
    }

    @Override
    public List<MovimientoInventario> listarTodos() {
        List<MovimientoInventario> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_MOVIMIENTOS_TODOS()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodos movimientos: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public List<MovimientoInventario> listarPorProducto(int idProducto) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_MOVIMIENTOS_POR_PRODUCTO(?)}");
            cs.setInt("_producto_id", idProducto);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarPorProducto movimientos: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public List<MovimientoInventario> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_MOVIMIENTOS_POR_FECHAS(?,?)}");
            cs.setTimestamp("_fecha_inicio", Timestamp.valueOf(fechaInicio));
            cs.setTimestamp("_fecha_fin", Timestamp.valueOf(fechaFin));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarPorFechas movimientos: " + ex.getMessage());
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

    private MovimientoInventario mapearMovimiento(ResultSet rs) throws SQLException {
        MovimientoInventario m = new MovimientoInventario();
        m.setIdMovimiento(rs.getInt("MOVIMIENTO_ID"));
        m.setIdProducto(rs.getInt("PRODUCTO_ID"));
        m.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
        m.setTipoMovimiento(rs.getString("TIPO_MOVIMIENTO"));
        m.setCantidad(rs.getInt("CANTIDAD"));
        m.setStockAnterior(rs.getInt("STOCK_ANTERIOR"));
        m.setStockResultante(rs.getInt("STOCK_RESULTANTE"));
        m.setMotivo(rs.getString("MOTIVO"));
        m.setFechaHora(rs.getTimestamp("FECHA_HORA") != null
                ? rs.getTimestamp("FECHA_HORA").toLocalDateTime() : null);
        return m;
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
