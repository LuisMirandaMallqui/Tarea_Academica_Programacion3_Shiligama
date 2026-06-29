package pe.edu.pucp.persistance.dao.reporte.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.persistance.dao.reporte.dao.VentasParamDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VentasParamDaoImpl implements VentasParamDao {

    @Override
    public String obtenerLogo() {
        return null; // Sin logo configurado en BD actualmente
    }

    @Override
    public String obtenerAgrupacionDefecto() {
        return "DIA";
    }

    @Override
    public Double obtenerSumaPorCanal(String fechaInicio, String fechaFin, String canal) {
        String sql = "SELECT COALESCE(SUM(v.MONTO_TOTAL - v.MONTO_DESCUENTO), 0) AS total " +
                     "FROM venta v " +
                     "WHERE v.FECHA_HORA BETWEEN STR_TO_DATE(?, '%Y-%m-%d') " +
                     "AND STR_TO_DATE(?, '%Y-%m-%d 23:59:59') " +
                     "AND v.ESTADO_VENTA = 'COMPLETADA' AND v.ACTIVO = 1" +
                     (canal != null ? " AND v.CANAL_VENTA = ?" : "");
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            if (canal != null) ps.setString(3, canal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (Exception e) {
            System.err.println("Error obtenerSumaPorCanal [" + canal + "]: " + e.getMessage());
        }
        return 0.0;
    }
}
