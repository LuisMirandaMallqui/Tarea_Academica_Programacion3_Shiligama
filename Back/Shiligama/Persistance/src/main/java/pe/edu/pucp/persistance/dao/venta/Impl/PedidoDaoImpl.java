package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoDaoImpl implements PedidoDao {

    // SP: INSERTAR_PEDIDO(OUT _pedido_id, IN _cliente_id, IN _monto_total,
    //   IN _direccion_entrega, IN _modalidad_entrega, IN _observaciones)
    @Override
    public int insertar(Pedido pedido) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, pedido.getCliente().getIdUsuario());
        parametrosEntrada.put(3, pedido.getMontoTotal());
        parametrosEntrada.put(4, pedido.getDireccionEntrega());
        parametrosEntrada.put(5, pedido.getModalidadVenta().name());
        parametrosEntrada.put(6, pedido.getObservaciones());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_PEDIDO", parametrosEntrada, parametrosSalida);
        pedido.setIdPedido((int) parametrosSalida.get(1));
        return pedido.getIdPedido();
    }

    // SP: MODIFICAR_ESTADO_PEDIDO(IN _pedido_id, IN _estado_pedido)
    @Override
    public int modificar(Pedido pedido) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, pedido.getIdPedido());
        parametrosEntrada.put(2, pedido.getEstadoPedido().name());
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_ESTADO_PEDIDO", parametrosEntrada, null);
    }

    // SP: ELIMINAR_PEDIDO(IN _pedido_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_PEDIDO", parametrosEntrada, null);
    }

    // SP: BUSCAR_PEDIDO_X_ID(IN _pedido_id)
    @Override
    public Pedido buscarPorID(int id) {
        Pedido pedido = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PEDIDO_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    pedido = mapearPedido(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar pedido: " + ex.getMessage());
        }
        return pedido;
    }

    // SP: LISTAR_PEDIDOS()
    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PEDIDOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pedidos: " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_PEDIDOS_X_CLIENTE(IN _cliente_id)
    @Override
    public List<Pedido> listarPorCliente(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idCliente);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PEDIDOS_X_CLIENTE", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en listarPorCliente (pedidos): " + ex.getMessage());
        }
        return lista;
    }

    // SP: LISTAR_PEDIDOS_X_ESTADO(IN _estado VARCHAR)
    @Override
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        List<Pedido> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, estado.name());

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PEDIDOS_X_ESTADO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en listarPorEstado (pedidos): " + ex.getMessage());
        }
        return lista;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("PEDIDO_ID"));
        p.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
        p.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
        p.setEstadoPedido(EstadoPedido.valueOf(rs.getString("ESTADO_PEDIDO")));
        p.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));
        p.setModalidadVenta(ModalidadVenta.valueOf(rs.getString("MODALIDAD_ENTREGA")));
        p.setObservaciones(rs.getString("OBSERVACIONES"));

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("CLIENTE_ID"));
        p.setCliente(cliente);

        return p;
    }
}
