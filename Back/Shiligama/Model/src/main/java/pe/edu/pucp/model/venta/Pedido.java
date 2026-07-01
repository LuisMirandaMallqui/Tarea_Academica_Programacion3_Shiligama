package pe.edu.pucp.model.venta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.usuario.Cliente;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Pedido")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pedido {
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
    //private double montoDescuento;
    @XmlElement(name = "estadoPedido")
    @JsonbProperty("estadoPedido")
    private EstadoPedido estadoPedido;
    @XmlElement(name = "direccionEntrega")
    @JsonbProperty("direccionEntrega")
    private String direccionEntrega; //En caso sea presencial queda vacío o como presencial
    @XmlElement(name = "modalidadVenta")
    @JsonbProperty("modalidadVenta")
    private ModalidadVenta modalidadVenta;
    @XmlElement(name = "observaciones")
    @JsonbProperty("observaciones")
    private String observaciones;
    //private String payloadJson;
    @XmlElement(name = "cliente")
    @JsonbProperty("cliente")
    private Cliente cliente;
    // VentaDto para trazabilidad:
    // el admin/trabajador puede buscar desde el pedido la venta y asi la boleta asociada
    @XmlElement(name = "venta")
    @JsonbProperty("venta")
    private Venta venta; //Este se crea después que se recibe el pedido, así que lo haremos con el setter
    @XmlElement(name = "detalles")
    @JsonbProperty("detalles")
    private List<DetallePedido> detalles = new ArrayList<>(); // nunca null — Yasson/JSON-B requiere lista vacía, no null
    @XmlElement(name = "totalItems")
    @JsonbProperty("totalItems")
    private int totalItems;

    @XmlElement(name = "fechaRecibido")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaRecibido")
    private LocalDateTime fechaRecibido;

    @XmlElement(name = "fechaEnPreparacion")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaEnPreparacion")
    private LocalDateTime fechaEnPreparacion;

    @XmlElement(name = "fechaListo")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaListo")
    private LocalDateTime fechaListo;

    @XmlElement(name = "fechaEnCamino")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaEnCamino")
    private LocalDateTime fechaEnCamino;

    @XmlElement(name = "fechaEntregado")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaEntregado")
    private LocalDateTime fechaEntregado;

    @XmlElement(name = "fechaRecogido")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaRecogido")
    private LocalDateTime fechaRecogido;

    @XmlElement(name = "fechaCancelado")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaCancelado")
    private LocalDateTime fechaCancelado;


    public LocalDateTime getFechaCancelado() {
        return fechaCancelado;
    }

    public void setFechaCancelado(LocalDateTime fechaCancelado) {
        this.fechaCancelado = fechaCancelado;
    }

    public LocalDateTime getFechaRecogido() {
        return fechaRecogido;
    }

    public void setFechaRecogido(LocalDateTime fechaRecogido) {
        this.fechaRecogido = fechaRecogido;
    }

    public LocalDateTime getFechaEntregado() {
        return fechaEntregado;
    }

    public void setFechaEntregado(LocalDateTime fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public LocalDateTime getFechaEnCamino() {
        return fechaEnCamino;
    }

    public void setFechaEnCamino(LocalDateTime fechaEnCamino) {
        this.fechaEnCamino = fechaEnCamino;
    }

    public LocalDateTime getFechaListo() {
        return fechaListo;
    }

    public void setFechaListo(LocalDateTime fechaListo) {
        this.fechaListo = fechaListo;
    }

    public LocalDateTime getFechaEnPreparacion() {
        return fechaEnPreparacion;
    }

    public void setFechaEnPreparacion(LocalDateTime fechaEnPreparacion) {
        this.fechaEnPreparacion = fechaEnPreparacion;
    }

    public LocalDateTime getFechaRecibido() {
        return fechaRecibido;
    }

    public void setFechaRecibido(LocalDateTime fechaRecibido) {
        this.fechaRecibido = fechaRecibido;
    }


    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public Pedido() {
    }

    public Pedido(int idPedido, LocalDateTime fechaHora, double montoTotal,//double montoDescuento,
                  EstadoPedido estadoPedido, String direccionEntrega, ModalidadVenta modalidadVenta,
                  String observaciones, Cliente cliente,
                  List<DetallePedido> detalles) {
        this.idPedido = idPedido;
        this.fechaHora = fechaHora;
        this.montoTotal = montoTotal;
        //this.montoDescuento = montoDescuento;
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

//    public double getMontoDescuento() {
//        return montoDescuento;
//    }
//
//    public void setMontoDescuento(double montoDescuento) {
//        this.montoDescuento = montoDescuento;
//    }

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(this.detalles); // para evitar referenciar al mismo
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    // Métodos de negocio
    public void agregarDetalle(DetallePedido detalle) { }
    public boolean verificarDisponibilidad() { return false; }
    //cuando se confirme un pedido
    public Venta convertirAVenta() { return null; }

}
