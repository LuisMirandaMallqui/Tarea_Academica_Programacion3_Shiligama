package pe.edu.pucp.shiligama.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.seguridad.CambiarContrasenaDto;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.model.usuario.Usuario;
import pe.edu.pucp.usuario.bo.AuthBo;
import pe.edu.pucp.usuario.bo.RecuperacionBo;
import pe.edu.pucp.usuario.bo.TrabajadorBo;
import pe.edu.pucp.usuario.impl.AuthBoImpl;
import pe.edu.pucp.usuario.impl.RecuperacionBoImpl;
import pe.edu.pucp.usuario.impl.TrabajadorBoImpl;

import java.util.List;

@Path("/trabajadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrabajadorRS {

    private final TrabajadorBo trabajadorBo   = new TrabajadorBoImpl();
    private final AuthBo       authBo         = new AuthBoImpl();
    private final RecuperacionBo recuperacionBo = new RecuperacionBoImpl();

    // 1. INSERTAR TRABAJADOR
    @POST
    public Response insertar(Trabajador trabajador) {
        try {
            int idGenerado = trabajadorBo.insertar(trabajador);
            return Response.status(Response.Status.CREATED).entity(idGenerado).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    // 2. MODIFICAR TRABAJADOR
    @PUT
    public Response modificar(Trabajador trabajador) {
        try {
            int resultado = trabajadorBo.modificar(trabajador);
            return Response.ok(resultado).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    // 3. ELIMINAR TRABAJADOR (Soft Delete)
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int resultado = trabajadorBo.eliminar(id);
            return Response.ok(resultado).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }
    }

    // 4. BUSCAR TRABAJADOR POR ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Trabajador t = trabajadorBo.buscarPorId(id);
            if (t != null) {
                return Response.ok(t).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Trabajador no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // 5. LISTAR TODOS LOS TRABAJADORES
    @GET
    public Response listarTodos() {
        try {
            List<Trabajador> lista = trabajadorBo.listarTodos();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // 6. BUSCAR POR DNI
    @GET
    @Path("/dni/{dni}")
    public Response obtenerPorDNI(@PathParam("dni") String dni) {
        try {
            Trabajador t = trabajadorBo.obtenerPorDNI(dni);
            if (t != null) {
                return Response.ok(t).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("DNI no registrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    // 7. LOGIN — delega a AuthBo (unified SP AUTENTICAR_USUARIO) y verifica que sea TRABAJADOR
    @POST
    @Path("/login")
    public Response login(Trabajador credenciales) {
        try {
            Usuario usuario = authBo.autenticar(credenciales.getCorreo(), credenciales.getContrasena());
            if (usuario instanceof Trabajador t) {
                return Response.ok(t).build();
            } else if (usuario != null) {
                // El correo existe pero pertenece a otro rol (cliente/admin)
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Esta cuenta no corresponde a un trabajador.").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Correo electrónico o contraseña incorrectos.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // 8. CAMBIAR CONTRASEÑA DESDE PERFIL
    // PUT /api/trabajadores/{id}/contrasena
    // Body: { "contrasenaActual": "...", "nuevaContrasena": "..." }
    @PUT
    @Path("/{id}/contrasena")
    public Response cambiarContrasena(@PathParam("id") int id, CambiarContrasenaDto dto) {
        try {
            if (dto == null
                    || dto.getContrasenaActual() == null || dto.getContrasenaActual().isBlank()
                    || dto.getNuevaContrasena()  == null || dto.getNuevaContrasena().isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Debes indicar la contraseña actual y la nueva.").build();
            }
            if (dto.getNuevaContrasena().length() < 6) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("La nueva contraseña debe tener al menos 6 caracteres.").build();
            }

            // Verificar contraseña actual: obtener el correo del trabajador y autenticar
            Trabajador trabajador = trabajadorBo.buscarPorId(id);
            if (trabajador == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Trabajador no encontrado.").build();
            }

            Usuario verificado = authBo.autenticar(trabajador.getCorreo(), dto.getContrasenaActual());
            if (verificado == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("La contraseña actual es incorrecta.").build();
            }

            // Actualizar con la nueva contraseña (ya la hashea RecuperacionBo)
            recuperacionBo.cambiarContrasena(id, dto.getNuevaContrasena());
            return Response.ok("Contraseña actualizada correctamente.").build();

        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
