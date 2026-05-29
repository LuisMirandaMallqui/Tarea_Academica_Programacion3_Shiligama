package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.model.venta.MetodoPago;
import pe.edu.pucp.persistance.dao.venta.dao.BoletaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoletaDaoImpl implements BoletaDao {

    // SP: EMITIR_BOLETA(IN _venta_id, IN _ruc, IN _contacto, IN _mensaje, OUT _numero)
    @Override
    public String emitir(int idVenta, String ruc, String contactoCliente, String mensajeBoleta) {
        String numeroGenerado = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosEntrada.put(1, idVenta);
        parametrosEntrada.put(2, ruc);
        parametrosEntrada.put(3, contactoCliente);
        parametrosEntrada.put(4, mensajeBoleta);
        parametrosSalida.put(5, Types.VARCHAR);

        DBManager.getInstance().ejecutarProcedimiento(
                "EMITIR_BOLETA", parametrosEntrada, parametrosSalida);
        Object out = parametrosSalida.get(5);
        if (out != null) {
            numeroGenerado = out.toString();
        }
        return numeroGenerado;
    }

    // SP: BUSCAR_BOLETA_X_VENTA(IN _venta_id)
    @Override
    public Boleta buscarPorIdVenta(int idVenta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idVenta);
        return ejecutarYMapearUno("BUSCAR_BOLETA_X_VENTA", parametrosEntrada);
    }

    // SP: BUSCAR_BOLETA_X_NUMERO(IN _numero)
    @Override
    public Boleta buscarPorNumero(String numeroBoleta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, numeroBoleta);
        return ejecutarYMapearUno("BUSCAR_BOLETA_X_NUMERO", parametrosEntrada);
    }

    // SP: LISTAR_BOLETAS()
    @Override
    public List<Boleta> listarTodas() {
        List<Boleta> lista = new ArrayList<>();
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_BOLETAS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearBoleta(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar boletas: " + ex.getMessage());
        }
        return lista;
    }

    // SP: ANULAR_BOLETA(IN _venta_id)
    @Override
    public int anular(int idVenta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idVenta);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ANULAR_BOLETA", parametrosEntrada, null);
    }

    // ---- helpers ----

    private Boleta ejecutarYMapearUno(String sp, Map<Integer, Object> parametros) {
        Boleta boleta = null;
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura(sp, parametros)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    boleta = mapearBoleta(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en " + sp + ": " + ex.getMessage());
        }
        return boleta;
    }

    private Boleta mapearBoleta(ResultSet rs) throws SQLException {
        Boleta b = new Boleta();
        b.setIdVenta(rs.getInt("VENTA_ID"));
        b.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
        b.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
        b.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
        b.setCanalVenta(CanalVenta.valueOf(rs.getString("CANAL_VENTA")));
        b.setEstadoVenta(EstadoVenta.valueOf(rs.getString("ESTADO_VENTA")));
        b.setObservaciones(rs.getString("OBSERVACIONES"));

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("CLIENTE_ID"));
        b.setCliente(cliente);

        Trabajador trabajador = new Trabajador();
        trabajador.setIdUsuario(rs.getInt("TRABAJADOR_ID"));
        b.setTrabajador(trabajador);

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
        metodoPago.setNombre(rs.getString("METODO_PAGO_NOMBRE"));
        b.setMetodoPago(metodoPago);

        b.setNumeroBoleta(rs.getString("NUMERO_BOLETA"));
        b.setRuc(rs.getString("RUC_EMPRESA"));
        b.setContactoCliente(rs.getString("CONTACTO_CLIENTE"));
        b.setMensajeBoleta(rs.getString("MENSAJE_BOLETA"));
        return b;
    }
}
