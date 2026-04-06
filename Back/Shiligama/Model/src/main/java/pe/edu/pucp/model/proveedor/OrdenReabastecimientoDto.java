package pe.edu.pucp.model.proveedor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.model.enums.EstadoOrden;
import pe.edu.pucp.model.usuario.TrabajadorDto;

public class OrdenReabastecimientoDto {
    private int idOrden;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEntregaEstimada;
    private EstadoOrden estadoOrden;
    private String observaciones;
    private ProveedorDto proveedor;
    private TrabajadorDto trabajador;
    private List<DetalleOrdenReabastecimientoDto> detalles;

    // Constructor vacío
    public OrdenReabastecimientoDto() {
        this.detalles = new ArrayList<>();
    }

    // Constructor completo
    public OrdenReabastecimientoDto(int idOrden, LocalDateTime fechaCreacion,
                                 LocalDate fechaEntregaEstimada,
                                 EstadoOrden estadoOrden, String observaciones,
                                 ProveedorDto proveedor, TrabajadorDto trabajador) {
        this.idOrden = idOrden;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estadoOrden = estadoOrden;
        this.observaciones = observaciones;
        this.proveedor = proveedor;
        this.trabajador = trabajador;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

    public EstadoOrden getEstadoOrden() { return estadoOrden; }
    public void setEstadoOrden(EstadoOrden estadoOrden) { this.estadoOrden = estadoOrden; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public ProveedorDto getProveedor() { return proveedor; }
    public void setProveedor(ProveedorDto proveedor) { this.proveedor = proveedor; }

    public TrabajadorDto getTrabajador() { return trabajador; }
    public void setTrabajador(TrabajadorDto trabajador) { this.trabajador = trabajador; }

    public List<DetalleOrdenReabastecimientoDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenReabastecimientoDto> detalles) { this.detalles = detalles; }

    // Métodos de negocio
    public void agregarDetalle(DetalleOrdenReabastecimientoDto detalle) { }
    public void recibirOrden() { }
}