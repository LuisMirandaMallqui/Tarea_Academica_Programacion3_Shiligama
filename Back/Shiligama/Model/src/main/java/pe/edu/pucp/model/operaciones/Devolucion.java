package pe.edu.pucp.model.operaciones;

import java.time.LocalDateTime;

/**
 * Clase que representa una Devolución en el sistema.
 */
public class Devolucion {
    // ================= ATRIBUTOS =================
    private int idDevolucion;
    private int idProducto;        // Solo el ID
    private int idTrabajador;      // Solo el ID
    private String estadoDevolucion; // "PENDIENTE", "APROBADO", "RECHAZADO"
    private int cantidad;
    private String motivo;
    private LocalDateTime fechaHora;
    private boolean activo;

    // ================= CONSTRUCTORES =================
    public Devolucion() {}

    public Devolucion(int idDevolucion, int idProducto, int idTrabajador, 
                      String estadoDevolucion, int cantidad, String motivo, 
                      LocalDateTime fechaHora, boolean activo) {
        this.idDevolucion = idDevolucion;
        this.idProducto = idProducto;
        this.idTrabajador = idTrabajador;
        this.estadoDevolucion = estadoDevolucion;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.activo = activo;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdDevolucion() { return idDevolucion; }
    public void setIdDevolucion(int idDevolucion) { this.idDevolucion = idDevolucion; }
    
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }
    
    public String getEstadoDevolucion() { return estadoDevolucion; }
    public void setEstadoDevolucion(String estadoDevolucion) { this.estadoDevolucion = estadoDevolucion; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "Devolucion{" +
                "id=" + idDevolucion +
                ", idProducto=" + idProducto +
                ", estado='" + estadoDevolucion + '\'' +
                ", cantidad=" + cantidad +
                ", motivo='" + motivo + '\'' +
                ", activo=" + activo +
                '}';
    }
}
