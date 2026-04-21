package pe.edu.pucp.model.operaciones;

import java.time.LocalDateTime;

/**
 * Clase que representa la vinculación entre una Promoción y un Producto.
 */
public class PromocionProducto {
    // ================= ATRIBUTOS =================
    private int idPromocion;
    private int idProducto;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private int usuarioCreacion;

    // ================= CONSTRUCTORES =================
    public PromocionProducto() {}

    public PromocionProducto(int idPromocion, int idProducto, boolean activo) {
        this.idPromocion = idPromocion;
        this.idProducto = idProducto;
        this.activo = activo;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public int getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(int usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    @Override
    public String toString() {
        return "PromocionProducto{" +
                "idPromocion=" + idPromocion +
                ", idProducto=" + idProducto +
                ", activo=" + activo +
                '}';
    }
}
