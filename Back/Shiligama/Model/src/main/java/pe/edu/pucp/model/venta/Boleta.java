package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

// BoletaDto representa una venta que además emite comprobante de pago.
// Extiende VentaDto porque una boleta es una venta
// Los campos específicos de boleta son simplemente columnas adicionales en la tabla 'ventas'.

@XmlType(name = "Boleta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Boleta extends Venta {
    @XmlElement(name = "idBoleta")
    @JsonbProperty("idBoleta")
    private int idBoleta;
    @XmlElement(name = "numeroBoleta")
    @JsonbProperty("numeroBoleta")
    private String numeroBoleta;              // NUMERO_BOLETA
    @XmlElement(name = "ruc")
    @JsonbProperty("ruc")
    private String ruc;                       // RUC_EMPRESA
    @XmlElement(name = "contactoCliente")
    @JsonbProperty("contactoCliente")
    private String contactoCliente;           // CONTACTO_CLIENTE
    @XmlElement(name = "mensajeBoleta")
    @JsonbProperty("mensajeBoleta")
    private String mensajeBoleta;             // MENSAJE_BOLETA
    private static final String DIRECCION_LOCAL = "Santa Eulalia 2023 Psje. 14";

    public Boleta() {
    }

    public Boleta(int idVenta, LocalDateTime fechaHora, double montoTotal,
                  double montoDescuento, CanalVenta canalVenta, EstadoVenta estadoVenta,
                  String observaciones, Cliente cliente, Trabajador trabajador,
                  MetodoPago metodoPago, List<DetalleVenta> detalles,
                  int idBoleta, String numeroBoleta, String ruc,
                  String contactoCliente, String mensajeBoleta) {
        super(idVenta, fechaHora, montoTotal, montoDescuento, canalVenta, estadoVenta,
                observaciones, cliente, trabajador, metodoPago, detalles);
        this.idBoleta = idBoleta;
        this.numeroBoleta = numeroBoleta;
        this.ruc = ruc;
        this.contactoCliente = contactoCliente;
        this.mensajeBoleta = mensajeBoleta;
    }

    public int getIdBoleta() { return idBoleta; }
    public void setIdBoleta(int idBoleta) { this.idBoleta = idBoleta; }

    public String getNumeroBoleta() { return numeroBoleta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getContactoCliente() { return contactoCliente; }
    public void setContactoCliente(String contactoCliente) { this.contactoCliente = contactoCliente; }

    public String getMensajeBoleta() { return mensajeBoleta; }
    public void setMensajeBoleta(String mensajeBoleta) { this.mensajeBoleta = mensajeBoleta; }

    public static String getDireccionLocal() { return DIRECCION_LOCAL; }
}