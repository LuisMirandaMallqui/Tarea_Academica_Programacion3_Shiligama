package pe.edu.pucp.shiligama.servicios.dashboard;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.dashboard.bo.DashboardBo;
import pe.edu.pucp.dashboard.impl.DashboardBoImpl;
import pe.edu.pucp.model.dashboard.KpiAdminDto;
import pe.edu.pucp.model.dashboard.KpiTrabajadorDto;

/**
 * Web Service de KPIs para los paneles principales (admin y trabajador).
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/dashboard
 */
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardWS {

    private final DashboardBo dashboardBo = new DashboardBoImpl();

    // GET /api/dashboard/admin
    @GET
    @Path("/admin")
    public Response kpisAdmin() {
        try {
            KpiAdminDto kpi = dashboardBo.obtenerKpisAdmin();
            return Response.ok(kpi).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener KPIs admin: " + ex.getMessage()).build();
        }
    }

    // GET /api/dashboard/trabajador/7
    @GET
    @Path("/trabajador/{idTrabajador}")
    public Response kpisTrabajador(@PathParam("idTrabajador") int idTrabajador) {
        try {
            KpiTrabajadorDto kpi = dashboardBo.obtenerKpisTrabajador(idTrabajador);
            return Response.ok(kpi).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al obtener KPIs trabajador: " + ex.getMessage()).build();
        }
    }
}
