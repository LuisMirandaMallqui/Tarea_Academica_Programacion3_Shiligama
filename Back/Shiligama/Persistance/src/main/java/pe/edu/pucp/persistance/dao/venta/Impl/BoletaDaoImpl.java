package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.model.venta.DetalleBoleta;
import pe.edu.pucp.persistance.dao.venta.dao.BoletaDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoletaDaoImpl implements BoletaDao {

    @Override
    public int insertar(Boleta boleta) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // Insert boleta header
            Map<Integer, Object> paramsEntrada = new HashMap<>();
            Map<Integer, Object> paramsSalida = new HashMap<>();

            paramsSalida.put(1, Types.BIGINT);
            paramsEntrada.put(2, boleta.getVentaId());
            paramsEntrada.put(3, boleta.getSerie());
            paramsEntrada.put(4, boleta.getNumero());
            paramsEntrada.put(5, java.sql.Date.valueOf(boleta.getFechaEmision()));
            paramsEntrada.put(6, boleta.getClienteTipoDocumento());
            paramsEntrada.put(7, boleta.getClienteNumeroDocumento());
            paramsEntrada.put(8, boleta.getClienteDenominacion());
            paramsEntrada.put(9, boleta.getClienteDireccion());
            paramsEntrada.put(10, boleta.getClienteEmail());
            paramsEntrada.put(11, boleta.getMoneda());
            paramsEntrada.put(12, boleta.getPorcentajeIgv());
            paramsEntrada.put(13, boleta.getTotalGravada());
            paramsEntrada.put(14, boleta.getTotalIgv());
            paramsEntrada.put(15, boleta.getTotal());
            paramsEntrada.put(16, boleta.getNubefactEnlace());
            paramsEntrada.put(17, boleta.getNubefactEnlacePdf());
            paramsEntrada.put(18, boleta.getNubefactEnlaceXml());
            paramsEntrada.put(19, boleta.getNubefactEnlaceCdr());
            paramsEntrada.put(20, boleta.getNubefactCadenaQr());
            paramsEntrada.put(21, boleta.getNubefactCodigoHash());
            paramsEntrada.put(22, boleta.getAceptadaPorSunat());
            paramsEntrada.put(23, boleta.getSunatResponseCode());
            paramsEntrada.put(24, boleta.getSunatDescription());
            paramsEntrada.put(25, boleta.getUsuarioRegistro());

            dbManager.ejecutarProcedimientoTransaccion(
                    "sp_InsertarBoleta", paramsEntrada, paramsSalida);
            long generatedId = (long) paramsSalida.get(1);
            boleta.setId(generatedId);

            // Insert details
            if (boleta.getDetalles() != null) {
                for (DetalleBoleta detalle : boleta.getDetalles()) {
                    Map<Integer, Object> paramsDetEntrada = new HashMap<>();
                    Map<Integer, Object> paramsDetSalida = new HashMap<>();

                    paramsDetSalida.put(1, Types.BIGINT);
                    paramsDetEntrada.put(2, generatedId);
                    paramsDetEntrada.put(3, detalle.getIdProducto());
                    paramsDetEntrada.put(4, detalle.getUnidadMedida());
                    paramsDetEntrada.put(5, detalle.getDescripcion());
                    paramsDetEntrada.put(6, detalle.getCantidad());
                    paramsDetEntrada.put(7, detalle.getValorUnitario());
                    paramsDetEntrada.put(8, detalle.getPrecioUnitario());
                    paramsDetEntrada.put(9, detalle.getSubtotal());
                    paramsDetEntrada.put(10, detalle.getIgv());
                    paramsDetEntrada.put(11, detalle.getTotal());

                    dbManager.ejecutarProcedimientoTransaccion(
                            "sp_InsertarDetalleBoleta", paramsDetEntrada, paramsDetSalida);
                    detalle.setId((long) paramsDetSalida.get(1));
                    detalle.setIdBoleta(generatedId);
                }
            }

            dbManager.confirmarTransaccion();
            resultado = (int) generatedId;
        } catch (SQLException ex) {
            System.out.println("Error al insertar boleta: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    @Override
    public Boleta buscarPorVentaId(int ventaId) {
        Boleta boleta = null;
        String sql = "SELECT * FROM boleta WHERE venta_id = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boleta = mapearBoleta(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar boleta por ventaId: " + ex.getMessage());
        }
        if (boleta != null) {
            boleta.setDetalles(buscarDetallesPorBoletaId(boleta.getId()));
        }
        return boleta;
    }

    @Override
    public Boleta buscarPorSerieNumero(String serie, int numero) {
        Boleta boleta = null;
        String sql = "SELECT * FROM boleta WHERE serie = ? AND numero = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, serie);
            ps.setInt(2, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boleta = mapearBoleta(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar boleta por serie y numero: " + ex.getMessage());
        }
        if (boleta != null) {
            boleta.setDetalles(buscarDetallesPorBoletaId(boleta.getId()));
        }
        return boleta;
    }

    @Override
    public int obtenerSiguienteNumero(String serie) {
        int siguiente = 1;
        Map<Integer, Object> paramsEntrada = new HashMap<>();
        paramsEntrada.put(1, serie);
        try (DBManager.ResultadoConsulta res = DBManager.getInstance()
                .ejecutarProcedimientoLectura("sp_ObtenerSiguienteNumeroBoleta", paramsEntrada)) {
            if (res != null) {
                ResultSet rs = res.getRs();
                if (rs.next()) {
                    siguiente = rs.getInt("siguiente_numero");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener siguiente numero de boleta: " + ex.getMessage());
        }
        return siguiente;
    }

    private List<DetalleBoleta> buscarDetallesPorBoletaId(long boletaId) {
        List<DetalleBoleta> lista = new ArrayList<>();
        String sql = "SELECT * FROM boleta_detalle WHERE id_boleta = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, boletaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleBoleta det = new DetalleBoleta();
                    det.setId(rs.getLong("id"));
                    det.setIdBoleta(rs.getLong("id_boleta"));
                    det.setIdProducto(rs.getObject("id_producto") != null ? rs.getLong("id_producto") : null);
                    det.setUnidadMedida(rs.getString("unidad_medida"));
                    det.setDescripcion(rs.getString("descripcion"));
                    det.setCantidad(rs.getDouble("cantidad"));
                    det.setValorUnitario(rs.getDouble("valor_unitario"));
                    det.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    det.setSubtotal(rs.getDouble("subtotal"));
                    det.setIgv(rs.getDouble("igv"));
                    det.setTotal(rs.getDouble("total"));
                    lista.add(det);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar detalles de boleta: " + ex.getMessage());
        }
        return lista;
    }

    private Boleta mapearBoleta(ResultSet rs) throws SQLException {
        Boleta b = new Boleta();
        b.setId(rs.getLong("id"));
        b.setVentaId(rs.getInt("venta_id"));
        b.setSerie(rs.getString("serie"));
        b.setNumero(rs.getInt("numero"));
        b.setFechaEmision(rs.getDate("fecha_emision").toLocalDate());
        b.setClienteTipoDocumento(rs.getString("cliente_tipo_documento"));
        b.setClienteNumeroDocumento(rs.getString("cliente_numero_documento"));
        b.setClienteDenominacion(rs.getString("cliente_denominacion"));
        b.setClienteDireccion(rs.getString("cliente_direccion"));
        b.setClienteEmail(rs.getString("cliente_email"));
        b.setMoneda(rs.getInt("moneda"));
        b.setPorcentajeIgv(rs.getDouble("porcentaje_igv"));
        b.setTotalGravada(rs.getDouble("total_gravada"));
        b.setTotalIgv(rs.getDouble("total_igv"));
        b.setTotal(rs.getDouble("total"));
        b.setNubefactEnlace(rs.getString("nubefact_enlace"));
        b.setNubefactEnlacePdf(rs.getString("nubefact_enlace_pdf"));
        b.setNubefactEnlaceXml(rs.getString("nubefact_enlace_xml"));
        b.setNubefactEnlaceCdr(rs.getString("nubefact_enlace_cdr"));
        b.setNubefactCadenaQr(rs.getString("nubefact_cadena_qr"));
        b.setNubefactCodigoHash(rs.getString("nubefact_codigo_hash"));
        b.setAceptadaPorSunat(rs.getBoolean("aceptada_por_sunat"));
        b.setSunatResponseCode(rs.getString("sunat_response_code"));
        b.setSunatDescription(rs.getString("sunat_description"));
        b.setAnulado(rs.getBoolean("anulado"));
        b.setAnulacionMotivo(rs.getString("anulacion_motivo"));
        b.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        b.setUsuarioRegistro(rs.getString("usuario_registro"));
        return b;
    }
}
