package pe.edu.pucp.persistance.dao.producto.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.Categoria;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.persistance.dao.producto.dao.ProductoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDaoImpl implements ProductoDao {

    // SP: INSERTAR_PRODUCTO(OUT _producto_id, IN _categoria_id, IN _nombre,
    //   IN _descripcion, IN _precio_unitario, IN _stock, IN _stock_minimo,
    //   IN _unidad_medida, IN _codigo_barras, IN _imagen_url)
    @Override
    public int insertar(Producto producto) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, producto.getCategoria().getIdCategoria());
        parametrosEntrada.put(3, producto.getNombre());
        parametrosEntrada.put(4, producto.getDescripcion());
        parametrosEntrada.put(5, producto.getPrecioUnitario());
        parametrosEntrada.put(6, producto.getStock());
        parametrosEntrada.put(7, producto.getStockMinimo());
        parametrosEntrada.put(8, producto.getUnidadMedida());
        parametrosEntrada.put(9, producto.getCodigoBarras());
        parametrosEntrada.put(10, producto.getImagenUrl());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_PRODUCTO", parametrosEntrada, parametrosSalida);
        producto.setIdProducto((int) parametrosSalida.get(1));
        return producto.getIdProducto();
    }

    // SP: MODIFICAR_PRODUCTO(IN _producto_id, IN _categoria_id, IN _nombre,
    //   IN _descripcion, IN _precio_unitario, IN _stock_minimo,
    //   IN _unidad_medida, IN _codigo_barras, IN _imagen_url)
    @Override
    public int modificar(Producto producto) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, producto.getIdProducto());
        parametrosEntrada.put(2, producto.getCategoria().getIdCategoria());
        parametrosEntrada.put(3, producto.getNombre());
        parametrosEntrada.put(4, producto.getDescripcion());
        parametrosEntrada.put(5, producto.getPrecioUnitario());
        parametrosEntrada.put(6, producto.getStockMinimo());
        parametrosEntrada.put(7, producto.getUnidadMedida());
        parametrosEntrada.put(8, producto.getCodigoBarras());
        parametrosEntrada.put(9, producto.getImagenUrl());

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_PRODUCTO", parametrosEntrada, null);
    }

    // SP: ELIMINAR_PRODUCTO(IN _producto_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_PRODUCTO", parametrosEntrada, null);
    }

    // SP: BUSCAR_PRODUCTO_X_ID(IN _producto_id)
    @Override
    public Producto buscarPorId(int id) {
        Producto producto = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PRODUCTO_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    producto = mapearProducto(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar producto: " + ex.getMessage());
        }
        return producto;
    }

    // SP: LISTAR_PRODUCTOS()
    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PRODUCTOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar productos: " + ex.getMessage());
        }
        return lista;
    }

    // SP: BUSCAR_PRODUCTOS_PAGINADO(_categoria_id, _q, _precio_min, _precio_max,
    //                               _solo_promo, _pagina, _tamano)
    @Override
    public List<Producto> buscarPaginado(Integer categoriaId, String q,
                                         Double precioMin, Double precioMax,
                                         Boolean soloPromo, int pagina, int tamano) {
        List<Producto> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, categoriaId);
        parametrosEntrada.put(2, q);
        parametrosEntrada.put(3, precioMin);
        parametrosEntrada.put(4, precioMax);
        parametrosEntrada.put(5, soloPromo == null ? null : (soloPromo ? 1 : 0));
        parametrosEntrada.put(6, pagina);
        parametrosEntrada.put(7, tamano);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PRODUCTOS_PAGINADO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en buscarPaginado (productos): " + ex.getMessage());
        }
        return lista;
    }

    // SP: CONTAR_PRODUCTOS_FILTRADOS(_categoria_id, _q, _precio_min, _precio_max, _solo_promo)
    @Override
    public int contarFiltrados(Integer categoriaId, String q,
                               Double precioMin, Double precioMax,
                               Boolean soloPromo) {
        int total = 0;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, categoriaId);
        parametrosEntrada.put(2, q);
        parametrosEntrada.put(3, precioMin);
        parametrosEntrada.put(4, precioMax);
        parametrosEntrada.put(5, soloPromo == null ? null : (soloPromo ? 1 : 0));

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("CONTAR_PRODUCTOS_FILTRADOS", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    total = rs.getInt("TOTAL");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en contarFiltrados (productos): " + ex.getMessage());
        }
        return total;
    }

    // SP: BUSCAR_PRODUCTO_X_CODIGO_BARRAS(_codigo)
    @Override
    public Producto buscarPorCodigoBarras(String codigo) {
        Producto producto = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, codigo);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PRODUCTO_X_CODIGO_BARRAS", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    producto = mapearProducto(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en buscarPorCodigoBarras: " + ex.getMessage());
        }
        return producto;
    }

    // SP: LISTAR_PRODUCTOS_BAJO_STOCK()
    @Override
    public List<Producto> listarBajoStock() {
        List<Producto> lista = new ArrayList<>();
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PRODUCTOS_BAJO_STOCK", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en listarBajoStock: " + ex.getMessage());
        }
        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("PRODUCTO_ID"));
        p.setNombre(rs.getString("NOMBRE"));
        p.setDescripcion(rs.getString("DESCRIPCION"));
        p.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
        p.setStock(rs.getInt("STOCK"));
        p.setStockMinimo(rs.getInt("STOCK_MINIMO"));
        p.setUnidadMedida(rs.getString("UNIDAD_MEDIDA"));
        p.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
        p.setImagenUrl(rs.getString("IMAGEN_URL"));
        p.setEstado(rs.getBoolean("ACTIVO"));

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("CATEGORIA_ID"));
        categoria.setNombre(rs.getString("CATEGORIA_NOMBRE"));
        p.setCategoria(categoria);

        Timestamp fechaRegistro = rs.getTimestamp("FECHA_CREACION");
        if (fechaRegistro != null) {
            p.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }

        return p;
    }
}
