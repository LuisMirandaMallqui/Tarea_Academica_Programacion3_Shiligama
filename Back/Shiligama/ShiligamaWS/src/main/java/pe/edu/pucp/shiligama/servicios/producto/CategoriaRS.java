package pe.edu.pucp.shiligama.servicios.producto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.producto.Categoria;
import pe.edu.pucp.producto.bo.CategoriaBo;
import pe.edu.pucp.producto.impl.CategoriaBoImpl;

import java.util.List;

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaRS {

    private final CategoriaBo categoriaBo = new CategoriaBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Categoria> categorias = categoriaBo.listarTodos();
            return Response.ok(categorias).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar categorías: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Categoria categoria = categoriaBo.buscarPorId(id);
            if (categoria != null) {
                return Response.ok(categoria).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Categoría con ID " + id + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar categoría: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Categoria categoria) {
        try {
            int idGenerado = categoriaBo.insertar(categoria);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar la categoría.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar categoría: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Categoria categoria) {
        try {
            int filasAfectadas = categoriaBo.modificar(categoria);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar categoría: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = categoriaBo.eliminar(id);
            return Response.ok("Categoría eliminada. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar categoría: " + ex.getMessage()).build();
        }
    }
}
