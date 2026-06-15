package pe.edu.pucp.model.venta;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "MetodoPago")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetodoPago {
    @XmlElement(name = "idMetodoPago")
    @JsonbProperty("idMetodoPago")
    private int idMetodoPago;
    @XmlElement(name = "nombre")
    @JsonbProperty("nombre")
    private String nombre;
    @XmlElement(name = "estado")
    @JsonbProperty("estado")
    private boolean estado;

    public MetodoPago() {
    }

    public MetodoPago(int idMetodoPago, String nombre, boolean estado) {
        this.idMetodoPago = idMetodoPago;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


}
