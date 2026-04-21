package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.venta.DetallePedidoDto;
import pe.edu.pucp.persistance.dao.venta.dao.DetallePedidoDtoDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDtoImpl implements DetallePedidoDtoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;


    @Override
    public int insertar(DetallePedidoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_DETALLE_PEDIDO(?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, obj.getIdPadrePedido());
            cs.setInt(3, obj.getProducto().getIdProducto());
            cs.setInt(4, obj.getCantidad());
            cs.execute();
            obj.setIdDetallePedido(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar DetallePedido: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(DetallePedidoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL MODIFICAR_DETALLE_PEDIDO(?, ?)}");
            cs.setInt(1, obj.getIdDetallePedido());
            cs.setInt(2, obj.getCantidad());
            cs.execute();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar DetallePedido: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL ELIMINAR_DETALLE_PEDIDO(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar DetallePedido: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public DetallePedidoDto buscarPorID(int id) {
        DetallePedidoDto detalle = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_DETALLE_PEDIDO_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                detalle = new DetallePedidoDto();
                detalle.setIdDetallePedido(rs.getInt("DETALLE_PEDIDO_ID"));
                detalle.setIdPadrePedido(rs.getInt("PEDIDO_ID"));
                detalle.setCantidad(rs.getInt("CANTIDAD"));
                detalle.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                detalle.setSubtotal(rs.getDouble("SUBTOTAL"));

                ProductoDto producto = new ProductoDto();
                producto.setIdProducto(rs.getInt("PRODUCTO_ID"));
                producto.setNombre(rs.getString("PRODUCTO_NOMBRE"));
                detalle.setProducto(producto);
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorId DetallePedido: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return detalle;
    }

    @Override
    public List<DetallePedidoDto> listarTodos() {
        List<DetallePedidoDto> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_DETALLES_PEDIDO()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                DetallePedidoDto detalle = new DetallePedidoDto();
                detalle.setIdDetallePedido(rs.getInt("DETALLE_PEDIDO_ID"));
                detalle.setIdPadrePedido(rs.getInt("PEDIDO_ID"));
                detalle.setCantidad(rs.getInt("CANTIDAD"));
                detalle.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                detalle.setSubtotal(rs.getDouble("SUBTOTAL"));

                ProductoDto producto = new ProductoDto();
                producto.setIdProducto(rs.getInt("PRODUCTO_ID"));
                producto.setNombre(rs.getString("PRODUCTO_NOMBRE"));
                detalle.setProducto(producto);

                lista.add(detalle);
            }
        } catch (Exception ex) {
            System.err.println("Error en listar DetallePedido: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return lista;
    }
}
