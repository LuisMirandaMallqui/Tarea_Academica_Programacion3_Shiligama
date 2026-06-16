package pe.edu.pucp.persistance.dao.operacion.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.model.operacion.DetalleDevolucion;
import pe.edu.pucp.persistance.dao.operacion.dao.DevolucionDao;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevolucionDaoImpl implements DevolucionDao {

    @Override
    public int insertar(Devolucion devolucion) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            Map<Integer, Object> parametrosEntrada = new HashMap<>();
            Map<Integer, Object> parametrosSalida = new HashMap<>();

            int firstProdId = 0;
            int totalCant = 0;
            if (devolucion.getDetalles() != null && !devolucion.getDetalles().isEmpty()) {
                firstProdId = devolucion.getDetalles().get(0).getIdProducto();
                totalCant = devolucion.getDetalles().stream().mapToInt(DetalleDevolucion::getCantidad).sum();
            } else {
                firstProdId = devolucion.getIdProducto();
                totalCant = devolucion.getCantidad();
            }

            parametrosSalida.put(1, Types.INTEGER);
            parametrosEntrada.put(2, firstProdId > 0 ? firstProdId : null);
            parametrosEntrada.put(3, devolucion.getIdPedido() > 0 ? devolucion.getIdPedido() : null);
            parametrosEntrada.put(4, devolucion.getIdTrabajador() > 0 ? devolucion.getIdTrabajador() : null);
            parametrosEntrada.put(5, devolucion.getEstadoDevolucion());
            parametrosEntrada.put(6, totalCant);
            parametrosEntrada.put(7, devolucion.getMotivo());
            parametrosEntrada.put(8, devolucion.getFechaHora() != null
                    ? Timestamp.valueOf(devolucion.getFechaHora())
                    : Timestamp.valueOf(LocalDateTime.now()));

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_DEVOLUCION", parametrosEntrada, parametrosSalida);
            devolucion.setIdDevolucion((int) parametrosSalida.get(1));

            // Insertar detalles
            if (devolucion.getDetalles() != null) {
                for (DetalleDevolucion det : devolucion.getDetalles()) {
                    Map<Integer, Object> paramsDet = new HashMap<>();
                    paramsDet.put(1, devolucion.getIdDevolucion());
                    paramsDet.put(2, det.getIdProducto());
                    paramsDet.put(3, det.getCantidad());
                    dbManager.ejecutarProcedimientoTransaccion(
                            "INSERTAR_DETALLE_DEVOLUCION", paramsDet, null);
                }
            }

            dbManager.confirmarTransaccion();
            resultado = devolucion.getIdDevolucion();
        } catch (SQLException ex) {
            System.out.println("Error al insertar devolucion: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    @Override
    public int modificar(Devolucion devolucion) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            Map<Integer, Object> parametrosEntrada = new HashMap<>();

            int firstProdId = 0;
            int totalCant = 0;
            if (devolucion.getDetalles() != null && !devolucion.getDetalles().isEmpty()) {
                firstProdId = devolucion.getDetalles().get(0).getIdProducto();
                totalCant = devolucion.getDetalles().stream().mapToInt(DetalleDevolucion::getCantidad).sum();
            } else {
                firstProdId = devolucion.getIdProducto();
                totalCant = devolucion.getCantidad();
            }

            parametrosEntrada.put(1, devolucion.getIdDevolucion());
            parametrosEntrada.put(2, firstProdId > 0 ? firstProdId : null);
            parametrosEntrada.put(3, devolucion.getIdPedido() > 0 ? devolucion.getIdPedido() : null);
            parametrosEntrada.put(4, devolucion.getIdTrabajador() > 0 ? devolucion.getIdTrabajador() : null);
            parametrosEntrada.put(5, devolucion.getEstadoDevolucion());
            parametrosEntrada.put(6, totalCant);
            parametrosEntrada.put(7, devolucion.getMotivo());
            parametrosEntrada.put(8, Timestamp.valueOf(devolucion.getFechaHora() != null 
                    ? devolucion.getFechaHora() : LocalDateTime.now()));

            dbManager.ejecutarProcedimientoTransaccion(
                    "MODIFICAR_DEVOLUCION", parametrosEntrada, null);

            // Eliminar detalles antiguos
            Map<Integer, Object> paramsDel = new HashMap<>();
            paramsDel.put(1, devolucion.getIdDevolucion());
            dbManager.ejecutarProcedimientoTransaccion(
                    "ELIMINAR_DETALLES_DEVOLUCION", paramsDel, null);

            // Insertar nuevos detalles
            if (devolucion.getDetalles() != null) {
                for (DetalleDevolucion det : devolucion.getDetalles()) {
                    Map<Integer, Object> paramsDet = new HashMap<>();
                    paramsDet.put(1, devolucion.getIdDevolucion());
                    paramsDet.put(2, det.getIdProducto());
                    paramsDet.put(3, det.getCantidad());
                    dbManager.ejecutarProcedimientoTransaccion(
                            "INSERTAR_DETALLE_DEVOLUCION", paramsDet, null);
                }
            }

            dbManager.confirmarTransaccion();
            resultado = 1;
        } catch (SQLException ex) {
            System.out.println("Error al modificar devolucion: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_DEVOLUCION", parametrosEntrada, null);
    }

    @Override
    public Devolucion buscarPorId(int id) {
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
        if (d != null) {
            cargarDetalles(d);
        }
        return d;
    }

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
        for (Devolucion d : lista) {
            cargarDetalles(d);
        }
        return lista;
    }

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
        for (Devolucion d : lista) {
            cargarDetalles(d);
        }
        return lista;
    }

    private Devolucion mapearDevolucion(ResultSet rs) throws SQLException {
        Devolucion d = new Devolucion();
        d.setIdDevolucion(rs.getInt("DEVOLUCION_ID"));
        d.setIdProducto(rs.getInt("PRODUCTO_ID"));
        d.setIdPedido(rs.getInt("PEDIDO_ID"));
        d.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
        d.setEstadoDevolucion(rs.getString("ESTADO_DEVOLUCION"));
        d.setCantidad(rs.getInt("CANTIDAD"));
        d.setMotivo(rs.getString("MOTIVO"));
        d.setFechaHora(rs.getTimestamp("FECHA_HORA") != null
                ? rs.getTimestamp("FECHA_HORA").toLocalDateTime() : null);
        d.setActivo(rs.getBoolean("ACTIVO"));
        
        try {
            d.setNombreTrabajador(rs.getString("TRABAJADOR_NOMBRE"));
        } catch (SQLException e) {
            d.setNombreTrabajador("");
        }
        
        return d;
    }

    private void cargarDetalles(Devolucion d) {
        if (d == null) return;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, d.getIdDevolucion());
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_DETALLES_DEVOLUCION", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    DetalleDevolucion det = new DetalleDevolucion();
                    det.setIdProducto(rs.getInt("PRODUCTO_ID"));
                    det.setNombreProducto(rs.getString("PRODUCTO_NOMBRE"));
                    det.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                    det.setCantidad(rs.getInt("CANTIDAD"));
                    d.getDetalles().add(det);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar detalles de devolucion: " + ex.getMessage());
        }
    }
}
