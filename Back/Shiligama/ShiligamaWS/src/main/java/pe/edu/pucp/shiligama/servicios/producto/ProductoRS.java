package pe.edu.pucp.shiligama.servicios.producto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.producto.bo.ProductoBo;
import pe.edu.pucp.producto.impl.ProductoBoImpl;

import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoRS {

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
            Producto producto = productoBo.buscarPorId(id);
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

    // Búsqueda paginada con filtros opcionales.
    // GET /api/productos/buscar?categoria=2&q=arroz&precioMin=10&precioMax=50&soloPromo=true&pagina=1&tamano=20
    // Respuesta: body con List<Producto> de la página actual + headers
    //   X-Total-Count, X-Page, X-Page-Size, X-Total-Pages.
    @GET
    @Path("/buscar")
    public Response buscarPaginado(@QueryParam("categoria") Integer categoriaId,
                                   @QueryParam("q") String q,
                                   @QueryParam("precioMin") Double precioMin,
                                   @QueryParam("precioMax") Double precioMax,
                                   @QueryParam("soloPromo") Boolean soloPromo,
                                   @QueryParam("pagina") @DefaultValue("1") int pagina,
                                   @QueryParam("tamano") @DefaultValue("20") int tamano) {
        try {
            List<Producto> datos = productoBo.buscarPaginado(
                    categoriaId, q, precioMin, precioMax, soloPromo, pagina, tamano);
            int total = productoBo.contarFiltrados(
                    categoriaId, q, precioMin, precioMax, soloPromo);
            int totalPaginas = (int) Math.ceil((double) total / tamano);

            return Response.ok(datos)
                    .header("X-Total-Count", total)
                    .header("X-Page", pagina)
                    .header("X-Page-Size", tamano)
                    .header("X-Total-Pages", totalPaginas)
                    // CORS expose para que el front pueda leer estos headers
                    .header("Access-Control-Expose-Headers",
                            "X-Total-Count, X-Page, X-Page-Size, X-Total-Pages")
                    .build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error en búsqueda paginada: " + ex.getMessage()).build();
        }
    }

    // GET /api/productos/codigo-barras/7750885000123  — uso clave para POS
    @GET
    @Path("/codigo-barras/{codigo}")
    public Response buscarPorCodigoBarras(@PathParam("codigo") String codigo) {
        try {
            Producto producto = productoBo.buscarPorCodigoBarras(codigo);
            if (producto != null) {
                return Response.ok(producto).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Producto con código " + codigo + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al buscar por código de barras: " + ex.getMessage()).build();
        }
    }

    // GET /api/productos/bajo-stock  — panel inventario admin
    @GET
    @Path("/bajo-stock")
    public Response listarBajoStock() {
        try {
            List<Producto> lista = productoBo.listarBajoStock();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar productos con bajo stock: " + ex.getMessage()).build();
        }
    }
}
