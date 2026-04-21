package pe.edu.pucp.persistance.dao.proveedor.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.proveedor.DetalleOrdenReabastecimientoDto;
import pe.edu.pucp.persistance.dao.proveedor.dao.DetalleOrdenReabastecimienDAO;
import pe.edu.pucp.persistance.dao.proveedor.dao.OrdenReabastecimientoDtoDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

public class DetalleOrdenReabastecimienImpl implements DetalleOrdenReabastecimienDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(DetalleOrdenReabastecimientoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_DETALLE_ORDEN_REABAST(?，?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, obj.getOrden().getIdOrden());
            cs.setInt(3, obj.getProducto().getIdProducto());
            cs.setInt(4, obj.getCantidadSolicitada());
            cs.setDouble(5, obj.getPrecioCompra());
            cs.executeUpdate();
            resultado = cs.getInt(1);
        } catch (Exception ex) {
            System.out.println("Error al insertar detalle orden: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(DetalleOrdenReabastecimientoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL MODIFICAR_DETALLE_ORDEN_REABAST(?,?,?,?)}");
            cs.setInt(1, obj.getIdDetalleOrden());
            cs.setInt(2, obj.getOrden().getIdOrden());
            cs.setInt(3, obj.getProducto().getIdProducto());
            cs.setInt(4, obj.getCantidadSolicitada());
            cs.setDouble(5, obj.getPrecioCompra());
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error al modificar detalle orden: " + ex.getMessage());
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
            cs = con.prepareCall("{CALL ELIMINAR_DETALLE_ORDEN_REABAST(?)}");
            cs.setInt(1, id);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error al eliminar detalle orden: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public DetalleOrdenReabastecimientoDto buscarPorID(int id) {
        return null;
    }

    @Override
    public List<DetalleOrdenReabastecimientoDto> listarTodos() {
        return null;
    }
}
