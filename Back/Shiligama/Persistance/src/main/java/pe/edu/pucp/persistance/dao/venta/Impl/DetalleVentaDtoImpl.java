package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.venta.DetalleVentaDto;
import pe.edu.pucp.persistance.dao.venta.dao.DetalleVentaDtoDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDtoImpl implements DetalleVentaDtoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(DetalleVentaDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_DETALLE_VENTA(?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, obj.getIdPadreVenta());
            cs.setInt(3, obj.getProducto().getIdProducto());
            cs.setInt(4, obj.getCantidad());
            cs.execute();
            obj.setIdDetalleVenta(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
    public int modificar(DetalleVentaDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL MODIFICAR_DETALLE_VENTA(?, ?)}");
            cs.setInt(1, obj.getIdDetalleVenta());
            cs.setInt(2, obj.getCantidad());
            cs.execute();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar DetalleVenta: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL ELIMINAR_DETALLE_VENTA(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar DetalleVenta: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public DetalleVentaDto buscarPorID(int id) {
        DetalleVentaDto detalle = null;
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_DETALLE_VENTA_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                detalle = new DetalleVentaDto();
                detalle.setIdDetalleVenta(rs.getInt("DETALLE_VENTA_ID"));
                detalle.setIdPadreVenta(rs.getInt("VENTA_ID"));
                detalle.setCantidad(rs.getInt("CANTIDAD"));
                detalle.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                detalle.setSubtotal(rs.getDouble("SUBTOTAL"));

                ProductoDto producto = new ProductoDto();
                producto.setIdProducto(rs.getInt("PRODUCTO_ID"));
                producto.setNombre(rs.getString("PRODUCTO_NOMBRE"));
                detalle.setProducto(producto);
                resultado = 1;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
    public List<DetalleVentaDto> listarTodos() {
        List<DetalleVentaDto> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_DETALLES_VENTA()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                DetalleVentaDto detalle = new DetalleVentaDto();
                detalle.setIdDetalleVenta(rs.getInt("DETALLE_VENTA_ID"));
                detalle.setIdPadreVenta(rs.getInt("VENTA_ID"));
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
            System.err.println("Error en listar DetalleVenta: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }

        return lista;
    }
}
