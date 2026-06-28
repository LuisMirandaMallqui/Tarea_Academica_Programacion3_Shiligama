package pe.edu.pucp.model.operacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Clase que representa una Devolución en el sistema.
 */
@XmlType(name = "Devolucion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Devolucion {
    // ================= ATRIBUTOS =================
    @XmlElement(name = "idDevolucion")
    @JsonbProperty("idDevolucion")
    private int idDevolucion;
    @XmlElement(name = "idProducto")
    @JsonbProperty("idProducto")
    private int idProducto;        // Solo el ID
    @XmlElement(name = "idPedido")
    @JsonbProperty("idPedido")
    private int idPedido;
    @XmlElement(name = "idVenta")
    @JsonbProperty("idVenta")
    private int idVenta;
    @XmlElement(name = "idUsuarioRegistra")
    @JsonbProperty("idUsuarioRegistra")
    private int idUsuarioRegistra;      // ID del usuario que registra (trabajador o admin)
    @XmlElement(name = "estadoDevolucion")
    @JsonbProperty("estadoDevolucion")
    private String estadoDevolucion; // "PENDIENTE", "APROBADO", "RECHAZADO"
    @XmlElement(name = "cantidad")
    @JsonbProperty("cantidad")
    private int cantidad;
    @XmlElement(name = "motivo")
    @JsonbProperty("motivo")
    private String motivo;
    @XmlElement(name = "observaciones")
    @JsonbProperty("observaciones")
    private String observaciones;
    @XmlElement(name = "fechaHora")
    @JsonbProperty("fechaHora")
    private LocalDateTime fechaHora;
    @XmlElement(name = "activo")
    @JsonbProperty("activo")
    private boolean activo;
    @XmlElement(name = "detalles")
    @JsonbProperty("detalles")
    private List<DetalleDevolucion> detalles = new ArrayList<>();
    @XmlElement(name = "nombreTrabajador")
    @JsonbProperty("nombreTrabajador")
    private String nombreTrabajador;

    // ================= CONSTRUCTORES =================
    public Devolucion() {}

    public Devolucion(int idDevolucion, int idProducto, int idUsuarioRegistra,
                      String estadoDevolucion, int cantidad, String motivo,
                      LocalDateTime fechaHora, boolean activo) {
        this.idDevolucion = idDevolucion;
        this.idProducto = idProducto;
        this.idUsuarioRegistra = idUsuarioRegistra;
        this.estadoDevolucion = estadoDevolucion;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.activo = activo;
    }

    // ================= GETTERS Y SETTERS =================
    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdDevolucion() { return idDevolucion; }
    public void setIdDevolucion(int idDevolucion) { this.idDevolucion = idDevolucion; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdUsuarioRegistra() { return idUsuarioRegistra; }
    public void setIdUsuarioRegistra(int idUsuarioRegistra) { this.idUsuarioRegistra = idUsuarioRegistra; }

    public String getEstadoDevolucion() { return estadoDevolucion; }
    public void setEstadoDevolucion(String estadoDevolucion) { this.estadoDevolucion = estadoDevolucion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<DetalleDevolucion> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleDevolucion> detalles) { this.detalles = detalles; }

    public String getNombreTrabajador() { return nombreTrabajador; }
    public void setNombreTrabajador(String nombreTrabajador) { this.nombreTrabajador = nombreTrabajador; }

    @Override
    public String toString() {
        return "Devolucion{" +
                "id=" + idDevolucion +
                ", idProducto=" + idProducto +
                ", idPedido=" + idPedido +
                ", idVenta=" + idVenta +
                ", estado='" + estadoDevolucion + '\'' +
                ", cantidad=" + cantidad +
                ", motivo='" + motivo + '\'' +
                ", activo=" + activo +
                ", detallesCount=" + (detalles != null ? detalles.size() : 0) +
                '}';
    }
}
