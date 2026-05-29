package pe.edu.pucp.persistance.dao.dashboard.dao;

import pe.edu.pucp.model.dashboard.KpiAdminDto;
import pe.edu.pucp.model.dashboard.KpiTrabajadorDto;

public interface DashboardDao {
    KpiAdminDto obtenerKpisAdmin();
    KpiTrabajadorDto obtenerKpisTrabajador(int idTrabajador);
}
