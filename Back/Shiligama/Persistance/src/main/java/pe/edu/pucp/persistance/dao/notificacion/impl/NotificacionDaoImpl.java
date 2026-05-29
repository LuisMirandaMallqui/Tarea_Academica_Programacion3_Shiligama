package pe.edu.pucp.persistance.dao.notificacion.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.model.notificacion.Notificacion;
import pe.edu.pucp.persistance.dao.notificacion.dao.NotificacionDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificacionDaoImpl implements NotificacionDao {

    // SP: INSERTAR_NOTIFICACION(OUT _notif_id, IN _titulo, _mensaje, _tipo, _id_destinatario)
    @Override
    public int insertar(Notificacion notif) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, notif.getTitulo());
        parametrosEntrada.put(3, notif.getMensaje());
        parametrosEntrada.put(4, notif.getTipo().name());
        parametrosEntrada.put(5, notif.getIdDestinatario());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_NOTIFICACION", parametrosEntrada, parametrosSalida);
        notif.setIdNotificacion((int) parametrosSalida.get(1));
        return notif.getIdNotificacion();
    }

    // SP: MODIFICAR_NOTIFICACION(IN _notif_id, _titulo, _mensaje, _tipo, _leida)
    @Override
    public int modificar(Notificacion notif) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, notif.getIdNotificacion());
        parametrosEntrada.put(2, notif.getTitulo());
        parametrosEntrada.put(3, notif.getMensaje());
        parametrosEntrada.put(4, notif.getTipo().name());
        parametrosEntrada.put(5, notif.isLeida() ? 1 : 0);
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_NOTIFICACION", parametrosEntrada, null);
    }

    // SP: ELIMINAR_NOTIFICACION(IN _notif_id) — soft delete
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_NOTIFICACION", parametrosEntrada, null);
    }

    // SP: BUSCAR_NOTIFICACION_X_ID(IN _notif_id)
    @Override
    public Notificacion buscarPorID(int id) {
        Notificacion notif = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_NOTIFICACION_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    notif = mapearNotificacion(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar notificación: " + ex.getMessage());
        }
        return notif;
    }

    // SP: LISTAR_NOTIFICACIONES()
    @Override
    public List<Notificacion> listarTodos() {
        List<Notificacion> lista = new ArrayList<>();
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_NOTIFICACIONES", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearNotificacion(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar notificaciones: " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_NOTIFICACIONES_X_USUARIO(IN _usuario_id)
    @Override
    public List<Notificacion> listarPorUsuario(int idUsuario) {
        List<Notificacion> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idUsuario);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_NOTIFICACIONES_X_USUARIO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearNotificacion(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en listarPorUsuario (notificaciones): " + ex.getMessage());
        }
        return lista;
    }

    // SP: MARCAR_NOTIFICACION_LEIDA(IN _notif_id)
    @Override
    public int marcarLeida(int idNotificacion) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idNotificacion);
        return DBManager.getInstance().ejecutarProcedimiento(
                "MARCAR_NOTIFICACION_LEIDA", parametrosEntrada, null);
    }

    // SP: CONTAR_NOTIFICACIONES_NO_LEIDAS(IN _usuario_id)
    @Override
    public int contarNoLeidas(int idUsuario) {
        int total = 0;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idUsuario);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("CONTAR_NOTIFICACIONES_NO_LEIDAS", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    total = rs.getInt("TOTAL");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en contarNoLeidas: " + ex.getMessage());
        }
        return total;
    }

    private Notificacion mapearNotificacion(ResultSet rs) throws SQLException {
        Notificacion n = new Notificacion();
        n.setIdNotificacion(rs.getInt("NOTIFICACION_ID"));
        n.setTitulo(rs.getString("TITULO"));
        n.setMensaje(rs.getString("MENSAJE"));
        n.setTipo(TipoNotificacion.valueOf(rs.getString("TIPO")));
        n.setLeida(rs.getInt("LEIDA") == 1);
        Timestamp fechaCreacion = rs.getTimestamp("FECHA_CREACION");
        if (fechaCreacion != null) {
            n.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        int destinatario = rs.getInt("ID_DESTINATARIO");
        n.setIdDestinatario(rs.wasNull() ? null : destinatario);
        return n;
    }
}
