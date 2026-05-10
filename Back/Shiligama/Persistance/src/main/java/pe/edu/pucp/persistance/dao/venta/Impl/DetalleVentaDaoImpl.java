package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.persistance.dao.venta.dao.DetalleVentaDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetalleVentaDaoImpl implements DetalleVentaDao {

    // SP: INSERTAR_DETALLE_VENTA(OUT _detalle_venta_id, IN _venta_id, IN _producto_id, IN _cantidad)
    @Override
    public int insertar(DetalleVenta detalleVenta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, detalleVenta.getIdPadreVenta());
        parametrosEntrada.put(3, detalleVenta.getProducto().getIdProducto());
        parametrosEntrada.put(4, detalleVenta.getCantidad());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_DETALLE_VENTA", parametrosEntrada, parametrosSalida);
        detalleVenta.setIdDetalleVenta((int) parametrosSalida.get(1));
        return detalleVenta.getIdDetalleVenta();
    }

    // SP: MODIFICAR_DETALLE_VENTA(IN _detalle_venta_id, IN _cantidad)
    @Override
    public int modificar(DetalleVenta detalleVenta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, detalleVenta.getIdDetalleVenta());
        parametrosEntrada.put(2, detalleVenta.getCantidad());
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_DETALLE_VENTA", parametrosEntrada, null);
    }

    // SP: ELIMINAR_DETALLE_VENTA(IN _detalle_venta_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_DETALLE_VENTA", parametrosEntrada, null);
    }

    // SP: BUSCAR_DETALLE_VENTA_X_ID(IN _detalle_venta_id)
    @Override
    public DetalleVenta buscarPorID(int id) {
        DetalleVenta detalle = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_DETALLE_VENTA_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    detalle = mapearDetalleVenta(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar detalle de venta: " + ex.getMessage());
        }
        return detalle;
    }

    // SP: LISTAR_DETALLES_VENTA()
    @Override
    public List<DetalleVenta> listarTodos() {
        List<DetalleVenta> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_DETALLES_VENTA", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearDetalleVenta(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar detalles de venta: " + ex.getMessage());
        }
        return lista;
    }

    private DetalleVenta mapearDetalleVenta(ResultSet rs) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdDetalleVenta(rs.getInt("DETALLE_VENTA_ID"));
        detalle.setIdPadreVenta(rs.getInt("VENTA_ID"));
        detalle.setCantidad(rs.getInt("CANTIDAD"));
        detalle.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
        detalle.setSubtotal(rs.getDouble("SUBTOTAL"));

        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("PRODUCTO_ID"));
        producto.setNombre(rs.getString("PRODUCTO_NOMBRE"));
        detalle.setProducto(producto);

        return detalle;
    }
}
