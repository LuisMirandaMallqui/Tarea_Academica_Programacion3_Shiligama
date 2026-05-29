package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.venta.bo.BoletaBo;
import pe.edu.pucp.venta.impl.BoletaBoImpl;

import java.util.List;

/**
 * Web Service REST para Boletas (comprobantes de venta).
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/boletas
 *
 * Operaciones específicas: emitir (sobre una venta existente), buscar, listar,
 * anular. NO hay CRUD genérico porque Boleta es una fase de Venta.
 */
@Path("/boletas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoletaWS {

    private final BoletaBo boletaBo = new BoletaBoImpl();

    // POST /api/boletas/emitir  body: Boleta (usa idVenta, ruc, contactoCliente, mensajeBoleta)
    @POST
    @Path("/emitir")
    public Response emitir(Boleta boleta) {
        try {
            if (boleta == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("La boleta es obligatoria.").build();
            }
            String numeroGenerado = boletaBo.emitir(
                    boleta.getIdVenta(),
                    boleta.getRuc(),
                    boleta.getContactoCliente(),
                    boleta.getMensajeBoleta());
            if (numeroGenerado != null) {
                return Response.status(Response.Status.CREATED).entity(numeroGenerado).build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo emitir la boleta.").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al emitir boleta: " + ex.getMessage()).build();
        }
    }

    // GET /api/boletas  — todas las boletas emitidas
    @GET
    public Response listarTodas() {
        try {
            List<Boleta> lista = boletaBo.listarTodas();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar boletas: " + ex.getMessage()).build();
        }
    }

    // GET /api/boletas/venta/15
    @GET
    @Path("/venta/{idVenta}")
    public Response buscarPorIdVenta(@PathParam("idVenta") int idVenta) {
        try {
            Boleta boleta = boletaBo.buscarPorIdVenta(idVenta);
            if (boleta != null) {
                return Response.ok(boleta).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Venta " + idVenta + " no tiene boleta emitida.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al buscar boleta por venta: " + ex.getMessage()).build();
        }
    }

    // GET /api/boletas/numero/B-0000123
    @GET
    @Path("/numero/{numero}")
    public Response buscarPorNumero(@PathParam("numero") String numero) {
        try {
            Boleta boleta = boletaBo.buscarPorNumero(numero);
            if (boleta != null) {
                return Response.ok(boleta).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Boleta " + numero + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al buscar boleta por número: " + ex.getMessage()).build();
        }
    }

    // DELETE /api/boletas/venta/15  — anula la boleta de esa venta
    @DELETE
    @Path("/venta/{idVenta}")
    public Response anular(@PathParam("idVenta") int idVenta) {
        try {
            int filasAfectadas = boletaBo.anular(idVenta);
            return Response.ok("Boleta anulada. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al anular boleta: " + ex.getMessage()).build();
        }
    }
}
