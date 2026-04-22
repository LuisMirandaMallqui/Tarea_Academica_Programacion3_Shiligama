package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.venta.PedidoDto;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDtoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDtoImpl implements PedidoDtoDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;
    @Override
    public int insertar(PedidoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL INSERTAR_PEDIDO(?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, obj.getCliente().getIdCliente());
            cs.setString(3, obj.getDireccionEntrega());
            cs.setString(4, obj.getModalidadVenta().name());
            cs.setString(5, obj.getObservaciones());
            cs.execute();
            obj.setIdPedido(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar Pedido: " + ex.getMessage());
        }finally {
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
    public int modificar(PedidoDto obj) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL MODIFICAR_ESTADO_PEDIDO(?, ?)}");
            cs.setInt(1, obj.getIdPedido());
            cs.setString(2, obj.getEstadoPedido().name());
            cs.execute();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar Pedido: " + ex.getMessage());
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
            cs = con.prepareCall("{CALL ELIMINAR_PEDIDO(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar Pedido: " + ex.getMessage());
        }  finally {
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
    public PedidoDto buscarPorID(int id) {
        PedidoDto pedido = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL BUSCAR_PEDIDO_POR_ID(?)}");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                pedido = new PedidoDto();
                pedido.setIdPedido(rs.getInt("PEDIDO_ID"));
                pedido.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
                pedido.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
                //pedido.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
                pedido.setEstadoPedido(EstadoPedido.valueOf(rs.getString("ESTADO_PEDIDO")));
                pedido.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));
                pedido.setModalidadVenta(ModalidadVenta.valueOf(rs.getString("MODALIDAD_ENTREGA")));
                pedido.setObservaciones(rs.getString("OBSERVACIONES"));

                ClienteDto cliente = new ClienteDto();
                cliente.setIdCliente(rs.getInt("CLIENTE_ID"));
                pedido.setCliente(cliente);
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorId Pedido: " + ex.getMessage());
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
        return pedido;
    }

    @Override
    public List<PedidoDto> listarTodos() {
        List<PedidoDto> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL LISTAR_PEDIDOS()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                PedidoDto pedido = new PedidoDto();
                pedido.setIdPedido(rs.getInt("PEDIDO_ID"));
                pedido.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
                pedido.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
                //pedido.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
                pedido.setEstadoPedido(EstadoPedido.valueOf(rs.getString("ESTADO_PEDIDO")));
                pedido.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));
                pedido.setModalidadVenta(ModalidadVenta.valueOf(rs.getString("MODALIDAD_ENTREGA")));
                pedido.setObservaciones(rs.getString("OBSERVACIONES"));

                ClienteDto cliente = new ClienteDto();
                cliente.setIdCliente(rs.getInt("CLIENTE_ID"));
                pedido.setCliente(cliente);

                lista.add(pedido);
            }
        } catch (Exception ex) {
            System.err.println("Error en listar Pedido: " + ex.getMessage());
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
