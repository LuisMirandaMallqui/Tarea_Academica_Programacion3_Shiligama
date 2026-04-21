package pe.edu.pucp.model.operaciones;

import java.time.LocalDateTime;

/**
 * Clase que representa una Devolución en el sistema.
 */
public class Devolucion {
    // ================= ATRIBUTOS =================
    private int idDevolucion;
    private int idVenta; // Referencia a la venta
    private String motivo;
    private LocalDateTime fechaSolicitud;
    private String estado; // EJ: "PENDIENTE", "APROBADO", "RECHAZADO"
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private int usuarioCreacion;
    private int usuarioModificacion;

    // ================= CONSTRUCTORES =================
    public Devolucion() {}

    public Devolucion(int idDevolucion, int idVenta, String motivo, 
                      LocalDateTime fechaSolicitud, String estado, boolean activo) {
        this.idDevolucion = idDevolucion;
        this.idVenta = idVenta;
        this.motivo = motivo;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.activo = activo;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdDevolucion() { return idDevolucion; }
    public void setIdDevolucion(int idDevolucion) { this.idDevolucion = idDevolucion; }
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }
    public int getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(int usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public int getUsuarioModificacion() { return usuarioModificacion; }
    public void setUsuarioModificacion(int usuarioModificacion) { this.usuarioModificacion = usuarioModificacion; }

    @Override
    public String toString() {
        return "Devolucion{" +
                "id=" + idDevolucion +
                ", idVenta=" + idVenta +
                ", estado='" + estado + '\'' +
                ", motivo='" + motivo + '\'' +
                ", activo=" + activo +
                '}';
    }
}
