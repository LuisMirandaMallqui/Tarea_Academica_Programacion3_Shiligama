package pe.edu.pucp.shiligama.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.usuario.bo.TrabajadorBo;
import pe.edu.pucp.usuario.impl.TrabajadorBoImpl;

import java.util.List;

@Path("/trabajadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrabajadorWS {
    // Instanciamos el BOImpl directamente, igual que en el Principal.java
    private final TrabajadorBo trabajadorBo = new TrabajadorBoImpl();
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
    public Response buscarPorID(@PathParam("id") int id) {
        try {
            Trabajador t = trabajadorBo.buscarPorID(id);
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

    // 6. BUSCAR POR DNI (Método de UsuarioBo)
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

    // 7. AUTENTICACIÓN / LOGIN ESPECÍFICO PARA PESTAÑA TRABAJADOR
    @POST
    @Path("/login")
    public Response login(Trabajador credenciales) {
        try {
            // Se utiliza el método buscarPorCorreo definido en el BO [cite: 862]
            Trabajador t = trabajadorBo.buscarPorCorreo(credenciales.getCorreo());

            if (t != null && t.getContrasena().equals(credenciales.getContrasena())) {
                return Response.ok(t).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Correo electrónico o contraseña incorrectos.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
