package pe.edu.pucp.persistance.dao.operacion.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.persistance.dao.operacion.dao.MovimientoInventarioDao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientoInventarioDaoImpl implements MovimientoInventarioDao {

    // SP: REGISTRAR_MOVIMIENTO_INVENTARIO(OUT _movimiento_id, IN _producto_id,
    //   IN _trabajador_id, IN _tipo_movimiento, IN _cantidad, IN _motivo)
    @Override
    public int insertar(MovimientoInventario mov) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, mov.getIdProducto());
        parametrosEntrada.put(3, mov.getIdTrabajador());
        parametrosEntrada.put(4, mov.getTipoMovimiento());
        parametrosEntrada.put(5, mov.getCantidad());
        parametrosEntrada.put(6, mov.getMotivo());

        DBManager.getInstance().ejecutarProcedimiento(
                "REGISTRAR_MOVIMIENTO_INVENTARIO", parametrosEntrada, parametrosSalida);
        mov.setIdMovimiento((int) parametrosSalida.get(1));
        return mov.getIdMovimiento();
    }

    // Log inmutable — no aplica
    @Override
    public int modificar(MovimientoInventario mov) {
        System.out.println("No aplica para log inmutable");
        return 0;
    }

    // Log inmutable — no aplica
    @Override
    public int eliminar(int id) {
        System.out.println("No aplica para log inmutable");
        return 0;
    }

    // SP: BUSCAR_MOVIMIENTO_POR_ID(IN _movimiento_id)
    @Override
    public MovimientoInventario buscarPorID(int id) {
        MovimientoInventario m = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_MOVIMIENTO_POR_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    m = mapearMovimiento(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar movimiento: " + ex.getMessage());
        }
        return m;
    }

    // SP: LISTAR_MOVIMIENTOS_TODOS()
    @Override
    public List<MovimientoInventario> listarTodos() {
        List<MovimientoInventario> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_MOVIMIENTOS_TODOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos: " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_MOVIMIENTOS_POR_PRODUCTO(IN _producto_id)
    @Override
    public List<MovimientoInventario> listarPorProducto(int idProducto) {
        List<MovimientoInventario> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idProducto);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_MOVIMIENTOS_POR_PRODUCTO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos por producto: " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_MOVIMIENTOS_POR_FECHAS(IN _fecha_inicio, IN _fecha_fin)
    @Override
    public List<MovimientoInventario> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<MovimientoInventario> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, Timestamp.valueOf(fechaInicio));
        parametrosEntrada.put(2, Timestamp.valueOf(fechaFin));

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_MOVIMIENTOS_POR_FECHAS", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar movimientos por fechas: " + ex.getMessage());
        }
        return lista;
    }

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
}
