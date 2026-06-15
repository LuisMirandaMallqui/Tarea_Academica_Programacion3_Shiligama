package pe.edu.pucp.model.operacion;

import java.time.LocalDateTime;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Clase que representa un movimiento en el inventario como log inmutable.
 */
@XmlType(name = "MovimientoInventario")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovimientoInventario {
    // ================= ATRIBUTOS =================
    @XmlElement(name = "idMovimiento")
    @JsonbProperty("idMovimiento")
    private int idMovimiento;
    @XmlElement(name = "idProducto")
    @JsonbProperty("idProducto")
    private int idProducto;        // Solo el ID
    @XmlElement(name = "idTrabajador")
    @JsonbProperty("idTrabajador")
    private int idTrabajador;      // Solo el ID (puede ser 0 si es NULL)
    @XmlElement(name = "tipoMovimiento")
    @JsonbProperty("tipoMovimiento")
    private String tipoMovimiento; // "ENTRADA", "SALIDA", "AJUSTE", "DEVOLUCION"
    @XmlElement(name = "cantidad")
    @JsonbProperty("cantidad")
    private int cantidad;
    @XmlElement(name = "stockAnterior")
    @JsonbProperty("stockAnterior")
    private int stockAnterior;
    @XmlElement(name = "stockResultante")
    @JsonbProperty("stockResultante")
    private int stockResultante;
    @XmlElement(name = "motivo")
    @JsonbProperty("motivo")
    private String motivo;
    @XmlElement(name = "fechaHora")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaHora")
    private LocalDateTime fechaHora;

    // ================= CONSTRUCTORES =================
    public MovimientoInventario() {}

    public MovimientoInventario(int idMovimiento, int idProducto, int idTrabajador, String tipoMovimiento,
                                int cantidad, int stockAnterior, int stockResultante, String motivo, 
                                LocalDateTime fechaHora) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.idTrabajador = idTrabajador;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockResultante = stockResultante;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
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

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id=" + idMovimiento +
                ", idProducto=" + idProducto +
                ", tipo='" + tipoMovimiento + '\'' +
                ", cantidad=" + cantidad +
                ", stockAnterior=" + stockAnterior +
                ", stockResultante=" + stockResultante +
                ", motivo='" + motivo + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
