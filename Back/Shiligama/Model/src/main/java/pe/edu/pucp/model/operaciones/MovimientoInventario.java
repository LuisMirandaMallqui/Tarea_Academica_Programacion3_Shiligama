package pe.edu.pucp.model.operaciones;

import java.time.LocalDateTime;

/**
 * Clase que representa un movimiento en el inventario como log inmutable.
 */
public class MovimientoInventario {
    // ================= ATRIBUTOS =================
    private int idMovimiento;
    private int idProducto;
    private int idTrabajador;
    private String tipoMovimiento; // Entrada, Salida, etc.
    private int cantidad;
    private int stockAnterior;
    private int stockResultante;
    private String motivo;
    private LocalDateTime fechaHora;
    private int usuarioCreacion;

    // ================= CONSTRUCTORES =================
    public MovimientoInventario() {}

    public MovimientoInventario(int idMovimiento, int idProducto, int idTrabajador, String tipoMovimiento,
                                int cantidad, int stockAnterior, int stockResultante, String motivo, 
                                LocalDateTime fechaHora, int usuarioCreacion) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.idTrabajador = idTrabajador;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockResultante = stockResultante;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.usuarioCreacion = usuarioCreacion;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }
    
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public int getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(int stockAnterior) { this.stockAnterior = stockAnterior; }
    
    public int getStockResultante() { return stockResultante; }
    public void setStockResultante(int stockResultante) { this.stockResultante = stockResultante; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    
    public int getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(int usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id=" + idMovimiento +
                ", idProducto=" + idProducto +
                ", tipo='" + tipoMovimiento + '\'' +
                ", cantidad=" + cantidad +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
