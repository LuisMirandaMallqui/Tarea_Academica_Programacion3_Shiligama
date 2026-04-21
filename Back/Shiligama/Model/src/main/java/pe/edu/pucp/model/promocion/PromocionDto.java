package pe.edu.pucp.model.promocion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.model.enums.TipoDescuento;
import pe.edu.pucp.model.producto.ProductoDto;

public class PromocionDto {
    private int idPromocion;
    private String nombre;
    private String descripcion;
    private TipoDescuento tipoDescuento;
    private double valorDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String condiciones;
    private boolean estado;
    private List<ProductoDto> productos; //CORREGIR ESTO, VER EL TEMA SOLO USAR LA ID

    // Constructor vacío
    public PromocionDto() {
        this.productos = new ArrayList<>();
    }

    // Constructor completo
    public PromocionDto(int idPromocion, String nombre, String descripcion,
                        TipoDescuento tipoDescuento, double valorDescuento,
                        LocalDate fechaInicio, LocalDate fechaFin,
                        String condiciones, boolean estado) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.condiciones = condiciones;
        this.estado = estado;
        this.productos = new ArrayList<>();
    }

    // Getters y Setters
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

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public List<ProductoDto> getProductos() { return productos; }
    public void setProductos(List<ProductoDto> productos) { this.productos = productos; }

    // Métodos de negocio
    public boolean estaVigente() { return false; }
    public double calcularDescuento(double montoBase) { return 0; }
}