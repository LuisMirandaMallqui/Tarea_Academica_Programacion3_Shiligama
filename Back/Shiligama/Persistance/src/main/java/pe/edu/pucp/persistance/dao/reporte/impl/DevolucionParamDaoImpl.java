package pe.edu.pucp.persistance.dao.reporte.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.persistance.dao.reporte.dao.DevolucionParamDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DevolucionParamDaoImpl implements DevolucionParamDao {

    @Override
    public String obtenerLogo() {
        return null; // El logo se carga desde el BO directamente como java.awt.Image
    }

    @Override
    public Integer obtenerDiasAlertaDefecto() {
        return 30;
    }

    @Override
    public BigDecimal obtenerTotalPerdidaReal(String fechaInicio, String fechaFin) {
        String sql = "SELECT COALESCE(SUM(dd.CANTIDAD * p.PRECIO_UNITARIO), 0) AS total " +
                     "FROM devolucion d " +
                     "JOIN detalle_devolucion dd ON d.DEVOLUCION_ID = dd.DEVOLUCION_ID " +
                     "JOIN producto p ON dd.PRODUCTO_ID = p.PRODUCTO_ID " +
                     "WHERE DATE(d.FECHA_HORA) BETWEEN STR_TO_DATE(?, '%Y-%m-%d') " +
                     "AND STR_TO_DATE(?, '%Y-%m-%d') AND d.ACTIVO = 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal("total");
            }
        } catch (Exception e) {
            System.err.println("Error obtenerTotalPerdidaReal: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal obtenerTotalRiesgo(Integer diasAlerta) {
        String sql = "SELECT COALESCE(SUM(l.CANTIDAD_ACTUAL * p.PRECIO_UNITARIO), 0) AS total " +
                     "FROM lote l JOIN producto p ON l.PRODUCTO_ID = p.PRODUCTO_ID " +
                     "WHERE l.ACTIVO = 1 AND l.CANTIDAD_ACTUAL > 0 " +
                     "AND l.FECHA_VENCIMIENTO <= DATE_ADD(CURDATE(), INTERVAL ? DAY)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diasAlerta != null ? diasAlerta : 30);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal("total");
            }
        } catch (Exception e) {
            System.err.println("Error obtenerTotalRiesgo: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Integer obtenerContadorPorEstado(String fechaInicio, String fechaFin, String estado) {
        boolean todos = "TODOS".equalsIgnoreCase(estado);
        String sql = "SELECT COUNT(*) AS total FROM devolucion d " +
                     "WHERE DATE(d.FECHA_HORA) BETWEEN STR_TO_DATE(?, '%Y-%m-%d') " +
                     "AND STR_TO_DATE(?, '%Y-%m-%d') AND d.ACTIVO = 1" +
                     (todos ? "" : " AND d.ESTADO_DEVOLUCION = ?");
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            if (!todos) ps.setString(3, estado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (Exception e) {
            System.err.println("Error obtenerContadorPorEstado: " + e.getMessage());
        }
        return 0;
    }
}
