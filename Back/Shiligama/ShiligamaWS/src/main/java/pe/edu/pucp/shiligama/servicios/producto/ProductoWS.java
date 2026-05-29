package pe.edu.pucp.shiligama.servicios.producto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.producto.bo.ProductoBo;
import pe.edu.pucp.producto.impl.ProductoBoImpl;

import java.util.List;

/**
 * Web Service REST para Productos.
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/productos
 */
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoWS {

    private final ProductoBo productoBo = new ProductoBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Producto> productos = productoBo.listarTodos();
            return Response.ok(productos).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar productos: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Producto producto = productoBo.buscarPorID(id);
            if (producto != null) {
                return Response.ok(producto).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar producto: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Producto producto) {
        try {
            int idGenerado = productoBo.insertar(producto);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el producto.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar producto: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Producto producto) {
        try {
            int filasAfectadas = productoBo.modificar(producto);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar producto: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = productoBo.eliminar(id);
            return Response.ok("Producto eliminado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar producto: " + ex.getMessage()).build();
        }
    }
}
