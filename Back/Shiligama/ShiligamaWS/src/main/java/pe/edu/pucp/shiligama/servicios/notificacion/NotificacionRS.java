package pe.edu.pucp.shiligama.servicios.notificacion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.concurrente.TareaPromocionesPorVencer;
import pe.edu.pucp.model.notificacion.Notificacion;
import pe.edu.pucp.notificacion.bo.NotificacionBo;
import pe.edu.pucp.notificacion.impl.NotificacionBoImpl;

import java.util.List;

@Path("/notificaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificacionRS {

    private final NotificacionBo notificacionBo = new NotificacionBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Notificacion> lista = notificacionBo.listarTodos();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar notificaciones: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Notificacion notif = notificacionBo.buscarPorId(id);
            if (notif != null) {
                return Response.ok(notif).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Notificación con ID " + id + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar notificación: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Notificacion notif) {
        try {
            int idGenerado = notificacionBo.insertar(notif);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar la notificación.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar notificación: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Notificacion notif) {
        try {
            int filasAfectadas = notificacionBo.modificar(notif);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar notificación: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = notificacionBo.eliminar(id);
            if (filasAfectadas > 0) {
                return Response.ok("Notificación eliminada.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Notificación con ID " + id + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar notificación: " + ex.getMessage()).build();
        }
    }

    // GET /api/notificaciones/por-usuario/7  — incluye también las broadcast (destinatario null)
    // Usado por Cliente y Trabajador.
    @GET
    @Path("/por-usuario/{idUsuario}")
    public Response listarPorUsuario(@PathParam("idUsuario") int idUsuario) {
        try {
            List<Notificacion> lista = notificacionBo.listarPorUsuario(idUsuario);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar notificaciones por usuario: " + ex.getMessage()).build();
        }
    }

    // GET /api/notificaciones/para-admin — todo menos lo dirigido a un CLIENTE.
    // Incluye lo de trabajadores, lo de otros administradores y los broadcasts.
    @GET
    @Path("/para-admin")
    public Response listarParaAdmin() {
        try {
            List<Notificacion> lista = notificacionBo.listarParaAdmin();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar notificaciones para admin: " + ex.getMessage()).build();
        }
    }

    // PUT /api/notificaciones/12/marcar-leida
    @PUT
    @Path("/{id}/marcar-leida")
    public Response marcarLeida(@PathParam("id") int id) {
        try {
            int filasAfectadas = notificacionBo.marcarLeida(id);
            return Response.ok("Notificación marcada como leída. Filas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al marcar como leída: " + ex.getMessage()).build();
        }
    }

    // GET /api/notificaciones/contar-no-leidas/7  — para el badge del bell icon
    @GET
    @Path("/contar-no-leidas/{idUsuario}")
    public Response contarNoLeidas(@PathParam("idUsuario") int idUsuario) {
        try {
            int total = notificacionBo.contarNoLeidas(idUsuario);
            return Response.ok(total).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al contar notificaciones no leídas: " + ex.getMessage()).build();
        }
    }

    // POST /api/notificaciones/revisar-promociones
    // Dispara AHORA MISMO la revision de promociones por vencer/vencidas,
    // sin esperar el ciclo de 24h del scheduler. Pensado para pruebas y
    // demos en clase: util cuando se acaba de cambiar una FECHA_FIN y se
    // quiere ver la notificacion al instante.
    @POST
    @Path("/revisar-promociones")
    public Response revisarPromociones() {
        try {
            int generadas = TareaPromocionesPorVencer.ejecutarAhora();
            return Response.ok("Revisión completada. Notificaciones generadas: " + generadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al revisar promociones: " + ex.getMessage()).build();
        }
    }
}
