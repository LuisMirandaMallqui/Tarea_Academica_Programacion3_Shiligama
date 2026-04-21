package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.persistance.dao.operaciones.dao.MovimientoInventarioDAO;
import pe.edu.pucp.model.operaciones.MovimientoInventario;
import pe.edu.pucp.db.DBManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad MovimientoInventario.
 */
public class MovimientoInventarioImpl implements MovimientoInventarioDAO {
    // ================= RECURSOS JDBC =================
    private Connection con;
    private CallableStatement cs;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    @Override
    public int insertar(MovimientoInventario mov) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call REGISTRAR_MOVIMIENTO_INVENTARIO(?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER); // ID devuelto
            cs.setInt(2, mov.getIdProducto());
            cs.setInt(3, mov.getIdTrabajador());
            cs.setString(4, mov.getTipoMovimiento());
            cs.setInt(5, mov.getCantidad());
            cs.setInt(6, mov.getStockAnterior());
            cs.setInt(7, mov.getStockResultante());
            cs.setString(8, mov.getMotivo());
            cs.setTimestamp(9, mov.getFechaHora() != null ? 
                               Timestamp.valueOf(mov.getFechaHora()) : 
                               Timestamp.valueOf(LocalDateTime.now()));
            cs.setInt(10, mov.getUsuarioCreacion());
            cs.executeUpdate();
            mov.setIdMovimiento(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar MovimientoInventario: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
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

    @Override
    public MovimientoInventario buscarPorID(int id) {
        MovimientoInventario m = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_MOVIMIENTO_POR_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                m = new MovimientoInventario();
                m.setIdMovimiento(rs.getInt("id_movimiento"));
                m.setIdProducto(rs.getInt("id_producto"));
                m.setIdTrabajador(rs.getInt("id_trabajador"));
                m.setTipoMovimiento(rs.getString("tipo_movimiento"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setStockAnterior(rs.getInt("stock_anterior"));
                m.setStockResultante(rs.getInt("stock_resultante"));
                m.setMotivo(rs.getString("motivo"));
                m.setFechaHora(rs.getTimestamp("fecha_hora") != null ? 
                                     rs.getTimestamp("fecha_hora").toLocalDateTime() : null);
                m.setUsuarioCreacion(rs.getInt("usuario_creacion"));
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorID MovimientoInventario: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return m;
    }

    @Override
    public List<MovimientoInventario> listarTodos() {
        List<MovimientoInventario> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_MOVIMIENTOS_TODOS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                MovimientoInventario m = new MovimientoInventario();
                m.setIdMovimiento(rs.getInt("id_movimiento"));
                m.setIdProducto(rs.getInt("id_producto"));
                m.setIdTrabajador(rs.getInt("id_trabajador"));
                m.setTipoMovimiento(rs.getString("tipo_movimiento"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setStockAnterior(rs.getInt("stock_anterior"));
                m.setStockResultante(rs.getInt("stock_resultante"));
                m.setMotivo(rs.getString("motivo"));
                m.setFechaHora(rs.getTimestamp("fecha_hora") != null ? 
                                     rs.getTimestamp("fecha_hora").toLocalDateTime() : null);
                m.setUsuarioCreacion(rs.getInt("usuario_creacion"));
                lista.add(m);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarTodos MovimientoInventario: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    @Override
    public List<MovimientoInventario> listarPorProducto(int idProducto) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_MOVIMIENTOS_POR_PRODUCTO(?)}");
            cs.setInt(1, idProducto);
            rs = cs.executeQuery();
            while (rs.next()) {
                MovimientoInventario m = new MovimientoInventario();
                m.setIdMovimiento(rs.getInt("id_movimiento"));
                m.setIdProducto(rs.getInt("id_producto"));
                m.setIdTrabajador(rs.getInt("id_trabajador"));
                m.setTipoMovimiento(rs.getString("tipo_movimiento"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setStockAnterior(rs.getInt("stock_anterior"));
                m.setStockResultante(rs.getInt("stock_resultante"));
                m.setMotivo(rs.getString("motivo"));
                m.setFechaHora(rs.getTimestamp("fecha_hora") != null ? 
                                     rs.getTimestamp("fecha_hora").toLocalDateTime() : null);
                m.setUsuarioCreacion(rs.getInt("usuario_creacion"));
                lista.add(m);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarPorProducto MovimientoInventario: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    @Override
    public List<MovimientoInventario> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<MovimientoInventario> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_MOVIMIENTOS_POR_FECHAS(?,?)}");
            cs.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            cs.setTimestamp(2, Timestamp.valueOf(fechaFin));
            rs = cs.executeQuery();
            while (rs.next()) {
                MovimientoInventario m = new MovimientoInventario();
                m.setIdMovimiento(rs.getInt("id_movimiento"));
                m.setIdProducto(rs.getInt("id_producto"));
                m.setIdTrabajador(rs.getInt("id_trabajador"));
                m.setTipoMovimiento(rs.getString("tipo_movimiento"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setStockAnterior(rs.getInt("stock_anterior"));
                m.setStockResultante(rs.getInt("stock_resultante"));
                m.setMotivo(rs.getString("motivo"));
                m.setFechaHora(rs.getTimestamp("fecha_hora") != null ? 
                                     rs.getTimestamp("fecha_hora").toLocalDateTime() : null);
                m.setUsuarioCreacion(rs.getInt("usuario_creacion"));
                lista.add(m);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarPorFechas MovimientoInventario: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    /**
     * Cierra todos los recursos JDBC utilizados.
     * IMPORTANTE: Siempre llamar en el bloque finally.
     */
    private void cerrarRecursos() {
        try { if (cs != null) cs.close(); } catch (Exception ex) {}
        try { if (pst != null) pst.close(); } catch (Exception ex) {}
        try { if (st != null) st.close(); } catch (Exception ex) {}
        try { if (rs != null) rs.close(); } catch (Exception ex) {}
        try { if (con != null) con.close(); } catch (Exception ex) {}
    }
}
