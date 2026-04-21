package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.persistance.dao.operaciones.dao.DevolucionDAO;
import pe.edu.pucp.model.operaciones.Devolucion;
import pe.edu.pucp.db.DBManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Devolucion.
 * Utiliza procedimientos almacenados para las operaciones en BD.
 */
public class DevolucionImpl implements DevolucionDAO {
    // ================= RECURSOS JDBC =================
    private Connection con;
    private CallableStatement cs;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    /**
     * Inserta una nueva devolución.
     * @param devolucion Objeto con los datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int insertar(Devolucion devolucion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call INSERTAR_DEVOLUCION(?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER); // ID devuelto
            cs.setInt(2, devolucion.getIdVenta());
            cs.setString(3, devolucion.getMotivo());
            // Si fechaSolicitud es generada, se puede usar Timestamp.valueOf
            cs.setTimestamp(4, devolucion.getFechaSolicitud() != null ? 
                               Timestamp.valueOf(devolucion.getFechaSolicitud()) : 
                               Timestamp.valueOf(LocalDateTime.now()));
            cs.setString(5, devolucion.getEstado());
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

    /**
     * Modifica una devolución existente.
     * @param devolucion Objeto con los nuevos datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int modificar(Devolucion devolucion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_DEVOLUCION(?,?,?,?,?)}");
            cs.setInt(1, devolucion.getIdDevolucion());
            cs.setInt(2, devolucion.getIdVenta());
            cs.setString(3, devolucion.getMotivo());
            cs.setTimestamp(4, Timestamp.valueOf(devolucion.getFechaSolicitud()));
            cs.setString(5, devolucion.getEstado());
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

    /**
     * Elimina lógicamente una devolución (activo = 0).
     * @param id ID de la devolución
     * @return 1 si éxito, 0 si error
     */
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

    /**
     * Busca una devolución mediante su ID.
     * @param id ID de la devolución
     * @return Objeto Devolucion, o null si no se encuentra
     */
    @Override
    public Devolucion buscarPorID(int id) {
        Devolucion d = null;
        try {
            con = DBManager.getInstance().getConnection();
            // A falta de un SP, usaré PreparedStatement o asumo que existe el SP. 
            // Crear el SP BUSCAR_DEVOLUCION_POR_ID en el script SQL para consistencia.
            cs = con.prepareCall("{call BUSCAR_DEVOLUCION_POR_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                d = new Devolucion();
                d.setIdDevolucion(rs.getInt("id_devolucion"));
                d.setIdVenta(rs.getInt("id_venta"));
                d.setMotivo(rs.getString("motivo"));
                d.setFechaSolicitud(rs.getTimestamp("fecha_solicitud") != null ? 
                                    rs.getTimestamp("fecha_solicitud").toLocalDateTime() : null);
                d.setEstado(rs.getString("estado"));
                d.setActivo(rs.getBoolean("activo"));
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorID Devolucion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return d;
    }

    /**
     * Lista todas las devoluciones activas registradas en el sistema.
     * @return Lista de devoluciones
     */
    @Override
    public List<Devolucion> listarTodos() {
        List<Devolucion> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_DEVOLUCIONES_TODAS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                Devolucion d = new Devolucion();
                d.setIdDevolucion(rs.getInt("id_devolucion"));
                d.setIdVenta(rs.getInt("id_venta"));
                d.setMotivo(rs.getString("motivo"));
                d.setFechaSolicitud(rs.getTimestamp("fecha_solicitud") != null ? 
                                    rs.getTimestamp("fecha_solicitud").toLocalDateTime() : null);
                d.setEstado(rs.getString("estado"));
                d.setActivo(rs.getBoolean("activo"));
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
            cs = con.prepareCall("{call LISTAR_DEVOLUCIONES_POR_FECHAS(?,?)}");
            cs.setTimestamp(1, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            cs.setTimestamp(2, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            rs = cs.executeQuery();
            while (rs.next()) {
                Devolucion d = new Devolucion();
                d.setIdDevolucion(rs.getInt("id_devolucion"));
                d.setIdVenta(rs.getInt("id_venta"));
                d.setMotivo(rs.getString("motivo"));
                d.setFechaSolicitud(rs.getTimestamp("fecha_solicitud") != null ? 
                                    rs.getTimestamp("fecha_solicitud").toLocalDateTime() : null);
                d.setEstado(rs.getString("estado"));
                d.setActivo(rs.getBoolean("activo"));
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
