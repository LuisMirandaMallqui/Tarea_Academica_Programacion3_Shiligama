package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.model.venta.VentaDto;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDtoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDtoImpl implements VentaDtoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(VentaDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_VENTA(?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, obj.getCliente().getIdCliente());
            cs.setInt(3, obj.getTrabajador().getIdTrabajador());
            cs.setInt(4, obj.getMetodoPago().getIdMetodoPago());
            cs.setString(5, obj.getCanalVenta().name());
            cs.setString(6, obj.getObservaciones());
            cs.execute();
            obj.setIdVenta(cs.getInt(1));
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
    public int modificar(VentaDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL COMPLETAR_VENTA(?)}");
            cs.setInt(1, obj.getIdVenta());
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
            cs = con.prepareCall("{CALL ANULAR_VENTA(?)}");
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
    public VentaDto buscarPorID(int id) {
        VentaDto venta = null;
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_VENTA_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                venta = new VentaDto();
                venta.setIdVenta(rs.getInt("VENTA_ID"));
                venta.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
                venta.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
                venta.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
                venta.setCanalVenta(CanalVenta.valueOf(rs.getString("CANAL_VENTA")));
                venta.setEstadoVenta(EstadoVenta.valueOf(rs.getString("ESTADO_VENTA")));
                venta.setObservaciones(rs.getString("OBSERVACIONES"));

                ClienteDto cliente = new ClienteDto();
                cliente.setIdCliente(rs.getInt("CLIENTE_ID"));
                venta.setCliente(cliente);

                TrabajadorDto trabajador = new TrabajadorDto();
                trabajador.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                venta.setTrabajador(trabajador);

                MetodoPagoDto metodoPago = new MetodoPagoDto();
                metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
                metodoPago.setNombre(rs.getString("METODO_PAGO_NOMBRE"));
                venta.setMetodoPago(metodoPago);
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
        return venta;
    }

    @Override
    public List<VentaDto> listarTodos() {
        List<VentaDto> lista = new ArrayList<>();
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_VENTAS()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                VentaDto venta = new VentaDto();
                venta.setIdVenta(rs.getInt("VENTA_ID"));
                venta.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
                venta.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
                venta.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
                venta.setCanalVenta(CanalVenta.valueOf(rs.getString("CANAL_VENTA")));
                venta.setEstadoVenta(EstadoVenta.valueOf(rs.getString("ESTADO_VENTA")));
                venta.setObservaciones(rs.getString("OBSERVACIONES"));

                ClienteDto cliente = new ClienteDto();
                cliente.setIdCliente(rs.getInt("CLIENTE_ID"));
                venta.setCliente(cliente);

                TrabajadorDto trabajador = new TrabajadorDto();
                trabajador.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
                venta.setTrabajador(trabajador);

                MetodoPagoDto metodoPago = new MetodoPagoDto();
                metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
                metodoPago.setNombre(rs.getString("METODO_PAGO_NOMBRE"));
                venta.setMetodoPago(metodoPago);

                lista.add(venta);
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
