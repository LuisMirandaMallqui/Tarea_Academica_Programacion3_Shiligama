package pe.edu.pucp.persistance.dao.operacion.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.operacion.Lote;
import pe.edu.pucp.persistance.dao.operacion.dao.LoteDao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoteDaoImpl implements LoteDao {

    @Override
    public int insertar(Lote lote) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, lote.getIdProducto());
        parametrosEntrada.put(3, lote.getIdTrabajador());
        parametrosEntrada.put(4, lote.getCantidadInicial());
        parametrosEntrada.put(5, lote.getFechaVencimiento() != null
                ? Date.valueOf(lote.getFechaVencimiento()) : null);
        parametrosEntrada.put(6, lote.getNumeroLote());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_LOTE", parametrosEntrada, parametrosSalida);
        lote.setIdLote((int) parametrosSalida.get(1));
        return lote.getIdLote();
    }

    @Override
    public int modificar(Lote lote) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, lote.getIdLote());
        parametrosEntrada.put(2, lote.getCantidadActual());
        parametrosEntrada.put(3, lote.getFechaVencimiento() != null
                ? Date.valueOf(lote.getFechaVencimiento()) : null);
        parametrosEntrada.put(4, lote.getNumeroLote());

        DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_LOTE", parametrosEntrada, null);
        return 1;
    }

    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_LOTE", parametrosEntrada, null);
        return 1;
    }

    @Override
    public Lote buscarPorId(int id) {
        Lote lote = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_LOTE_POR_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    lote = mapearLote(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar lote: " + ex.getMessage());
        }
        return lote;
    }

    @Override
    public List<Lote> listarTodos() {
        List<Lote> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_LOTES_TODOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearLote(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar lotes: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Lote> listarPorProducto(int idProducto) {
        List<Lote> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idProducto);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_LOTES_POR_PRODUCTO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearLote(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar lotes por producto: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Lote> listarProximosAVencer(int dias) {
        List<Lote> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, dias);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_LOTES_PROXIMOS_VENCER", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearLote(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar lotes próximos a vencer: " + ex.getMessage());
        }
        return lista;
    }

    private Lote mapearLote(ResultSet rs) throws SQLException {
        Lote l = new Lote();
        l.setIdLote(rs.getInt("LOTE_ID"));
        l.setIdProducto(rs.getInt("PRODUCTO_ID"));
        int trabId = rs.getInt("TRABAJADOR_ID");
        l.setIdTrabajador(rs.wasNull() ? 0 : trabId);
        l.setCantidadInicial(rs.getInt("CANTIDAD_INICIAL"));
        l.setCantidadActual(rs.getInt("CANTIDAD_ACTUAL"));
        Date fv = rs.getDate("FECHA_VENCIMIENTO");
        l.setFechaVencimiento(fv != null ? fv.toLocalDate() : null);
        l.setNumeroLote(rs.getString("NUMERO_LOTE"));
        l.setActivo(rs.getInt("ACTIVO") == 1);
        try { l.setNombreTrabajador(rs.getString("TRABAJADOR_NOMBRE")); } catch (SQLException ignored) {}
        try { l.setNombreProducto(rs.getString("PRODUCTO_NOMBRE")); } catch (SQLException ignored) {}
        return l;
    }
}
