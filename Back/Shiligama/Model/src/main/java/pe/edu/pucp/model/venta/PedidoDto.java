package pe.edu.pucp.model.venta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.usuario.ClienteDto;

public class PedidoDto {
    private int idPedido;
    private LocalDateTime fechaHora;
    private double montoTotal;
    private EstadoPedido estadoPedido;
    private int prioridad;
    private String direccionEntrega;
    private String observaciones;
    private String payloadJson;
    private ClienteDto cliente;
    // VentaDto para trazabilidad:
    // el admin/trabajador puede buscar desde el pedido la venta y asi la boleta asociada
    private VentaDto venta;
    private List<DetallePedidoDto> detalles;

    public PedidoDto() {
    }

    public PedidoDto(int idPedido, LocalDateTime fechaHora, double montoTotal,
                     EstadoPedido estadoPedido, int prioridad, String direccionEntrega,
                     String observaciones, String payloadJson, ClienteDto cliente,
                     VentaDto venta, List<DetallePedidoDto> detalles) {
        this.idPedido = idPedido;
        this.fechaHora = fechaHora;
        this.montoTotal = montoTotal;
        this.estadoPedido = estadoPedido;
        this.prioridad = prioridad;
        this.direccionEntrega = direccionEntrega;
        this.observaciones = observaciones;
        this.payloadJson = payloadJson;
        this.cliente = cliente;
        this.venta = venta;
        this.detalles = detalles;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public ClienteDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    public VentaDto getVenta() {
        return venta;
    }

    public void setVenta(VentaDto venta) {
        this.venta = venta;
    }

    public List<DetallePedidoDto> getDetalles() {
        return new ArrayList<>(this.detalles); // para evitar referenciar al mismo
    }

    public void setDetalles(List<DetallePedidoDto> detalles) {
        this.detalles = detalles;
    }

    // Métodos de negocio
    public void agregarDetalle(DetallePedidoDto detalle) { }
    public boolean verificarDisponibilidad() { return false; }
    //cuando se confirme un pedido
    public VentaDto convertirAVenta() { return null; }

}
