package pe.edu.pucp.dashboard.bo;

import pe.edu.pucp.model.dashboard.KpiAdminDto;
import pe.edu.pucp.model.dashboard.KpiTrabajadorDto;

public interface DashboardBo {
    KpiAdminDto obtenerKpisAdmin() throws Exception;
    KpiTrabajadorDto obtenerKpisTrabajador(int idTrabajador) throws Exception;
}
