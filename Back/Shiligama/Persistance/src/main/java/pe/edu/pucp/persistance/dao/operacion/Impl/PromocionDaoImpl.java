package pe.edu.pucp.persistance.dao.operacion.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.TipoDescuento;
import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.persistance.dao.operacion.dao.PromocionDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PromocionDaoImpl implements PromocionDao {

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
        parametrosEntrada.put(9, promocion.isMostrarEnCarrusel() ? 1 : 0);
        parametrosEntrada.put(10, promocion.getColorCarrusel() != null ? promocion.getColorCarrusel() : "#0D4525");

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_PROMOCION", parametrosEntrada, parametrosSalida);
        promocion.setIdPromocion((int) parametrosSalida.get(1));
        return promocion.getIdPromocion();
    }

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
        parametrosEntrada.put(9, promocion.isActivo() ? 1 : 0);
        parametrosEntrada.put(10, promocion.isMostrarEnCarrusel() ? 1 : 0);
        parametrosEntrada.put(11, promocion.getColorCarrusel() != null ? promocion.getColorCarrusel() : "#0D4525");

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_PROMOCION", parametrosEntrada, null);
    }

    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_PROMOCION", parametrosEntrada, null);
    }

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

    // SP: LISTAR_PROMOCIONES_CON_PRODUCTOS()
    // Devuelve todas las promos con sus productos en UNA sola query (sin N+1).
    // El ResultSet tiene una fila por cada (promo, producto). Si la promo no
    // tiene productos vinculados aparece con producto_id = NULL (LEFT JOIN).
    @Override
    public List<Promocion> listarTodasConProductos() {
        // LinkedHashMap para mantener el orden de inserción (por id_promocion)
        Map<Integer, Promocion> mapaPromos = new LinkedHashMap<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PROMOCIONES_CON_PRODUCTOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    int idPromocion = rs.getInt("id_promocion");

                    // Si la promo aún no está en el mapa, la creamos
                    Promocion promo = mapaPromos.get(idPromocion);
                    if (promo == null) {
                        promo = mapearPromocion(rs);
                        promo.setProductos(new ArrayList<>());
                        mapaPromos.put(idPromocion, promo);
                    }

                    // Agregar el producto vinculado (si existe)
                    int idProducto = rs.getInt("PRODUCTO_ID");
                    if (!rs.wasNull() && idProducto > 0) {
                        // Reutilizamos el campo "productos" de Promocion como lista de IDs
                        // Los pasamos como objetos mínimos — el front solo necesita el ID
                        pe.edu.pucp.model.producto.Producto prod = new pe.edu.pucp.model.producto.Producto();
                        prod.setIdProducto(idProducto);
                        promo.getProductos().add(prod);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar promociones con productos: " + ex.getMessage());
        }

        return new ArrayList<>(mapaPromos.values());
    }

    @Override
    public int asociarProducto(int idPromocion, int idProducto) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPromocion);
        parametrosEntrada.put(2, idProducto);
        return DBManager.getInstance().ejecutarProcedimiento(
                "VINCULAR_PRODUCTO_PROMOCION", parametrosEntrada, null);
    }

    @Override
    public int desasociarProducto(int idPromocion, int idProducto) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPromocion);
        parametrosEntrada.put(2, idProducto);
        return DBManager.getInstance().ejecutarProcedimiento(
                "DESVINCULAR_PRODUCTO_PROMOCION", parametrosEntrada, null);
    }

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
        p.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
        p.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
        p.setCondiciones(rs.getString("condiciones"));
        p.setActivo(rs.getBoolean("activo"));
        p.setMostrarEnCarrusel(rs.getBoolean("mostrar_en_carrusel"));
        p.setColorCarrusel(rs.getString("color_carrusel"));
        return p;
    }
}
