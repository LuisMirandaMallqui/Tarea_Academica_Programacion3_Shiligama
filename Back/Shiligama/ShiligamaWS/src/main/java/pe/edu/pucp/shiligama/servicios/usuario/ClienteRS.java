package pe.edu.pucp.shiligama.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.usuario.bo.ClienteBo;
import pe.edu.pucp.usuario.impl.ClienteBoImpl;

import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteRS {

    // Instanciamos el BOImpl directamente, igual que en el Principal.java
    private final ClienteBo clienteBo = new ClienteBoImpl();

    // 1. LISTAR TODOS
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes
    @GET
    public Response listarTodos() {
        try {
            List<Cliente> clientes = clienteBo.listarTodos();
            return Response.ok(clientes).build();
        } catch (Exception ex) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar clientes: " + ex.getMessage())
                    .build();
        }
    }

    // 2. BUSCAR POR ID
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes/5
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Cliente cliente = clienteBo.buscarPorId(id);
            if (cliente != null) {
                return Response.ok(cliente).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar cliente: " + ex.getMessage()).build();
        }
    }

    // 3. INSERTAR (CREAR)
    // POST http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes
    // El JSON del Cliente se envía en el Body del Request
    @POST
    public Response insertar(Cliente cliente) {
        try {
            int resultadoId = clienteBo.insertar(cliente);
            if (resultadoId > 0) {
                // Retornamos 201 Created indicando el ID asignado
                return Response.status(Response.Status.CREATED).entity(resultadoId).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el cliente.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al insertar cliente: " + ex.getMessage()).build();
        }
    }

    // 4. MODIFICAR (ACTUALIZAR)
    // PUT http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes
    // El JSON con los datos cambiados va en el Body del Request
    @PUT
    public Response modificar(Cliente cliente) {
        try {
            int filasAfectadas = clienteBo.modificar(cliente);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al modificar cliente: " + ex.getMessage()).build();
        }
    }

    // 5. ELIMINAR (Soft Delete)
    // DELETE http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes/5
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = clienteBo.eliminar(id);
            return Response.ok("Cliente eliminado lógicamente. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar cliente: " + ex.getMessage()).build();
        }
    }

    // 6. OBTENER POR DNI (Método de UsuarioBo)
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes/dni/77654321
    @GET
    @Path("/dni/{dni}")
    public Response obtenerPorDni(@PathParam("dni") String dni) {
        try {
            Cliente cliente = clienteBo.obtenerPorDNI(dni);
            if (cliente != null) {
                return Response.ok(cliente).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente con DNI " + dni + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar por DNI: " + ex.getMessage()).build();
        }
    }

    // 7. BUSCAR POR CORREO (Método de UsuarioBo)
    // GET http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes/buscarPorCorreo?correo=juan@gmail.com
    @GET
    @Path("/buscarPorCorreo")
    public Response buscarPorCorreo(@QueryParam("correo") String correo) {
        try {
            Cliente cliente = clienteBo.buscarPorCorreo(correo);
            if (cliente != null) {
                return Response.ok(cliente).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente con correo " + correo + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar por correo: " + ex.getMessage()).build();
        }
    }

    // 8. AUTENTICACIÓN / LOGIN
    // POST http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes/login
    @POST
    @Path("/login")
    public Response login(Cliente credenciales) {
        try {
            // Buscamos al cliente usando el correo que viene de la pantalla
            Cliente cliente = clienteBo.buscarPorCorreo(credenciales.getCorreo());

            // Validamos si el cliente existe y si la contraseña coincide
            if (cliente != null && cliente.getContrasena().equals(credenciales.getContrasena())) {
                // Si todo está bien, devolvemos un 200 OK con los datos del usuario logueado
                return Response.ok(cliente).build();
            } else {
                // Si el correo no existe o la clave está mal, devolvemos 401 Unauthorized
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Correo electrónico o contraseña incorrectos.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error en el proceso de login: " + ex.getMessage()).build();
        }
    }
}