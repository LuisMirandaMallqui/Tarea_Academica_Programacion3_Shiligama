package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.model.venta.BoletaDto;
import pe.edu.pucp.model.venta.DetalleVentaDto;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.model.venta.VentaDto;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentaDaoImpl implements VentaDao {

    // -------------------------------------------------------------------------
    // INSERT con transaccion — SP: INSERTAR_VENTA + INSERTAR_DETALLE_VENTA
    // Usa transacciones del DBManager para insertar cabecera + detalles
    // -------------------------------------------------------------------------
    @Override
    public int insertar(VentaDto venta) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // Insertar cabecera de venta
            Map<Integer, Object> paramsEntrada = new HashMap<>();
            Map<Integer, Object> paramsSalida = new HashMap<>();

            paramsSalida.put(1, Types.INTEGER);
            paramsEntrada.put(2, venta.getCliente().getIdCliente());
            paramsEntrada.put(3, venta.getTrabajador().getIdTrabajador());
            paramsEntrada.put(4, venta.getMetodoPago().getIdMetodoPago());
            paramsEntrada.put(5, venta.getCanalVenta().name());
            paramsEntrada.put(6, venta.getObservaciones());

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_VENTA", paramsEntrada, paramsSalida);
            venta.setIdVenta((int) paramsSalida.get(1));

            // Insertar detalles
            if (venta.getDetalles() != null) {
                for (DetalleVentaDto detalle : venta.getDetalles()) {
                    Map<Integer, Object> paramsDetEntrada = new HashMap<>();
                    Map<Integer, Object> paramsDetSalida = new HashMap<>();

                    paramsDetSalida.put(1, Types.INTEGER);
                    paramsDetEntrada.put(2, venta.getIdVenta());
                    paramsDetEntrada.put(3, detalle.getProducto().getIdProducto());
                    paramsDetEntrada.put(4, detalle.getCantidad());

                    dbManager.ejecutarProcedimientoTransaccion(
                            "INSERTAR_DETALLE_VENTA", paramsDetEntrada, paramsDetSalida);
                    detalle.setIdDetalleVenta((int) paramsDetSalida.get(1));
                }
            }

            dbManager.confirmarTransaccion();
            resultado = venta.getIdVenta();
        } catch (SQLException ex) {
            System.out.println("Error al insertar venta: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    // SP: COMPLETAR_VENTA(IN _venta_id)
    @Override
    public int modificar(VentaDto venta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, venta.getIdVenta());
        return DBManager.getInstance().ejecutarProcedimiento(
                "COMPLETAR_VENTA", parametrosEntrada, null);
    }

    // SP: ANULAR_VENTA(IN _venta_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ANULAR_VENTA", parametrosEntrada, null);
    }

    // SP: BUSCAR_VENTA_X_ID(IN _venta_id)
    @Override
    public VentaDto buscarPorID(int id) {
        VentaDto venta = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_VENTA_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    String numeroBoleta = rs.getString("NUMERO_BOLETA");
                    if (numeroBoleta != null && !numeroBoleta.isEmpty()) {
                        venta = mapearBoleta(rs, numeroBoleta);
                    } else {
                        venta = mapearVenta(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar venta: " + ex.getMessage());
        }
        return venta;
    }

    // SP: LISTAR_VENTAS()
    @Override
    public List<VentaDto> listarTodos() {
        List<VentaDto> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_VENTAS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    String numeroBoleta = rs.getString("NUMERO_BOLETA");
                    if (numeroBoleta != null && !numeroBoleta.isEmpty()) {
                        lista.add(mapearBoleta(rs, numeroBoleta));
                    } else {
                        lista.add(mapearVenta(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar ventas: " + ex.getMessage());
        }
        return lista;
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet
    // -------------------------------------------------------------------------
    private VentaDto mapearVenta(ResultSet rs) throws SQLException {
        VentaDto v = new VentaDto();
        completarCamposVenta(rs, v);
        return v;
    }

    private BoletaDto mapearBoleta(ResultSet rs, String numeroBoleta) throws SQLException {
        BoletaDto boleta = new BoletaDto();
        completarCamposVenta(rs, boleta);
        boleta.setNumeroBoleta(numeroBoleta);
        boleta.setRuc(rs.getString("RUC_EMPRESA"));
        boleta.setContactoCliente(rs.getString("CONTACTO_CLIENTE"));
        boleta.setMensajeBoleta(rs.getString("MENSAJE_BOLETA"));
        return boleta;
    }

    private void completarCamposVenta(ResultSet rs, VentaDto v) throws SQLException {
        v.setIdVenta(rs.getInt("VENTA_ID"));
        v.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
        v.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
        v.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
        v.setCanalVenta(CanalVenta.valueOf(rs.getString("CANAL_VENTA")));
        v.setEstadoVenta(EstadoVenta.valueOf(rs.getString("ESTADO_VENTA")));
        v.setObservaciones(rs.getString("OBSERVACIONES"));

        ClienteDto cliente = new ClienteDto();
        cliente.setIdCliente(rs.getInt("CLIENTE_ID"));
        v.setCliente(cliente);

        TrabajadorDto trabajador = new TrabajadorDto();
        trabajador.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
        v.setTrabajador(trabajador);

        MetodoPagoDto metodoPago = new MetodoPagoDto();
        metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
        metodoPago.setNombre(rs.getString("METODO_PAGO_NOMBRE"));
        v.setMetodoPago(metodoPago);
    }
}
