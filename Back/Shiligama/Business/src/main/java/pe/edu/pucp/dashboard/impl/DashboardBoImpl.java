package pe.edu.pucp.dashboard.impl;

import pe.edu.pucp.dashboard.bo.DashboardBo;
import pe.edu.pucp.model.dashboard.KpiAdminDto;
import pe.edu.pucp.model.dashboard.KpiTrabajadorDto;
import pe.edu.pucp.persistance.dao.dashboard.dao.DashboardDao;
import pe.edu.pucp.persistance.dao.dashboard.impl.DashboardDaoImpl;

public class DashboardBoImpl implements DashboardBo {
    private final DashboardDao dashboardDao;

    public DashboardBoImpl() {
        this.dashboardDao = new DashboardDaoImpl();
    }

    @Override
    public KpiAdminDto obtenerKpisAdmin() throws Exception {
        return dashboardDao.obtenerKpisAdmin();
    }

    @Override
    public KpiTrabajadorDto obtenerKpisTrabajador(int idTrabajador) throws Exception {
        if (idTrabajador <= 0) {
            throw new Exception("El ID del trabajador debe ser mayor que cero.");
        }
        return dashboardDao.obtenerKpisTrabajador(idTrabajador);
    }
}
