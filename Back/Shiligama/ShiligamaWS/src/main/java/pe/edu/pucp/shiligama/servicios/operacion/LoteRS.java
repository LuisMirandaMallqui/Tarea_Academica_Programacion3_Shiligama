package pe.edu.pucp.shiligama.servicios.operacion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.operacion.Lote;
import pe.edu.pucp.operacion.bo.LoteBo;
import pe.edu.pucp.operacion.impl.LoteBoImpl;

import java.util.List;

@Path("/lotes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoteRS {

    private final LoteBo loteBo = new LoteBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Lote> lotes = loteBo.listarTodos();
            return Response.ok(lotes).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar lotes: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Lote lote = loteBo.buscarPorId(id);
            if (lote != null) {
                return Response.ok(lote).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Lote con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar lote: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Lote lote) {
        try {
            int idGenerado = loteBo.insertar(lote);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el lote.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar lote: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Lote lote) {
        try {
            int filasAfectadas = loteBo.modificar(lote);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar lote: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = loteBo.eliminar(id);
            return Response.ok("Lote eliminado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar lote: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/por-producto/{idProducto}")
    public Response listarPorProducto(@PathParam("idProducto") int idProducto) {
        try {
            List<Lote> lista = loteBo.listarPorProducto(idProducto);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar lotes por producto: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/proximos-vencer")
    public Response listarProximosAVencer(@QueryParam("dias") @DefaultValue("30") int dias) {
        try {
            List<Lote> lista = loteBo.listarProximosAVencer(dias);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar lotes próximos a vencer: " + ex.getMessage()).build();
        }
    }
}
