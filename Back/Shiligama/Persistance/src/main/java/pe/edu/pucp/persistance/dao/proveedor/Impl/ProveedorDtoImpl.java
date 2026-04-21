package pe.edu.pucp.persistance.dao.proveedor.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.proveedor.ProveedorDto;
import pe.edu.pucp.persistance.dao.proveedor.dao.ProveedorDtoDAO;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDtoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDtoImpl implements ProveedorDtoDAO{
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;


    @Override
    public int insertar(ProveedorDto obj) {
        int resultado = 0;
        try{
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_PROVEEDOR(?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, obj.getRazonSocial());
            cs.setString(3, obj.getRuc());
            cs.setString(4, obj.getTelefono());
            cs.setString(5, obj.getEmail());
            cs.setString(6, obj.getDireccion());
            cs.setString(7, obj.getContacto());
            cs.execute();
            obj.setIdProveedor(cs.getInt(1));
            resultado = 1;
        }catch(Exception ex){
            System.out.println("Erro en insertar proveedor: " + ex.getMessage());
        }finally{
            try{
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try{
                con.close();
            }catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(ProveedorDto obj) {
        int resultado = 0;
        try{
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{MODIFICAR_PROVEEDOR(?,?,?,?,?,?,?)}");
            cs.setInt(1, obj.getIdProveedor());
            cs.setString(2, obj.getRazonSocial());
            cs.setString(3, obj.getRuc());
            cs.setString(4, obj.getTelefono());
            cs.setString(5, obj.getEmail());
            cs.setString(6, obj.getDireccion());
            cs.setString(7, obj.getContacto());

        }catch (Exception ex){
            System.out.println("Erro en modificar proveedor: " + ex.getMessage());
        } finally{
            try{
                cs.close();
            }catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            try{
                con.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try{
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL ELIMINAR_PROVEEDOR(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.out.println("Erro en eliminar proveedor: " + ex.getMessage());;
        } finally{
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            try{
                con.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public ProveedorDto buscarPorID(int id) {
        ProveedorDto prove = null;
        int resultado = 0;
        try{
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_PROVEEDOR_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if(rs.next()){
                prove = new ProveedorDto();
                prove.setIdProveedor(rs.getInt("PROVEEDOR_ID"));
                prove.setRazonSocial(rs.getString("RAZON_SOCIAL"));
                prove.setRuc(rs.getString("RUC"));
                prove.setTelefono(rs.getString("TELEFONO"));
                prove.setEmail(rs.getString("EMAIL"));
                prove.setDireccion(rs.getString("DIRECCION"));
                prove.setContacto(rs.getString("CONTACTO"));
                prove.setEstado(rs.getBoolean("ACTIVO"));
                resultado = 1;
            }
        }catch (Exception ex){
            System.out.println("Error en buscar proveedor: " + ex.getMessage());
        }finally {
            try{
                cs.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());;
            }
            try{
                con.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return prove;
    }

    @Override
    public List<ProveedorDto> listarTodos() {
        List<ProveedorDto> lista = new ArrayList<>();
        try{
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_PROVEEDORES()}");
            ResultSet rs = cs.executeQuery();
            while(rs.next()){
                ProveedorDto prove = new ProveedorDto();
                prove.setIdProveedor(rs.getInt("PROVEEDOR_ID"));
                prove.setRazonSocial(rs.getString("RAZON_SOCIAL"));
                prove.setRuc(rs.getString("RUC"));
                prove.setTelefono(rs.getString("TELEFONO"));
                prove.setEmail(rs.getString("EMAIL"));
                prove.setDireccion(rs.getString("DIRECCION"));
                prove.setContacto(rs.getString("CONTACTO"));
                prove.setEstado(rs.getBoolean("ACTIVO"));
                lista.add(prove);
            }
        }catch(Exception ex){
            System.out.println("Erro en listar proveedores: " + ex.getMessage());
        }
        finally{
            try{
                cs.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            try{
                con.close();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return lista;
    }
}
