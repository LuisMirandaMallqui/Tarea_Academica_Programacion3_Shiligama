package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.venta.bo.DetalleVentaBo;
import pe.edu.pucp.venta.impl.DetalleVentaBoImpl;

import java.util.List;

@Path("/detalles-venta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DetalleVentaRS {

    private final DetalleVentaBo detalleVentaBo = new DetalleVentaBoImpl();

    // =========================
    // LISTAR TODOS
    // =========================
    @GET
    public Response listarTodos() {
        try {
            List<DetalleVenta> lista = detalleVentaBo.listarTodos();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar detalles de venta: " + ex.getMessage())
                    .build();
        }
    }

    // =========================
    // LISTAR POR VENTA
    // =========================
    @GET
    @Path("/por-venta/{idVenta}")
    public Response listarPorVenta(@PathParam("idVenta") int idVenta) {
        try {
            List<DetalleVenta> lista = detalleVentaBo.listarPorVenta(idVenta);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar detalles por venta: " + ex.getMessage())
                    .build();
        }
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            DetalleVenta obj = detalleVentaBo.buscarPorId(id);

            if (obj == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Detalle de venta no encontrado")
                        .build();
            }

            return Response.ok(obj).build();

        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar detalle de venta: " + ex.getMessage())
                    .build();
        }
    }

    // =========================
    // INSERTAR
    // =========================
    @POST
    public Response insertar(DetalleVenta detalle) {
        try {
            int id = detalleVentaBo.insertar(detalle);

            if (id > 0) {
                return Response.status(Response.Status.CREATED).entity(id).build();
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo insertar detalle de venta")
                    .build();

        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar: " + ex.getMessage())
                    .build();
        }
    }

    // =========================
    // MODIFICAR
    // =========================
    @PUT
    public Response modificar(DetalleVenta detalle) {
        try {
            int filas = detalleVentaBo.modificar(detalle);
            return Response.ok(filas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar: " + ex.getMessage())
                    .build();
        }
    }

    // =========================
    // ELIMINAR
    // =========================
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filas = detalleVentaBo.eliminar(id);
            return Response.ok(filas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al eliminar: " + ex.getMessage())
                    .build();
        }
    }
}