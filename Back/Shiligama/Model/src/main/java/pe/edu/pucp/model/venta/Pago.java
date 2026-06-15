package pe.edu.pucp.model.venta;

import java.time.LocalDateTime;
import pe.edu.pucp.model.enums.EstadoPago;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Transacción de pago asociada a un Pedido del portal web.
 *
 * Por cumplimiento PCI-DSS este DTO (y la tabla) NO contienen datos
 * sensibles de tarjeta (número, CVV). Solo se guarda el estado, monto,
 * método y la referencia/transacción devuelta por la pasarela (Izipay).
 */
@XmlType(name = "Pago")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pago {
    @XmlElement(name = "idPago")
    @JsonbProperty("idPago")
    private int idPago;
    @XmlElement(name = "idPedido")
    @JsonbProperty("idPedido")
    private int idPedido;
    @XmlElement(name = "idMetodoPago")
    @JsonbProperty("idMetodoPago")
    private int idMetodoPago;
    @XmlElement(name = "monto")
    @JsonbProperty("monto")
    private double monto;
    @XmlElement(name = "moneda")
    @JsonbProperty("moneda")
    private String moneda;          // ISO-4217, p.ej. "PEN"
    @XmlElement(name = "estado")
    @JsonbProperty("estado")
    private EstadoPago estado;
    @XmlElement(name = "referencia")
    @JsonbProperty("referencia")
    private String referencia;      // vads_trans_id de Izipay
    @XmlElement(name = "orderId")
    @JsonbProperty("orderId")
    private String orderId;         // vads_order_id enviado a la pasarela
    @XmlElement(name = "fechaPago")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaPago")
    private LocalDateTime fechaPago;

    public Pago() {
    }

    public Pago(int idPago, int idPedido, int idMetodoPago, double monto,
                String moneda, EstadoPago estado, String referencia,
                String orderId, LocalDateTime fechaPago) {
        this.idPago = idPago;
        this.idPedido = idPedido;
        this.idMetodoPago = idMetodoPago;
        this.monto = monto;
        this.moneda = moneda;
        this.estado = estado;
        this.referencia = referencia;
        this.orderId = orderId;
        this.fechaPago = fechaPago;
    }

    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
}
