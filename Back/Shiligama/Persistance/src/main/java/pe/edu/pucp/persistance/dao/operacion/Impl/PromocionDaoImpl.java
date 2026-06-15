package pe.edu.pucp.persistance.dao.operacion.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.TipoDescuento;
import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.persistance.dao.operacion.dao.PromocionDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromocionDaoImpl implements PromocionDao {

    // SP: INSERTAR_PROMOCION(OUT _promocion_id, IN _nombre, IN _descripcion,
    //   IN _tipo_descuento, IN _valor_descuento, IN _fecha_inicio, IN _fecha_fin, IN _condiciones)
    @Override
    public int insertar(Promocion promocion) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, promocion.getNombre());
        parametrosEntrada.put(3, promocion.getDescripcion());
        parametrosEntrada.put(4, promocion.getTipoDescuento().toString());
        parametrosEntrada.put(5, promocion.getValorDescuento());
        parametrosEntrada.put(6, Timestamp.valueOf(promocion.getFechaInicio()));
        parametrosEntrada.put(7, Timestamp.valueOf(promocion.getFechaFin()));
        parametrosEntrada.put(8, promocion.getCondiciones());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_PROMOCION", parametrosEntrada, parametrosSalida);
        promocion.setIdPromocion((int) parametrosSalida.get(1));
        return promocion.getIdPromocion();
    }

    // SP: MODIFICAR_PROMOCION(IN _promocion_id, IN _nombre, IN _descripcion,
    //   IN _tipo_descuento, IN _valor_descuento, IN _fecha_inicio, IN _fecha_fin, IN _condiciones)
    @Override
    public int modificar(Promocion promocion) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, promocion.getIdPromocion());
        parametrosEntrada.put(2, promocion.getNombre());
        parametrosEntrada.put(3, promocion.getDescripcion());
        parametrosEntrada.put(4, promocion.getTipoDescuento().toString());
        parametrosEntrada.put(5, promocion.getValorDescuento());
        parametrosEntrada.put(6, Timestamp.valueOf(promocion.getFechaInicio()));
        parametrosEntrada.put(7, Timestamp.valueOf(promocion.getFechaFin()));
        parametrosEntrada.put(8, promocion.getCondiciones());

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_PROMOCION", parametrosEntrada, null);
    }

    // SP: ELIMINAR_PROMOCION(IN _promocion_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_PROMOCION", parametrosEntrada, null);
    }

    // SP: BUSCAR_PROMOCION_POR_ID(IN _promocion_id)
    @Override
    public Promocion buscarPorId(int id) {
        Promocion p = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PROMOCION_POR_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    p = mapearPromocion(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar promocion: " + ex.getMessage());
        }
        return p;
    }

    // SP: LISTAR_PROMOCIONES_TODAS()
    @Override
    public List<Promocion> listarTodos() {
        List<Promocion> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PROMOCIONES_TODAS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPromocion(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar promociones: " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_PROMOCIONES_VIGENTES()
    @Override
    public List<Promocion> listarVigentes() {
        List<Promocion> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PROMOCIONES_VIGENTES", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPromocion(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar promociones vigentes: " + ex.getMessage());
        }
        return lista;
    }

    // SP: VINCULAR_PRODUCTO_PROMOCION(IN _promocion_id, IN _producto_id)
    @Override
    public int asociarProducto(int idPromocion, int idProducto) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPromocion);
        parametrosEntrada.put(2, idProducto);
        return DBManager.getInstance().ejecutarProcedimiento(
                "VINCULAR_PRODUCTO_PROMOCION", parametrosEntrada, null);
    }

    // SP: DESVINCULAR_PRODUCTO_PROMOCION(IN _promocion_id, IN _producto_id)
    @Override
    public int desasociarProducto(int idPromocion, int idProducto) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPromocion);
        parametrosEntrada.put(2, idProducto);
        return DBManager.getInstance().ejecutarProcedimiento(
                "DESVINCULAR_PRODUCTO_PROMOCION", parametrosEntrada, null);
    }

    // SP: LISTAR_PRODUCTOS_POR_PROMOCION(IN _promocion_id)
    @Override
    public List<Integer> listarProductosPorPromocion(int idPromocion) {
        List<Integer> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPromocion);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PRODUCTOS_POR_PROMOCION", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(rs.getInt("PRODUCTO_ID"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar productos por promocion: " + ex.getMessage());
        }
        return lista;
    }

    private Promocion mapearPromocion(ResultSet rs) throws SQLException {
        Promocion p = new Promocion();
        p.setIdPromocion(rs.getInt("id_promocion"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setTipoDescuento(TipoDescuento.valueOf(rs.getString("tipo_descuento")));
        p.setValorDescuento(rs.getDouble("valor_descuento"));
        p.setFechaInicio(
                rs.getTimestamp("fecha_inicio").toLocalDateTime()
        );

        p.setFechaFin(
                rs.getTimestamp("fecha_fin").toLocalDateTime()
        );
        p.setCondiciones(rs.getString("condiciones"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}
