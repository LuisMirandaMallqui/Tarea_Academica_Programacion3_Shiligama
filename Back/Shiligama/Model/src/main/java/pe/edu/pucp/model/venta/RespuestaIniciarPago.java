package pe.edu.pucp.model.venta;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Respuesta del endpoint que inicia un pago. Contiene los datos que el
 * frontend necesita para renderizar el formulario embebido de Izipay
 * (SDK Krypton): el formToken, la clave pública y la base del SDK.
 */
@XmlType(name = "RespuestaIniciarPago")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespuestaIniciarPago {
    @XmlElement(name = "exito")
    @JsonbProperty("exito")
    private boolean exito;
    @XmlElement(name = "mensaje")
    @JsonbProperty("mensaje")
    private String mensaje;
    @XmlElement(name = "formToken")
    @JsonbProperty("formToken")
    private String formToken;
    @XmlElement(name = "publicKey")
    @JsonbProperty("publicKey")
    private String publicKey;
    @XmlElement(name = "jsBase")
    @JsonbProperty("jsBase")
    private String jsBase;
    @XmlElement(name = "orderId")
    @JsonbProperty("orderId")
    private String orderId;
    @XmlElement(name = "idPago")
    @JsonbProperty("idPago")
    private int idPago;

    public RespuestaIniciarPago() {
    }

    public RespuestaIniciarPago(boolean exito, String mensaje, String formToken,
                                String publicKey, String jsBase, String orderId, int idPago) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.formToken = formToken;
        this.publicKey = publicKey;
        this.jsBase = jsBase;
        this.orderId = orderId;
        this.idPago = idPago;
    }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getFormToken() { return formToken; }
    public void setFormToken(String formToken) { this.formToken = formToken; }

    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    public String getJsBase() { return jsBase; }
    public void setJsBase(String jsBase) { this.jsBase = jsBase; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }
}
