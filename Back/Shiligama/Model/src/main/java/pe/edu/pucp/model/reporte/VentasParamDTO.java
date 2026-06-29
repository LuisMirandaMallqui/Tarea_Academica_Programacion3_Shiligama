package pe.edu.pucp.model.reporte;

import java.awt.Image;

public class VentasParamDTO {
    private String fechaInicio;   // "2026-04-19"
    private String fechaFin;      // "2026-07-19"
    private String agrupacion;    // "DIA", "SEMANA", "MES"
    private Image logo;           // java.awt.Image

    // Resúmenes requeridos por el reporte
    private Double sumaPresencial;
    private Double sumaWeb;
    private Double ventaTotal;

    public VentasParamDTO() {}

    public VentasParamDTO(String fechaInicio, String fechaFin, String agrupacion, Image logo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.agrupacion  = agrupacion;
        this.logo        = logo;
    }

    public String getFechaInicio()  { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin()     { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getAgrupacion()   { return agrupacion; }
    public void setAgrupacion(String agrupacion) { this.agrupacion = agrupacion; }

    public Image getLogo()          { return logo; }
    public void setLogo(Image logo) { this.logo = logo; }

    public Double getSumaPresencial()  { return sumaPresencial; }
    public void setSumaPresencial(Double sumaPresencial) { this.sumaPresencial = sumaPresencial; }

    public Double getSumaWeb()         { return sumaWeb; }
    public void setSumaWeb(Double sumaWeb) { this.sumaWeb = sumaWeb; }

    public Double getVentaTotal()      { return ventaTotal; }
    public void setVentaTotal(Double ventaTotal) { this.ventaTotal = ventaTotal; }
}
