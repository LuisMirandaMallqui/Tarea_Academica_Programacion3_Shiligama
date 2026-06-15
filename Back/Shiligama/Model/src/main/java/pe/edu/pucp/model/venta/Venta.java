package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Venta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Venta {
    @XmlElement(name = "idVenta")
    @JsonbProperty("idVenta")
    private int idVenta;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    @XmlElement(name = "idPedido")
    @JsonbProperty("idPedido")
    private int idPedido;
    @XmlElement(name = "fechaHora")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaHora")
    private LocalDateTime fechaHora;
    @XmlElement(name = "montoTotal")
    @JsonbProperty("montoTotal")
    private double montoTotal;
    @XmlElement(name = "montoDescuento")
    @JsonbProperty("montoDescuento")
    private double montoDescuento;
    @XmlElement(name = "canalVenta")
    @JsonbProperty("canalVenta")
    private CanalVenta canalVenta;
    @XmlElement(name = "estadoVenta")
    @JsonbProperty("estadoVenta")
    private EstadoVenta estadoVenta;
    @XmlElement(name = "observaciones")
    @JsonbProperty("observaciones")
    private String observaciones;
    @XmlElement(name = "cliente")
    @JsonbProperty("cliente")
    private Cliente cliente;
    @XmlElement(name = "trabajador")
    @JsonbProperty("trabajador")
    private Trabajador trabajador;
    @XmlElement(name = "metodoPago")
    @JsonbProperty("metodoPago")
    private MetodoPago metodoPago;
    @XmlElement(name = "detalles")
    @JsonbProperty("detalles")
    private List<DetalleVenta> detalles = new ArrayList<>(); // nunca null — Yasson/JSON-B requiere lista vacía, no null


    public Venta() {
    }

    public Venta(int idVenta, LocalDateTime fechaHora, double montoTotal,
                 double montoDescuento, CanalVenta canalVenta, EstadoVenta estadoVenta,
                 String observaciones, Cliente cliente, Trabajador trabajador,
                 MetodoPago metodoPago, List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.fechaHora = fechaHora;
        this.montoTotal = montoTotal;
        this.montoDescuento = montoDescuento;
        this.canalVenta = canalVenta;
        this.estadoVenta = estadoVenta;
        this.observaciones = observaciones;
        this.cliente = cliente;
        this.trabajador = trabajador;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento(double montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public CanalVenta getCanalVenta() {
        return canalVenta;
    }

    public void setCanalVenta(CanalVenta canalVenta) {
        this.canalVenta = canalVenta;
    }

    public EstadoVenta getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(EstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles; // falta implementar para no romper encapsulamiento
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles; // falta implementar para no romper encapsulamiento
    }

    // Métodos de negocio
    public void agregarDetalle(DetalleVenta detalle) { }
    public void eliminarDetalle(DetalleVenta detalle) { }
    public double calcularMontoTotal() { return 0; }
}
