package pe.edu.pucp.model.venta;

import java.time.LocalDateTime;

public class BoletaDto {
    private int idBoleta;
    private String numeroBoleta;
    private LocalDateTime fechaEmision;
    private double montoTotal;
    private boolean sincronizado;
    private VentaDto venta;

    public BoletaDto() {
    }

    public BoletaDto(int idBoleta, String numeroBoleta, LocalDateTime fechaEmision,
                     double montoTotal, boolean sincronizado, VentaDto venta) {
        this.idBoleta = idBoleta;
        this.numeroBoleta = numeroBoleta;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.sincronizado = sincronizado;
        this.venta = venta;
    }

    public int getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(int idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getNumeroBoleta() {
        return numeroBoleta;
    }

    public void setNumeroBoleta(String numeroBoleta) {
        this.numeroBoleta = numeroBoleta;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public VentaDto getVenta() {
        return venta;
    }

    public void setVenta(VentaDto venta) {
        this.venta = venta;
    }
}

