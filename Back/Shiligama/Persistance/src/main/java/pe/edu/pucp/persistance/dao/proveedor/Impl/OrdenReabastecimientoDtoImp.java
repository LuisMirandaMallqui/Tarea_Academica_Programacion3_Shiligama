package pe.edu.pucp.persistance.dao.proveedor.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.EstadoOrden;
import pe.edu.pucp.model.proveedor.OrdenReabastecimientoDto;
import pe.edu.pucp.model.proveedor.ProveedorDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.persistance.dao.proveedor.dao.OrdenReabastecimientoDtoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenReabastecimientoDtoImp implements OrdenReabastecimientoDtoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(OrdenReabastecimientoDto obj) {
        int resultado = 0;
        try{
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_ORDEN_REABASTECIMIENTO(?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, obj.getProveedor().getIdProveedor());
            cs.setInt(3, obj.getTrabajador().getIdTrabajador());
            cs.setDate(4, Date.valueOf(obj.getFechaEntregaEstimada()));
            cs.setString(5, obj.getObservaciones());
            cs.execute();
            obj.setIdOrden(cs.getInt(1));
            resultado = 1;
        }catch(Exception ex){
            System.out.println("Error en insertar orden de abastecimiento" + ex.getMessage());
        }finally{
            try{
                cs.close();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
            try{
                con.close();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(OrdenReabastecimientoDto obj) {
        throw new UnsupportedOperationException("Usar modificar(int, String)");
    }

    @Override
    public int modificar(int id, String estado) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_ESTADO_ORDEN(?,?)}");
            cs.setInt(1, id);
            cs.setString(2, estado);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error en modificar orden de abastecimiento: " + ex.getMessage());
        }
        return resultado;
    }

    @Override
    public int recibir_orden(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL RECIBIR_ORDEN_REABASTECIMIENTO(?)}");
            cs.setInt(1, id);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error al recibir orden de abastecimiento: " + ex.getMessage());
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
            cs = con.prepareCall("{CALL ELIMINAR_ORDEN(?)}");
            cs.setInt(1, id);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error en eliminar orden: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public OrdenReabastecimientoDto buscarPorID(int id) {
        OrdenReabastecimientoDto orden = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_ORDEN_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                orden = new OrdenReabastecimientoDto();
                orden.setIdOrden(rs.getInt("ORDEN_ID"));
                orden.setFechaEntregaEstimada(rs.getDate("FECHA_ENTREGA_ESTIMADA").toLocalDate());
                orden.setEstadoOrden(EstadoOrden.valueOf(rs.getString("ESTADO_ORDEN")));
                orden.setObservaciones(rs.getString("OBSERVACIONES"));

                ProveedorDto proveedor = new ProveedorDto();
                proveedor.setIdProveedor(rs.getInt("PROVEEDOR_ID"));
                orden.setProveedor(proveedor);

                TrabajadorDto trabajador = new TrabajadorDto();
                trabajador.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                orden.setTrabajador(trabajador);
            }
        } catch (Exception ex) {
            System.out.println("Error en buscar orden: " + ex.getMessage());
        } finally {
            try {
                cs.close();
            }catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try {
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return orden;
    }

    @Override
    public List<OrdenReabastecimientoDto> listarTodos() {
        List<OrdenReabastecimientoDto> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_ORDENES()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                OrdenReabastecimientoDto orden = new OrdenReabastecimientoDto();
                orden.setIdOrden(rs.getInt("ORDEN_ID"));
                orden.setFechaEntregaEstimada(rs.getDate("FECHA_ENTREGA_ESTIMADA").toLocalDate());
                orden.setEstadoOrden(EstadoOrden.valueOf(rs.getString("ESTADO_ORDEN")));
                orden.setObservaciones(rs.getString("OBSERVACIONES"));

                ProveedorDto proveedor = new ProveedorDto();
                proveedor.setIdProveedor(rs.getInt("PROVEEDOR_ID"));
                orden.setProveedor(proveedor);

                TrabajadorDto trabajador = new TrabajadorDto();
                trabajador.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                orden.setTrabajador(trabajador);

                lista.add(orden);
            }
        } catch (Exception ex) {
            System.out.println("Error en listar ordenes: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
        return lista;
    }
}
