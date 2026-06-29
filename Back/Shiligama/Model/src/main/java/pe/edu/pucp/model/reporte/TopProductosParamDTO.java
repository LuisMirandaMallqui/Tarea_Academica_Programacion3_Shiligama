package pe.edu.pucp.model.reporte;

import java.awt.Image;

public class TopProductosParamDTO {
    private String fechaInicio;   // "2026-04-19"
    private String fechaFin;      // "2026-07-19"
    private Integer limite;       // 20
    private Image logo;           // java.awt.Image

    // Resúmenes requeridos por el reporte
    private Double sumaPresencial;
    private Double sumaWeb;
    private Double ventaTotal;

    public TopProductosParamDTO() {}

    public TopProductosParamDTO(String fechaInicio, String fechaFin, Integer limite, Image logo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.limite      = limite;
        this.logo        = logo;
    }

    public String getFechaInicio()  { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin()     { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public Integer getLimite()      { return limite; }
    public void setLimite(Integer limite) { this.limite = limite; }

    public Image getLogo()          { return logo; }
    public void setLogo(Image logo) { this.logo = logo; }

    public Double getSumaPresencial()  { return sumaPresencial; }
    public void setSumaPresencial(Double sumaPresencial) { this.sumaPresencial = sumaPresencial; }

    public Double getSumaWeb()         { return sumaWeb; }
    public void setSumaWeb(Double sumaWeb) { this.sumaWeb = sumaWeb; }

    public Double getVentaTotal()      { return ventaTotal; }
    public void setVentaTotal(Double ventaTotal) { this.ventaTotal = ventaTotal; }
}
