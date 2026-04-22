package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.persistance.dao.operaciones.dao.DevolucionDao;
import pe.edu.pucp.model.operaciones.Devolucion;
import pe.edu.pucp.db.DBManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Devolucion.
 * Utiliza procedimientos almacenados actualizados para las operaciones en BD.
 */
public class DevolucionDaoImpl implements DevolucionDao {
    // ================= RECURSOS JDBC =================
    private Connection con;
    private CallableStatement cs;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    @Override
    public int insertar(Devolucion devolucion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call INSERTAR_DEVOLUCION(?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER); // ID devuelto
            cs.setInt(2, devolucion.getIdProducto());
            cs.setInt(3, devolucion.getIdTrabajador());
            cs.setString(4, devolucion.getEstadoDevolucion());
            cs.setInt(5, devolucion.getCantidad());
            cs.setString(6, devolucion.getMotivo());
            cs.setTimestamp(7, devolucion.getFechaHora() != null ? Timestamp.valueOf(devolucion.getFechaHora())
                    : Timestamp.valueOf(LocalDateTime.now()));
            cs.executeUpdate();
            devolucion.setIdDevolucion(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar Devolucion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    @Override
    public int modificar(Devolucion devolucion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_DEVOLUCION(?,?,?,?,?,?,?)}");
            cs.setInt(1, devolucion.getIdDevolucion());
            cs.setInt(2, devolucion.getIdProducto());
            cs.setInt(3, devolucion.getIdTrabajador());
            cs.setString(4, devolucion.getEstadoDevolucion());
            cs.setInt(5, devolucion.getCantidad());
            cs.setString(6, devolucion.getMotivo());
            cs.setTimestamp(7, Timestamp.valueOf(devolucion.getFechaHora()));
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar Devolucion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call ELIMINAR_DEVOLUCION(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar Devolucion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    @Override
    public Devolucion buscarPorID(int id) {
        Devolucion d = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_DEVOLUCION_POR_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                d = new Devolucion();
                d.setIdDevolucion(rs.getInt("DEVOLUCION_ID"));
                d.setIdProducto(rs.getInt("PRODUCTO_ID"));
                d.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                d.setEstadoDevolucion(rs.getString("ESTADO_DEVOLUCION"));
                d.setCantidad(rs.getInt("CANTIDAD"));
                d.setMotivo(rs.getString("MOTIVO"));
                d.setFechaHora(
                        rs.getTimestamp("FECHA_HORA") != null ? rs.getTimestamp("FECHA_HORA").toLocalDateTime() : null);
                d.setActivo(rs.getBoolean("ACTIVO"));
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorID Devolucion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return d;
    }

    @Override
    public List<Devolucion> listarTodos() {
        List<Devolucion> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_DEVOLUCIONES_TODAS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                Devolucion d = new Devolucion();
                d.setIdDevolucion(rs.getInt("DEVOLUCION_ID"));
                d.setIdProducto(rs.getInt("PRODUCTO_ID"));
                d.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                d.setEstadoDevolucion(rs.getString("ESTADO_DEVOLUCION"));
                d.setCantidad(rs.getInt("CANTIDAD"));
                d.setMotivo(rs.getString("MOTIVO"));
                d.setFechaHora(
                        rs.getTimestamp("FECHA_HORA") != null ? rs.getTimestamp("FECHA_HORA").toLocalDateTime() : null);
                d.setActivo(rs.getBoolean("ACTIVO"));
                lista.add(d);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarTodos Devoluciones: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    @Override
    public List<Devolucion> listarPorFechas(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        List<Devolucion> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            // Asumiendo que LISTAR_DEVOLUCIONES_POR_FECHAS usa las mismas columnas
            cs = con.prepareCall("{call LISTAR_DEVOLUCIONES_POR_FECHAS(?,?)}");
            cs.setTimestamp(1, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            cs.setTimestamp(2, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            rs = cs.executeQuery();
            while (rs.next()) {
                Devolucion d = new Devolucion();
                d.setIdDevolucion(rs.getInt("DEVOLUCION_ID"));
                d.setIdProducto(rs.getInt("PRODUCTO_ID"));
                d.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                d.setEstadoDevolucion(rs.getString("ESTADO_DEVOLUCION"));
                d.setCantidad(rs.getInt("CANTIDAD"));
                d.setMotivo(rs.getString("MOTIVO"));
                d.setFechaHora(
                        rs.getTimestamp("FECHA_HORA") != null ? rs.getTimestamp("FECHA_HORA").toLocalDateTime() : null);
                d.setActivo(rs.getBoolean("ACTIVO"));
                lista.add(d);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarPorFechas Devoluciones: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    private void cerrarRecursos() {
        try {
            if (cs != null)
                cs.close();
        } catch (Exception ex) {
        }
        try {
            if (pst != null)
                pst.close();
        } catch (Exception ex) {
        }
        try {
            if (st != null)
                st.close();
        } catch (Exception ex) {
        }
        try {
            if (rs != null)
                rs.close();
        } catch (Exception ex) {
        }
        try {
            if (con != null)
                con.close();
        } catch (Exception ex) {
        }
    }
}
