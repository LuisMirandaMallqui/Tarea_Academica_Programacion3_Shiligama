package pe.edu.pucp.model.operaciones;

import java.time.LocalDate;

/**
 * Clase que representa una promoción en el sistema.
 * Una promoción puede ser de tipo porcentaje o monto fijo,
 * y tiene una fecha de inicio y fin de vigencia.
 */
public class Promocion {
    // ================= ATRIBUTOS =================
    private int idPromocion;
    private String nombre;
    private String descripcion;
    private String tipoDescuento;   // "PORCENTAJE" o "MONTO_FIJO"
    private double valorDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String condiciones;
    private boolean activo;

    // ================= CONSTRUCTORES =================
    public Promocion() {}

    public Promocion(int idPromocion, String nombre, String descripcion, 
                     String tipoDescuento, double valorDescuento, 
                     LocalDate fechaInicio, LocalDate fechaFin, 
                     String condiciones, boolean activo) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.condiciones = condiciones;
        this.activo = activo;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }
    public double getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(double valorDescuento) { this.valorDescuento = valorDescuento; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Promocion{" +
                "id=" + idPromocion +
                ", nombre='" + nombre + '\'' +
                ", descuento=" + valorDescuento +
                " (" + tipoDescuento + ')' +
                ", vigencia=" + fechaInicio + " -> " + fechaFin +
                ", activo=" + activo +
                '}';
    }
}
