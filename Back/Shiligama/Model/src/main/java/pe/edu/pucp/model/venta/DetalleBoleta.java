package pe.edu.pucp.model.venta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;

@XmlType(name = "DetalleBoleta")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleBoleta {
    @XmlElement(name = "id")
    @JsonbProperty("id")
    private long id;

    @XmlElement(name = "idBoleta")
    @JsonbProperty("idBoleta")
    private long idBoleta;

    @XmlElement(name = "idProducto")
    @JsonbProperty("idProducto")
    private Long idProducto;

    @XmlElement(name = "unidadMedida")
    @JsonbProperty("unidadMedida")
    private String unidadMedida = "NIU";

    @XmlElement(name = "descripcion")
    @JsonbProperty("descripcion")
    private String descripcion;

    @XmlElement(name = "cantidad")
    @JsonbProperty("cantidad")
    private double cantidad;

    @XmlElement(name = "valorUnitario")
    @JsonbProperty("valorUnitario")
    private double valorUnitario;

    @XmlElement(name = "precioUnitario")
    @JsonbProperty("precioUnitario")
    private double precioUnitario;

    @XmlElement(name = "subtotal")
    @JsonbProperty("subtotal")
    private double subtotal;

    @XmlElement(name = "igv")
    @JsonbProperty("igv")
    private double igv;

    @XmlElement(name = "total")
    @JsonbProperty("total")
    private double total;

    public DetalleBoleta() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdBoleta() { return idBoleta; }
    public void setIdBoleta(long idBoleta) { this.idBoleta = idBoleta; }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIgv() { return igv; }
    public void setIgv(double igv) { this.igv = igv; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
