package pe.edu.pucp.model.operacion;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Lote")
@XmlAccessorType(XmlAccessType.FIELD)
public class Lote {
    @XmlElement(name = "idLote")
    @JsonbProperty("idLote")
    private int idLote;
    @XmlElement(name = "idProducto")
    @JsonbProperty("idProducto")
    private int idProducto;
    @XmlElement(name = "idTrabajador")
    @JsonbProperty("idTrabajador")
    private int idTrabajador;
    @XmlElement(name = "cantidadInicial")
    @JsonbProperty("cantidadInicial")
    private int cantidadInicial;
    @XmlElement(name = "cantidadActual")
    @JsonbProperty("cantidadActual")
    private int cantidadActual;
    @XmlElement(name = "fechaVencimiento")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @JsonbProperty("fechaVencimiento")
    private LocalDate fechaVencimiento;
    @XmlElement(name = "numeroLote")
    @JsonbProperty("numeroLote")
    private String numeroLote;
    @XmlElement(name = "activo")
    @JsonbProperty("activo")
    private boolean activo;
    @XmlElement(name = "nombreTrabajador")
    @JsonbProperty("nombreTrabajador")
    private String nombreTrabajador;
    @XmlElement(name = "nombreProducto")
    @JsonbProperty("nombreProducto")
    private String nombreProducto;

    public Lote() {}

    public Lote(int idLote, int idProducto, int idTrabajador, int cantidadInicial,
                int cantidadActual, LocalDate fechaVencimiento, String numeroLote, boolean activo) {
        this.idLote = idLote;
        this.idProducto = idProducto;
        this.idTrabajador = idTrabajador;
        this.cantidadInicial = cantidadInicial;
        this.cantidadActual = cantidadActual;
        this.fechaVencimiento = fechaVencimiento;
        this.numeroLote = numeroLote;
        this.activo = activo;
    }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }
    public int getCantidadInicial() { return cantidadInicial; }
    public void setCantidadInicial(int cantidadInicial) { this.cantidadInicial = cantidadInicial; }
    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getNombreTrabajador() { return nombreTrabajador; }
    public void setNombreTrabajador(String nombreTrabajador) { this.nombreTrabajador = nombreTrabajador; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    @Override
    public String toString() {
        return "Lote{" +
                "id=" + idLote +
                ", idProducto=" + idProducto +
                ", cantidadInicial=" + cantidadInicial +
                ", cantidadActual=" + cantidadActual +
                ", fechaVencimiento=" + fechaVencimiento +
                ", numeroLote='" + numeroLote + '\'' +
                '}';
    }
}
