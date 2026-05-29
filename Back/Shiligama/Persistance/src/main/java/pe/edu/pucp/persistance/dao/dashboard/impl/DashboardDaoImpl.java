package pe.edu.pucp.persistance.dao.dashboard.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.dashboard.KpiAdminDto;
import pe.edu.pucp.model.dashboard.KpiTrabajadorDto;
import pe.edu.pucp.persistance.dao.dashboard.dao.DashboardDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DashboardDaoImpl implements DashboardDao {

    // SP: KPI_ADMIN_DASHBOARD()
    @Override
    public KpiAdminDto obtenerKpisAdmin() {
        KpiAdminDto kpi = new KpiAdminDto();
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("KPI_ADMIN_DASHBOARD", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    kpi.setVentasHoy(rs.getDouble("VENTAS_HOY"));
                    kpi.setPedidosPendientes(rs.getInt("PEDIDOS_PENDIENTES"));
                    kpi.setProductosBajoStock(rs.getInt("PRODUCTOS_BAJO_STOCK"));
                    kpi.setIngresosMes(rs.getDouble("INGRESOS_MES"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en obtenerKpisAdmin: " + ex.getMessage());
        }
        return kpi;
    }

    // SP: KPI_TRABAJADOR_DASHBOARD(IN _trabajador_id INT)
    @Override
    public KpiTrabajadorDto obtenerKpisTrabajador(int idTrabajador) {
        KpiTrabajadorDto kpi = new KpiTrabajadorDto();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idTrabajador);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("KPI_TRABAJADOR_DASHBOARD", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    kpi.setPedidosPendientesHoy(rs.getInt("PEDIDOS_PENDIENTES_HOY"));
                    kpi.setVentasHoy(rs.getInt("VENTAS_HOY"));
                    kpi.setMontoRecaudadoHoy(rs.getDouble("MONTO_RECAUDADO_HOY"));
                    kpi.setDevolucionesAtendidasHoy(rs.getInt("DEVOLUCIONES_ATENDIDAS_HOY"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en obtenerKpisTrabajador: " + ex.getMessage());
        }
        return kpi;
    }
}
