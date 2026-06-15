package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.EstadoPago;
import pe.edu.pucp.model.venta.Pago;
import pe.edu.pucp.persistance.dao.venta.dao.PagoDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagoDaoImpl implements PagoDao {

    // SP: INSERTAR_PAGO(OUT _pago_id, IN _pedido_id, _metodo_pago_id,
    //                   _monto, _moneda, _estado, _referencia, _order_id)
    @Override
    public int insertar(Pago pago) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, pago.getIdPedido());
        parametrosEntrada.put(3, pago.getIdMetodoPago());
        parametrosEntrada.put(4, pago.getMonto());
        parametrosEntrada.put(5, pago.getMoneda() != null ? pago.getMoneda() : "PEN");
        parametrosEntrada.put(6, pago.getEstado() != null
                ? pago.getEstado().name() : EstadoPago.PENDIENTE.name());
        parametrosEntrada.put(7, pago.getReferencia());
        parametrosEntrada.put(8, pago.getOrderId());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_PAGO", parametrosEntrada, parametrosSalida);
        pago.setIdPago((int) parametrosSalida.get(1));
        return pago.getIdPago();
    }

    // SP: MODIFICAR_PAGO(IN _pago_id, _estado, _referencia)
    @Override
    public int modificar(Pago pago) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, pago.getIdPago());
        parametrosEntrada.put(2, pago.getEstado() != null
                ? pago.getEstado().name() : EstadoPago.PENDIENTE.name());
        parametrosEntrada.put(3, pago.getReferencia());
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_PAGO", parametrosEntrada, null);
    }

    // SP: MODIFICAR_PAGO_X_ORDER(IN _order_id, _estado, _referencia)
    @Override
    public int modificarPorOrder(String orderId, EstadoPago estado, String referencia) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, orderId);
        parametrosEntrada.put(2, estado != null ? estado.name() : EstadoPago.PENDIENTE.name());
        parametrosEntrada.put(3, referencia);
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_PAGO_X_ORDER", parametrosEntrada, null);
    }

    // SP: ELIMINAR_PAGO(IN _pago_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_PAGO", parametrosEntrada, null);
    }

    // SP: BUSCAR_PAGO_X_ID(IN _pago_id)
    @Override
    public Pago buscarPorId(int id) {
        Pago pago = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PAGO_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    pago = mapearPago(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar pago: " + ex.getMessage());
        }
        return pago;
    }

    // SP: BUSCAR_PAGO_X_PEDIDO(IN _pedido_id) -> devuelve el pago más reciente
    @Override
    public Pago buscarPorPedido(int idPedido) {
        Pago pago = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPedido);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PAGO_X_PEDIDO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    pago = mapearPago(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar pago por pedido: " + ex.getMessage());
        }
        return pago;
    }

    // SP: BUSCAR_PAGO_X_PEDIDO(IN _pedido_id) -> todos los pagos del pedido
    @Override
    public List<Pago> listarPorPedido(int idPedido) {
        List<Pago> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idPedido);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PAGO_X_PEDIDO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPago(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pagos por pedido: " + ex.getMessage());
        }
        return lista;
    }

    // SP: BUSCAR_PAGO_X_ORDER(IN _order_id)
    @Override
    public Pago buscarPorOrder(String orderId) {
        Pago pago = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, orderId);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_PAGO_X_ORDER", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    pago = mapearPago(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar pago por order: " + ex.getMessage());
        }
        return pago;
    }

    // SP: LISTAR_PAGOS()
    @Override
    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PAGOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearPago(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pagos: " + ex.getMessage());
        }
        return lista;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("PAGO_ID"));
        pago.setIdPedido(rs.getInt("PEDIDO_ID"));
        pago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
        pago.setMonto(rs.getDouble("MONTO"));
        pago.setMoneda(rs.getString("MONEDA"));
        String estado = rs.getString("ESTADO");
        if (estado != null) {
            pago.setEstado(EstadoPago.valueOf(estado));
        }
        pago.setReferencia(rs.getString("REFERENCIA"));
        pago.setOrderId(rs.getString("ORDER_ID"));
        Timestamp fp = rs.getTimestamp("FECHA_PAGO");
        if (fp != null) {
            pago.setFechaPago(fp.toLocalDateTime());
        }
        return pago;
    }
}
