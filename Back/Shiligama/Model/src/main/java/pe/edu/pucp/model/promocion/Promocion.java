package pe.edu.pucp.model.promocion;

import pe.edu.pucp.model.enums.TipoDescuento;
import pe.edu.pucp.model.producto.Producto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Clase que representa una promoción en el sistema.
 * Una promoción puede ser de tipo porcentaje o monto fijo,
 * y tiene una fecha de inicio y fin de vigencia.
 */
@XmlType(name = "Promocion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Promocion {
    // ================= ATRIBUTOS =================
    @XmlElement(name = "idPromocion")
    @JsonbProperty("idPromocion")
    private int idPromocion;
    @XmlElement(name = "nombre")
    @JsonbProperty("nombre")
    private String nombre;
    @XmlElement(name = "descripcion")
    @JsonbProperty("descripcion")
    private String descripcion;
    @XmlElement(name = "tipoDescuento")
    @JsonbProperty("tipoDescuento")
    private TipoDescuento tipoDescuento; // "PORCENTAJE" o "MONTO_FIJO"
    @XmlElement(name = "valorDescuento")
    @JsonbProperty("valorDescuento")
    private double valorDescuento;
    @XmlElement(name = "fechaInicio")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaInicio")
    private LocalDateTime fechaInicio;
    @XmlElement(name = "fechaFin")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaFin")
    private LocalDateTime  fechaFin;
    @XmlElement(name = "condiciones")
    @JsonbProperty("condiciones")
    private String condiciones;
    @XmlElement(name = "activo")
    @JsonbProperty("activo")
    private boolean activo;

    @XmlElement(name = "productos")
    @JsonbProperty("productos")
    private List<Producto> productos; //CORREGIR ESTO, VER EL TEMA SOLO USAR LA ID

    // ================= CONSTRUCTORES =================
    public Promocion() {}

    public Promocion(int idPromocion, String nombre, String descripcion,
                     TipoDescuento tipoDescuento, double valorDescuento,
                     LocalDateTime  fechaInicio, LocalDateTime  fechaFin,
                     String condiciones, boolean activo) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.condiciones = condiciones;
        this.activo = activo;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public TipoDescuento getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(TipoDescuento tipoDescuento) { this.tipoDescuento = tipoDescuento; }
    public double getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(double valorDescuento) { this.valorDescuento = valorDescuento; }
    public LocalDateTime  getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime  fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime  getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime  fechaFin) { this.fechaFin = fechaFin; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Promocion{" +
                "id=" + idPromocion +
                ", nombre='" + nombre + '\'' +
                ", descuento=" + valorDescuento +
                " (" + tipoDescuento + ')' +
                ", vigencia=" + fechaInicio + " -> " + fechaFin +
                ", activo=" + activo +
                '}';
    }
}
