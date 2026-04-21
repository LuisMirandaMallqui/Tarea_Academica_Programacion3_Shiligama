package pe.edu.pucp.model.venta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.usuario.ClienteDto;

public class PedidoDto {
    private int idPedido;
    private LocalDateTime fechaHora;
    private double montoTotal;
    private double montoDescuento;
    private EstadoPedido estadoPedido;
    private String direccionEntrega; //En caso sea presencial queda vacío o como presencial
    private ModalidadVenta modalidadVenta;
    private String observaciones;
    //private String payloadJson;
    private ClienteDto cliente;
    // VentaDto para trazabilidad:
    // el admin/trabajador puede buscar desde el pedido la venta y asi la boleta asociada
    private VentaDto venta; //Este se crea después que se recibe el pedido, así que lo haremos con el setter
    private List<DetallePedidoDto> detalles;

    public PedidoDto() {
    }

    public PedidoDto(int idPedido, LocalDateTime fechaHora, double montoTotal,double montoDescuento,
                     EstadoPedido estadoPedido, String direccionEntrega,ModalidadVenta modalidadVenta,
                     String observaciones, ClienteDto cliente,
                      List<DetallePedidoDto> detalles) {
        this.idPedido = idPedido;
        this.fechaHora = fechaHora;
        this.montoTotal = montoTotal;
        this.montoDescuento = montoDescuento;
        this.estadoPedido = estadoPedido;
        this.direccionEntrega = direccionEntrega;
        this.modalidadVenta = modalidadVenta;
        this.observaciones = observaciones;
        //this.payloadJson = payloadJson;
        this.cliente = cliente;
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

    public double getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento(double montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public ModalidadVenta getModalidadVenta() {
        return modalidadVenta;
    }

    public void setModalidadVenta(ModalidadVenta modalidadVenta) {
        this.modalidadVenta = modalidadVenta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

//    public String getPayloadJson() {
//        return payloadJson;
//    }

//    public void setPayloadJson(String payloadJson) {
//        this.payloadJson = payloadJson;
//    }

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
