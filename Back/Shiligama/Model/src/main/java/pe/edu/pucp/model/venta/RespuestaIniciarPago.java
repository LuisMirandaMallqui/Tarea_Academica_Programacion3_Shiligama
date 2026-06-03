package pe.edu.pucp.model.venta;

/**
 * Respuesta del endpoint que inicia un pago: contiene la URL a la que el
 * frontend debe redirigir al cliente y los identificadores generados.
 */
public class RespuestaIniciarPago {
    private boolean exito;
    private String mensaje;
    private String redirectionUrl;
    private String orderId;
    private int idPago;

    public RespuestaIniciarPago() {
    }

    public RespuestaIniciarPago(boolean exito, String mensaje, String redirectionUrl,
                                String orderId, int idPago) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.redirectionUrl = redirectionUrl;
        this.orderId = orderId;
        this.idPago = idPago;
    }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getRedirectionUrl() { return redirectionUrl; }
    public void setRedirectionUrl(String redirectionUrl) { this.redirectionUrl = redirectionUrl; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
}
