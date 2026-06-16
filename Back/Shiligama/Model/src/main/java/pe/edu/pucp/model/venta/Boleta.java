package pe.edu.pucp.model.venta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Boleta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Boleta {
    @XmlElement(name = "id")
    @JsonbProperty("id")
    private long id;

    @XmlElement(name = "ventaId")
    @JsonbProperty("ventaId")
    private int ventaId;

    @XmlElement(name = "serie")
    @JsonbProperty("serie")
    private String serie;

    @XmlElement(name = "numero")
    @JsonbProperty("numero")
    private int numero;

    @XmlElement(name = "fechaEmision")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @JsonbProperty("fechaEmision")
    private LocalDate fechaEmision;

    @XmlElement(name = "clienteTipoDocumento")
    @JsonbProperty("clienteTipoDocumento")
    private String clienteTipoDocumento;

    @XmlElement(name = "clienteNumeroDocumento")
    @JsonbProperty("clienteNumeroDocumento")
    private String clienteNumeroDocumento;

    @XmlElement(name = "clienteDenominacion")
    @JsonbProperty("clienteDenominacion")
    private String clienteDenominacion;

    @XmlElement(name = "clienteDireccion")
    @JsonbProperty("clienteDireccion")
    private String clienteDireccion;

    @XmlElement(name = "clienteEmail")
    @JsonbProperty("clienteEmail")
    private String clienteEmail;

    @XmlElement(name = "moneda")
    @JsonbProperty("moneda")
    private int moneda = 1;

    @XmlElement(name = "porcentajeIgv")
    @JsonbProperty("porcentajeIgv")
    private double porcentajeIgv = 18.0;

    @XmlElement(name = "totalGravada")
    @JsonbProperty("totalGravada")
    private double totalGravada;

    @XmlElement(name = "totalIgv")
    @JsonbProperty("totalIgv")
    private double totalIgv;

    @XmlElement(name = "total")
    @JsonbProperty("total")
    private double total;

    @XmlElement(name = "nubefactEnlace")
    @JsonbProperty("nubefactEnlace")
    private String nubefactEnlace;

    @XmlElement(name = "nubefactEnlacePdf")
    @JsonbProperty("nubefactEnlacePdf")
    private String nubefactEnlacePdf;

    @XmlElement(name = "nubefactEnlaceXml")
    @JsonbProperty("nubefactEnlaceXml")
    private String nubefactEnlaceXml;

    @XmlElement(name = "nubefactEnlaceCdr")
    @JsonbProperty("nubefactEnlaceCdr")
    private String nubefactEnlaceCdr;

    @XmlElement(name = "nubefactCadenaQr")
    @JsonbProperty("nubefactCadenaQr")
    private String nubefactCadenaQr;

    @XmlElement(name = "nubefactCodigoHash")
    @JsonbProperty("nubefactCodigoHash")
    private String nubefactCodigoHash;

    @XmlElement(name = "aceptadaPorSunat")
    @JsonbProperty("aceptadaPorSunat")
    private Boolean aceptadaPorSunat = false;

    @XmlElement(name = "sunatResponseCode")
    @JsonbProperty("sunatResponseCode")
    private String sunatResponseCode;

    @XmlElement(name = "sunatDescription")
    @JsonbProperty("sunatDescription")
    private String sunatDescription;

    @XmlElement(name = "anulado")
    @JsonbProperty("anulado")
    private Boolean anulado = false;

    @XmlElement(name = "anulacionMotivo")
    @JsonbProperty("anulacionMotivo")
    private String anulacionMotivo;

    @XmlElement(name = "fechaRegistro")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaRegistro")
    private LocalDateTime fechaRegistro;

    @XmlElement(name = "usuarioRegistro")
    @JsonbProperty("usuarioRegistro")
    private String usuarioRegistro;

    @XmlElement(name = "venta")
    @JsonbProperty("venta")
    private Venta venta;

    @XmlElement(name = "detalles")
    @JsonbProperty("detalles")
    private List<DetalleBoleta> detalles = new ArrayList<>();

    public Boleta() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getVentaId() { return ventaId; }
    public void setVentaId(int ventaId) { this.ventaId = ventaId; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getClienteTipoDocumento() { return clienteTipoDocumento; }
    public void setClienteTipoDocumento(String clienteTipoDocumento) { this.clienteTipoDocumento = clienteTipoDocumento; }

    public String getClienteNumeroDocumento() { return clienteNumeroDocumento; }
    public void setClienteNumeroDocumento(String clienteNumeroDocumento) { this.clienteNumeroDocumento = clienteNumeroDocumento; }

    public String getClienteDenominacion() { return clienteDenominacion; }
    public void setClienteDenominacion(String clienteDenominacion) { this.clienteDenominacion = clienteDenominacion; }

    public String getClienteDireccion() { return clienteDireccion; }
    public void setClienteDireccion(String clienteDireccion) { this.clienteDireccion = clienteDireccion; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public int getMoneda() { return moneda; }
    public void setMoneda(int moneda) { this.moneda = moneda; }

    public double getPorcentajeIgv() { return porcentajeIgv; }
    public void setPorcentajeIgv(double porcentajeIgv) { this.porcentajeIgv = porcentajeIgv; }

    public double getTotalGravada() { return totalGravada; }
    public void setTotalGravada(double totalGravada) { this.totalGravada = totalGravada; }

    public double getTotalIgv() { return totalIgv; }
    public void setTotalIgv(double totalIgv) { this.totalIgv = totalIgv; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getNubefactEnlace() { return nubefactEnlace; }
    public void setNubefactEnlace(String nubefactEnlace) { this.nubefactEnlace = nubefactEnlace; }

    public String getNubefactEnlacePdf() { return nubefactEnlacePdf; }
    public void setNubefactEnlacePdf(String nubefactEnlacePdf) { this.nubefactEnlacePdf = nubefactEnlacePdf; }

    public String getNubefactEnlaceXml() { return nubefactEnlaceXml; }
    public void setNubefactEnlaceXml(String nubefactEnlaceXml) { this.nubefactEnlaceXml = nubefactEnlaceXml; }

    public String getNubefactEnlaceCdr() { return nubefactEnlaceCdr; }
    public void setNubefactEnlaceCdr(String nubefactEnlaceCdr) { this.nubefactEnlaceCdr = nubefactEnlaceCdr; }

    public String getNubefactCadenaQr() { return nubefactCadenaQr; }
    public void setNubefactCadenaQr(String nubefactCadenaQr) { this.nubefactCadenaQr = nubefactCadenaQr; }

    public String getNubefactCodigoHash() { return nubefactCodigoHash; }
    public void setNubefactCodigoHash(String nubefactCodigoHash) { this.nubefactCodigoHash = nubefactCodigoHash; }

    public Boolean getAceptadaPorSunat() { return aceptadaPorSunat; }
    public void setAceptadaPorSunat(Boolean aceptadaPorSunat) { this.aceptadaPorSunat = aceptadaPorSunat; }

    public String getSunatResponseCode() { return sunatResponseCode; }
    public void setSunatResponseCode(String sunatResponseCode) { this.sunatResponseCode = sunatResponseCode; }

    public String getSunatDescription() { return sunatDescription; }
    public void setSunatDescription(String sunatDescription) { this.sunatDescription = sunatDescription; }

    public Boolean getAnulado() { return anulado; }
    public void setAnulado(Boolean anulado) { this.anulado = anulado; }

    public String getAnulacionMotivo() { return anulacionMotivo; }
    public void setAnulacionMotivo(String anulacionMotivo) { this.anulacionMotivo = anulacionMotivo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getUsuarioRegistro() { return usuarioRegistro; }
    public void setUsuarioRegistro(String usuarioRegistro) { this.usuarioRegistro = usuarioRegistro; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public List<DetalleBoleta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleBoleta> detalles) { this.detalles = detalles; }
}