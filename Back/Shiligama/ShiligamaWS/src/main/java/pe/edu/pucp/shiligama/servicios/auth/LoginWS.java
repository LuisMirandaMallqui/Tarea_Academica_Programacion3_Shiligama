package pe.edu.pucp.shiligama.servicios.auth;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.usuario.CredencialesDto;
import pe.edu.pucp.model.usuario.Usuario;
import pe.edu.pucp.usuario.bo.AuthBo;
import pe.edu.pucp.usuario.impl.AuthBoImpl;

/**
 * Login UNIFICADO para los 3 roles (cliente, trabajador, administrador).
 *
 * Reemplaza los métodos /clientes/login, /trabajadores/login y
 * /administradores/login. El front envía {correo, contrasena} y recibe
 * el subtipo concreto de Usuario en JSON (con su campo "rol" polimórfico)
 * para decidir a qué pantalla redirigir.
 *
 * URL: POST http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/auth/login
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginWS {

    private final AuthBo authBo = new AuthBoImpl();

    // POST /api/auth/login   body: { "correo": "...", "contrasena": "..." }
    @POST
    @Path("/login")
    public Response login(CredencialesDto credenciales) {
        try {
            if (credenciales == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Las credenciales son obligatorias.").build();
            }
            Usuario usuario = authBo.autenticar(
                    credenciales.getCorreo(),
                    credenciales.getContrasena());

            if (usuario != null) {
                return Response.ok(usuario).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Correo electrónico o contraseña incorrectos.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error en el proceso de login: " + ex.getMessage()).build();
        }
    }
}
