package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;

import java.time.LocalDateTime;
import java.util.List;

public class VentaDto {
    private int idVenta;
    private LocalDateTime fechaHora;
    private double montoTotal;
    private double montoDescuento;
    private CanalVenta canalVenta;
    private EstadoVenta estadoVenta;
    private String observaciones;
    private ClienteDto cliente;
    private TrabajadorDto trabajador;
    private MetodoPagoDto metodoPago;
    private List<DetalleVentaDto> detalles;


    public VentaDto() {
    }

    public VentaDto(int idVenta, LocalDateTime fechaHora, double montoTotal,
                    double montoDescuento, CanalVenta canalVenta, EstadoVenta estadoVenta,
                    String observaciones, ClienteDto cliente, TrabajadorDto trabajador,
                    MetodoPagoDto metodoPago, List<DetalleVentaDto> detalles) {
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

    public ClienteDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    public TrabajadorDto getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(TrabajadorDto trabajador) {
        this.trabajador = trabajador;
    }

    public MetodoPagoDto getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoDto metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleVentaDto> getDetalles() {
        return detalles; // falta implementar para no romper encapsulamiento
    }

    public void setDetalles(List<DetalleVentaDto> detalles) {
        this.detalles = detalles; // falta implementar para no romper encapsulamiento
    }

    // Métodos de negocio
    public void agregarDetalle(DetalleVentaDto detalle) { }
    public void eliminarDetalle(DetalleVentaDto detalle) { }
    public double calcularMontoTotal() { return 0; }
}
