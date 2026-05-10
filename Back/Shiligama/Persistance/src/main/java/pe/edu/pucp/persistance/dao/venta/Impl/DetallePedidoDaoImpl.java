package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.model.venta.DetallePedido;
import pe.edu.pucp.persistance.dao.venta.dao.DetallePedidoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetallePedidoDaoImpl implements DetallePedidoDao {

    // SP: INSERTAR_DETALLE_PEDIDO(OUT _detalle_pedido_id, IN _pedido_id, IN _producto_id, IN _cantidad)
    @Override
    public int insertar(DetallePedido detallePedido) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, detallePedido.getIdPadrePedido());
        parametrosEntrada.put(3, detallePedido.getProducto().getIdProducto());
        parametrosEntrada.put(4, detallePedido.getCantidad());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_DETALLE_PEDIDO", parametrosEntrada, parametrosSalida);
        detallePedido.setIdDetallePedido((int) parametrosSalida.get(1));
        return detallePedido.getIdDetallePedido();
    }

    // SP: MODIFICAR_DETALLE_PEDIDO(IN _detalle_pedido_id, IN _cantidad)
    @Override
    public int modificar(DetallePedido detallePedido) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, detallePedido.getIdDetallePedido());
        parametrosEntrada.put(2, detallePedido.getCantidad());
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_DETALLE_PEDIDO", parametrosEntrada, null);
    }

    // SP: ELIMINAR_DETALLE_PEDIDO(IN _detalle_pedido_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_DETALLE_PEDIDO", parametrosEntrada, null);
    }

    // SP: BUSCAR_DETALLE_PEDIDO_X_ID(IN _detalle_pedido_id)
    @Override
    public DetallePedido buscarPorID(int id) {
        DetallePedido detalle = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_DETALLE_PEDIDO_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    detalle = mapearDetallePedido(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar detalle de pedido: " + ex.getMessage());
        }
        return detalle;
    }

    // SP: LISTAR_DETALLES_PEDIDO()
    @Override
    public List<DetallePedido> listarTodos() {
        List<DetallePedido> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_DETALLES_PEDIDO", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearDetallePedido(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar detalles de pedido: " + ex.getMessage());
        }
        return lista;
    }

    private DetallePedido mapearDetallePedido(ResultSet rs) throws SQLException {
        DetallePedido detalle = new DetallePedido();
        detalle.setIdDetallePedido(rs.getInt("DETALLE_PEDIDO_ID"));
        detalle.setIdPadrePedido(rs.getInt("PEDIDO_ID"));
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
