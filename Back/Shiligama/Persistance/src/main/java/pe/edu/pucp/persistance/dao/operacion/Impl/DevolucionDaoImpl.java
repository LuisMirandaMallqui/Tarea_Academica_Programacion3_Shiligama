package pe.edu.pucp.persistance.dao.operacion.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.persistance.dao.operacion.dao.DevolucionDao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevolucionDaoImpl implements DevolucionDao {

    // SP: INSERTAR_DEVOLUCION(OUT _devolucion_id, IN _id_producto, IN _id_trabajador,
    //   IN _estado_devolucion, IN _cantidad, IN _motivo, IN _fecha_hora)
    @Override
    public int insertar(Devolucion devolucion) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, devolucion.getIdProducto());
        parametrosEntrada.put(3, devolucion.getIdTrabajador());
        parametrosEntrada.put(4, devolucion.getEstadoDevolucion());
        parametrosEntrada.put(5, devolucion.getCantidad());
        parametrosEntrada.put(6, devolucion.getMotivo());
        parametrosEntrada.put(7, devolucion.getFechaHora() != null
                ? Timestamp.valueOf(devolucion.getFechaHora())
                : Timestamp.valueOf(LocalDateTime.now()));

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_DEVOLUCION", parametrosEntrada, parametrosSalida);
        devolucion.setIdDevolucion((int) parametrosSalida.get(1));
        return devolucion.getIdDevolucion();
    }

    // SP: MODIFICAR_DEVOLUCION(IN _devolucion_id, IN _producto_id, IN _trabajador_id,
    //   IN _estado_devolucion, IN _cantidad, IN _motivo, IN _fecha_hora)
    @Override
    public int modificar(Devolucion devolucion) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, devolucion.getIdDevolucion());
        parametrosEntrada.put(2, devolucion.getIdProducto());
        parametrosEntrada.put(3, devolucion.getIdTrabajador());
        parametrosEntrada.put(4, devolucion.getEstadoDevolucion());
        parametrosEntrada.put(5, devolucion.getCantidad());
        parametrosEntrada.put(6, devolucion.getMotivo());
        parametrosEntrada.put(7, Timestamp.valueOf(devolucion.getFechaHora()));

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_DEVOLUCION", parametrosEntrada, null);
    }

    // SP: ELIMINAR_DEVOLUCION(IN _devolucion_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_DEVOLUCION", parametrosEntrada, null);
    }

    // SP: BUSCAR_DEVOLUCION_POR_ID(IN _devolucion_id)
    @Override
    public Devolucion buscarPorID(int id) {
        Devolucion d = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_DEVOLUCION_POR_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    d = mapearDevolucion(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar devolucion: " + ex.getMessage());
        }
        return d;
    }

    // SP: LISTAR_DEVOLUCIONES_TODAS()
    @Override
    public List<Devolucion> listarTodos() {
        List<Devolucion> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_DEVOLUCIONES_TODAS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearDevolucion(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar devoluciones: " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_DEVOLUCIONES_POR_FECHAS(IN _fecha_inicio, IN _fecha_fin)
    @Override
    public List<Devolucion> listarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Devolucion> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, Timestamp.valueOf(fechaInicio.atStartOfDay()));
        parametrosEntrada.put(2, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_DEVOLUCIONES_POR_FECHAS", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearDevolucion(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar devoluciones por fechas: " + ex.getMessage());
        }
        return lista;
    }

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
}
