package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.venta.bo.BoletaBo;
import pe.edu.pucp.venta.impl.BoletaBoImpl;

@Path("/boleta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoletaRS {

    private final BoletaBo boletaBo = new BoletaBoImpl();

    @POST
    @Path("/generar/{ventaId}")
    public Response generarBoleta(@PathParam("ventaId") int ventaId) {
        try {
            Boleta boleta = boletaBo.generarBoleta(ventaId);
            return Response.status(Response.Status.CREATED).entity(boleta).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al generar boleta: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/venta/{ventaId}")
    public Response buscarPorVentaId(@PathParam("ventaId") int ventaId) {
        try {
            Boleta boleta = boletaBo.buscarPorVentaId(ventaId);
            if (boleta != null) {
                return Response.ok(boleta).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Boleta para venta con ID " + ventaId + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar boleta por venta ID: " + ex.getMessage()).build();
        }
    }
}
