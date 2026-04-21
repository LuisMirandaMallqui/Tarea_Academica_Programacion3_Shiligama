package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.persistance.dao.venta.dao.MetodoPagoDtoDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDtoImpl implements MetodoPagoDtoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(MetodoPagoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_METODO_PAGO(?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, obj.getNombre());
            cs.execute();
            obj.setIdMetodoPago(cs.getInt(1));
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
    public int modificar(MetodoPagoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL MODIFICAR_METODO_PAGO(?, ?)}");
            cs.setInt(1, obj.getIdMetodoPago());
            cs.setString(2, obj.getNombre());
            cs.execute();
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
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL ELIMINAR_METODO_PAGO(?)}");
            cs.setInt(1, id);
            cs.execute();
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
    public MetodoPagoDto buscarPorID(int id) {
        MetodoPagoDto metodoPago = null;
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_METODO_PAGO_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                metodoPago = new MetodoPagoDto();
                metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
                metodoPago.setNombre(rs.getString("NOMBRE"));
                metodoPago.setEstado(rs.getBoolean("ACTIVO"));
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
        return metodoPago;
    }

    @Override
    public List<MetodoPagoDto> listarTodos() {
        List<MetodoPagoDto> lista = new ArrayList<>();
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_METODOS_PAGO()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                MetodoPagoDto metodoPago = new MetodoPagoDto();
                metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
                metodoPago.setNombre(rs.getString("NOMBRE"));
                metodoPago.setEstado(rs.getBoolean("ACTIVO"));
                lista.add(metodoPago);
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
        return lista;
    }
}
