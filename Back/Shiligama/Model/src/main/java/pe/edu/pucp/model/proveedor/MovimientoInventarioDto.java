package pe.edu.pucp.model.proveedor;

import java.time.LocalDateTime;
import pe.edu.pucp.model.enums.TipoMovimiento;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;

public class MovimientoInventarioDto {
    private int idMovimiento;
    private TipoMovimiento tipoMovimiento;
    private int cantidad;
    private int stockAnterior;
    private int stockResultante;
    private String motivo;
    private LocalDateTime fechaHora;
    private ProductoDto producto;
    private TrabajadorDto trabajador;

    // Constructor vacío
    public MovimientoInventarioDto() {
    }

    // Constructor completo
    public MovimientoInventarioDto(int idMovimiento, TipoMovimiento tipoMovimiento,
                                   int cantidad, int stockAnterior,
                                   int stockResultante, String motivo,
                                   LocalDateTime fechaHora,
                                   ProductoDto producto, TrabajadorDto trabajador) {
        this.idMovimiento = idMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockResultante = stockResultante;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.producto = producto;
        this.trabajador = trabajador;
    }

    // Getters y Setters
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

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

    public ProductoDto getProducto() { return producto; }
    public void setProducto(ProductoDto producto) { this.producto = producto; }

    public TrabajadorDto getTrabajador() { return trabajador; }
    public void setTrabajador(TrabajadorDto trabajador) { this.trabajador = trabajador; }
}