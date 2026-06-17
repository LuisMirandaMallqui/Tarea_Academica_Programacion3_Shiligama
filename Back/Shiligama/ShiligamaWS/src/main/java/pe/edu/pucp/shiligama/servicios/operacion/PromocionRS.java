package pe.edu.pucp.shiligama.servicios.operacion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.operacion.bo.PromocionBO;
import pe.edu.pucp.operacion.impl.PromocionBoImpl;

import java.util.List;

@Path("/promociones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PromocionRS {

    private final PromocionBO promocionBo = new PromocionBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Promocion> promociones = promocionBo.listarTodos();
            return Response.ok(promociones).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar promociones: " + ex.getMessage()).build();
        }
    }

    // GET /api/promociones/con-productos
    // Devuelve todas las promociones con sus IDs de productos en UNA sola query.
    // Elimina el loop N+1 del front (1 call en vez de 1 + N calls).
    @GET
    @Path("/con-productos")
    public Response listarConProductos() {
        try {
            List<Promocion> promociones = promocionBo.listarTodasConProductos();
            return Response.ok(promociones).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar promociones con productos: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Promocion promocion = promocionBo.buscarPorId(id);
            if (promocion != null) {
                return Response.ok(promocion).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Promoción con ID " + id + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar promoción: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Promocion promocion) {
        try {
            int idGenerado = promocionBo.insertar(promocion);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar la promoción.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar promoción: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Promocion promocion) {
        try {
            int filasAfectadas = promocionBo.modificar(promocion);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar promoción: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = promocionBo.eliminar(id);
            return Response.ok("Promoción eliminada. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar promoción: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/vigentes")
    public Response listarVigentes() {
        try {
            List<Promocion> vigentes = promocionBo.listarVigentes();
            return Response.ok(vigentes).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar promociones vigentes: " + ex.getMessage()).build();
        }
    }

    @POST
    @Path("/{idPromocion}/productos/{idProducto}")
    public Response asociarProducto(@PathParam("idPromocion") int idPromocion,
                                    @PathParam("idProducto") int idProducto) {
        try {
            int filasAfectadas = promocionBo.asociarProducto(idPromocion, idProducto);
            return Response.ok("Producto asociado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al asociar producto a promoción: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{idPromocion}/productos/{idProducto}")
    public Response desasociarProducto(@PathParam("idPromocion") int idPromocion,
                                       @PathParam("idProducto") int idProducto) {
        try {
            int filasAfectadas = promocionBo.desasociarProducto(idPromocion, idProducto);
            return Response.ok("Producto desasociado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al desasociar producto de promoción: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{idPromocion}/productos")
    public Response listarProductosPorPromocion(@PathParam("idPromocion") int idPromocion) {
        try {
            List<Integer> ids = promocionBo.listarProductosPorPromocion(idPromocion);
            return Response.ok(ids).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar productos de la promoción: " + ex.getMessage()).build();
        }
    }
}
