package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.venta.IniciarPagoDto;
import pe.edu.pucp.model.venta.MetodoPago;
import pe.edu.pucp.model.venta.Pago;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.model.venta.RespuestaIniciarPago;
import pe.edu.pucp.venta.bo.MetodoPagoBo;
import pe.edu.pucp.venta.bo.PagoBo;
import pe.edu.pucp.venta.bo.PedidoBo;
import pe.edu.pucp.venta.impl.MetodoPagoBoImpl;
import pe.edu.pucp.venta.impl.PagoBoImpl;
import pe.edu.pucp.venta.impl.PedidoBoImpl;
import pe.edu.pucp.venta.pasarela.PasarelaPagoService;

import java.util.List;

/**
 * Web Service REST de Pagos (pasarela Izipay).
 *
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/pagos
 *
 * Flujo principal:
 *  - POST /api/pagos/iniciar   -> registra el pago PENDIENTE y devuelve la
 *                                 redirectionUrl de Izipay para redirigir al cliente.
 *  - GET  /api/pagos/pedido/{id} -> consulta el estado del pago de un pedido.
 *  - El callback (IPN) de Izipay lo atiende {@link IzipayCallbackWS}.
 */
@Path("/pagos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagoRS {

    private final PagoBo pagoBo = new PagoBoImpl();
    private final PedidoBo pedidoBo = new PedidoBoImpl();
    private final MetodoPagoBo metodoPagoBo = new MetodoPagoBoImpl();
    private final PasarelaPagoService pasarela = new PasarelaPagoService();

    @POST
    @Path("/iniciar")
    public Response iniciarPago(IniciarPagoDto dto) {
        try {
            if (dto == null || dto.getIdPedido() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new RespuestaIniciarPago(false,
                                "El id del pedido es obligatorio.", null, null, null, null, 0)).build();
            }

            Pedido pedido = pedidoBo.buscarPorId(dto.getIdPedido());
            if (pedido == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new RespuestaIniciarPago(false,
                                "El pedido no existe.", null, null, null, null, 0)).build();
            }

            double monto = dto.getMonto() > 0 ? dto.getMonto() : pedido.getMontoTotal();
            if (monto <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new RespuestaIniciarPago(false,
                                "El monto del pedido es inválido.", null, null, null, null, 0)).build();
            }
            String moneda = (dto.getMoneda() != null && !dto.getMoneda().isBlank())
                    ? dto.getMoneda() : "PEN";

            int idMetodoIzipay = obtenerIdMetodoIzipay();
            if (idMetodoIzipay <= 0) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new RespuestaIniciarPago(false,
                                "No está configurado el método de pago IZIPAY.", null, null, null, null, 0)).build();
            }

            // Order id único y trazable al pedido (el callback recupera el
            // pedido a partir del pago localizado por este order id).
            String orderId = "PED" + pedido.getIdPedido() + "-" + System.currentTimeMillis();

            Pago pago = pagoBo.registrarPagoPendiente(
                    pedido.getIdPedido(), idMetodoIzipay, monto, moneda, orderId);

            // Izipay maneja montos en céntimos (entero).
            long amountCents = Math.round(monto * 100);
            String email = (dto.getEmail() != null && !dto.getEmail().isBlank())
                    ? dto.getEmail()
                    : (pedido.getCliente() != null ? pedido.getCliente().getCorreo() : null);

            // CreatePayment → formToken para el formulario embebido.
            String formToken = pasarela.crearFormToken(amountCents, moneda, orderId, email);

            if (formToken == null) {
                // El pago queda PENDIENTE; el cliente puede reintentar.
                return Response.status(Response.Status.BAD_GATEWAY)
                        .entity(new RespuestaIniciarPago(false,
                                "No se pudo iniciar el pago con Izipay. Intente nuevamente.",
                                null, null, null, orderId, pago.getIdPago())).build();
            }

            return Response.ok(new RespuestaIniciarPago(true,
                    "Pago iniciado. Renderizar el formulario embebido.",
                    formToken, pasarela.getPublicKey(), pasarela.getJsBase(),
                    orderId, pago.getIdPago())).build();

        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new RespuestaIniciarPago(false,
                            "Error al iniciar el pago: " + ex.getMessage(), null, null, null, null, 0)).build();
        }
    }

    @GET
    @Path("/pedido/{id}")
    public Response consultarPorPedido(@PathParam("id") int idPedido) {
        try {
            Pago pago = pagoBo.buscarPorPedido(idPedido);
            if (pago != null) {
                return Response.ok(pago).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No hay pagos registrados para el pedido " + idPedido + ".").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al consultar el pago: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Pago pago = pagoBo.buscarPorId(id);
            if (pago != null) {
                return Response.ok(pago).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pago con ID " + id + " no encontrado.").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar el pago: " + ex.getMessage()).build();
        }
    }

    @GET
    public Response listarTodos() {
        try {
            List<Pago> pagos = pagoBo.listarTodos();
            return Response.ok(pagos).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar pagos: " + ex.getMessage()).build();
        }
    }

    private int obtenerIdMetodoIzipay() throws Exception {
        for (MetodoPago mp : metodoPagoBo.listarTodos()) {
            if (mp.getNombre() != null && mp.getNombre().equalsIgnoreCase("IZIPAY")) {
                return mp.getIdMetodoPago();
            }
        }
        return -1;
    }
}
