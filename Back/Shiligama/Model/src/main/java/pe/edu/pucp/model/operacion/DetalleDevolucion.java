package pe.edu.pucp.model.operacion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;

@XmlType(name = "DetalleDevolucion")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetalleDevolucion {
    @XmlElement(name = "idProducto")
    @JsonbProperty("idProducto")
    private int idProducto;

    @XmlElement(name = "nombreProducto")
    @JsonbProperty("nombreProducto")
    private String nombreProducto;

    @XmlElement(name = "precioUnitario")
    @JsonbProperty("precioUnitario")
    private double precioUnitario;

    @XmlElement(name = "cantidad")
    @JsonbProperty("cantidad")
    private int cantidad;

    public DetalleDevolucion() {}

    public DetalleDevolucion(int idProducto, int cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public DetalleDevolucion(int idProducto, String nombreProducto, double precioUnitario, int cantidad) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
