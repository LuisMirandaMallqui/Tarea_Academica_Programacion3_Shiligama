package pe.edu.pucp.shiligama.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.usuario.Administrador;
import pe.edu.pucp.usuario.bo.AdministradorBo;
import pe.edu.pucp.usuario.impl.AdministradorBoImpl;

import java.util.List;

/**
 * Web Service REST para Administradores.
 * Sigue el mismo patrón que ClienteWS y TrabajadorWS.
 *
 * NOTA: el método /login específico por rol queda deprecado por LoginWS unificado.
 * Se mantiene aquí solo para retro-compatibilidad si algún cliente lo invoca.
 */
@Path("/administradores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdministradorWS {

    private final AdministradorBo administradorBo = new AdministradorBoImpl();

    // 1. LISTAR TODOS
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores
    @GET
    public Response listarTodos() {
        try {
            List<Administrador> lista = administradorBo.listarTodos();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar administradores: " + ex.getMessage()).build();
        }
    }

    // 2. BUSCAR POR ID
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores/5
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Administrador admin = administradorBo.buscarPorID(id);
            if (admin != null) {
                return Response.ok(admin).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Administrador con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar administrador: " + ex.getMessage()).build();
        }
    }

    // 3. INSERTAR
    // POST http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores
    @POST
    public Response insertar(Administrador administrador) {
        try {
            int idGenerado = administradorBo.insertar(administrador);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el administrador.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar administrador: " + ex.getMessage()).build();
        }
    }

    // 4. MODIFICAR
    // PUT http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores
    @PUT
    public Response modificar(Administrador administrador) {
        try {
            int filasAfectadas = administradorBo.modificar(administrador);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar administrador: " + ex.getMessage()).build();
        }
    }

    // 5. ELIMINAR (Soft Delete)
    // DELETE http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores/5
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = administradorBo.eliminar(id);
            return Response.ok("Administrador eliminado lógicamente. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar administrador: " + ex.getMessage()).build();
        }
    }

    // 6. OBTENER POR DNI
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores/dni/77654321
    @GET
    @Path("/dni/{dni}")
    public Response obtenerPorDni(@PathParam("dni") String dni) {
        try {
            Administrador admin = administradorBo.obtenerPorDNI(dni);
            if (admin != null) {
                return Response.ok(admin).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Administrador con DNI " + dni + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al buscar por DNI: " + ex.getMessage()).build();
        }
    }

    // 7. BUSCAR POR CORREO
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/administradores/buscarPorCorreo?correo=admin@shiligama.com
    @GET
    @Path("/buscarPorCorreo")
    public Response buscarPorCorreo(@QueryParam("correo") String correo) {
        try {
            Administrador admin = administradorBo.buscarPorCorreo(correo);
            if (admin != null) {
                return Response.ok(admin).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Administrador con correo " + correo + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar por correo: " + ex.getMessage()).build();
        }
    }
}
