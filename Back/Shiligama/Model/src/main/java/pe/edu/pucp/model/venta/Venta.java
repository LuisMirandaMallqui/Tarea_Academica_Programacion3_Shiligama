package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int idVenta;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    private int idPedido;
    private LocalDateTime fechaHora;
    private double montoTotal;
    private double montoDescuento;
    private CanalVenta canalVenta;
    private EstadoVenta estadoVenta;
    private String observaciones;
    private Cliente cliente;
    private Trabajador trabajador;
    private MetodoPago metodoPago;
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
