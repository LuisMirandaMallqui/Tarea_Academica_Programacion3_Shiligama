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
import java.time.LocalDateTime;

public class PedidoDaoImpl implements PedidoDao {

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

    @Override
    public int modificar(Pedido pedido) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, pedido.getIdPedido());
        parametrosEntrada.put(2, pedido.getEstadoPedido().name());
        parametrosEntrada.put(3, pedido.getObservaciones());
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_ESTADO_PEDIDO", parametrosEntrada, null);
    }

    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_PEDIDO", parametrosEntrada, null);
    }

    @Override
    public Pedido buscarPorId(int id) {
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

    @Override
    public int confirmarPedidoAVenta(int idPedido, int idTrabajador, int idMetodoPago) {
        Map<Integer, Object> parametrosSalida = new HashMap<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosSalida.put(1, Types.INTEGER); // OUT p_venta_id
        parametrosEntrada.put(2, idPedido);     // IN  p_pedido_id
        parametrosEntrada.put(3, idTrabajador); // IN  p_trabajador_id
        parametrosEntrada.put(4, idMetodoPago); // IN  p_metodo_pago_id (fallback)

        DBManager.getInstance().ejecutarProcedimiento(
                "CONFIRMAR_PEDIDO_A_VENTA", parametrosEntrada, parametrosSalida);

        Object result = parametrosSalida.get(1);
        return result != null ? ((Number) result).intValue() : 0;
    }

    private LocalDateTime getLocalDateTime(ResultSet rs, String column) throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("PEDIDO_ID"));
        p.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
        p.setFechaRecibido(getLocalDateTime(rs, "FECHA_RECIBIDO"));
        p.setFechaEnPreparacion(getLocalDateTime(rs, "FECHA_EN_PREPARACION"));
        p.setFechaListo(getLocalDateTime(rs, "FECHA_LISTO"));
        p.setFechaEnCamino(getLocalDateTime(rs, "FECHA_EN_CAMINO"));
        p.setFechaEntregado(getLocalDateTime(rs, "FECHA_ENTREGADO"));
        p.setFechaRecogido(getLocalDateTime(rs, "FECHA_RECOGIDO"));
        p.setFechaCancelado(getLocalDateTime(rs, "FECHA_CANCELADO"));
        p.setMontoTotal(rs.getDouble("MONTO_TOTAL"));

        // Proteger contra valores null o inesperados en ESTADO_PEDIDO
        String estadoStr = rs.getString("ESTADO_PEDIDO");
        try {
            p.setEstadoPedido(estadoStr != null ? EstadoPedido.valueOf(estadoStr) : EstadoPedido.RECIBIDO);
        } catch (IllegalArgumentException e) {
            p.setEstadoPedido(EstadoPedido.RECIBIDO);
        }

        p.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));

        // FIX: proteger contra null en MODALIDAD_ENTREGA — si es null o valor
        // desconocido, usar DELIVERY como fallback en vez de lanzar excepción.
        String modalidadStr = rs.getString("MODALIDAD_ENTREGA");
        try {
            p.setModalidadVenta(modalidadStr != null ? ModalidadVenta.valueOf(modalidadStr) : ModalidadVenta.DELIVERY);
        } catch (IllegalArgumentException e) {
            p.setModalidadVenta(ModalidadVenta.DELIVERY);
        }

        p.setObservaciones(rs.getString("OBSERVACIONES"));
        try { p.setTotalItems(rs.getInt("TOTAL_ITEMS")); } catch (SQLException ignored) {}

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("CLIENTE_ID"));
        cliente.setNombres(rs.getString("CLIENTE_NOMBRES"));
        cliente.setApellidos(rs.getString("CLIENTE_APELLIDOS"));
        p.setCliente(cliente);

        return p;
    }

    @Override
    public String reservarStockParaPago(int idPedido) {
        Map<Integer, Object> parametrosSalida = new HashMap<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);  // OUT _exito
        parametrosSalida.put(2, Types.VARCHAR);  // OUT _mensaje
        parametrosEntrada.put(3, idPedido);      // IN _pedido_id

        DBManager.getInstance().ejecutarProcedimiento(
                "RESERVAR_STOCK_PEDIDO", parametrosEntrada, parametrosSalida);

        Object exitoObj = parametrosSalida.get(1);
        int exito = exitoObj != null ? ((Number) exitoObj).intValue() : 0;

        String mensaje = parametrosSalida.get(2) != null
                ? parametrosSalida.get(2).toString()
                : "No se pudo reservar stock para el pedido.";

        return exito == 1 ? null : mensaje;
    }

    @Override
    public int restaurarStockReservado(int idPedido) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPedido);

        return DBManager.getInstance().ejecutarProcedimiento(
                "RESTAURAR_STOCK_PEDIDO", parametrosEntrada, null);
    }
}
