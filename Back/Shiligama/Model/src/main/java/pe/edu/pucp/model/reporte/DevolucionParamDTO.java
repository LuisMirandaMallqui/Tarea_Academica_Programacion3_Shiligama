package pe.edu.pucp.model.reporte;

import java.awt.Image;
import java.math.BigDecimal;

public class DevolucionParamDTO {
    private String fechaInicio;   // "2026-04-19"
    private String fechaFin;      // "2026-07-19"
    private String estado;        // "APROBADO", "RECHAZADO", "PENDIENTE", "TODOS"
    private Integer diasAlerta;   // 30
    private Image logo;           // java.awt.Image

    // Resúmenes requeridos por el reporte
    private BigDecimal totalPerdidaReal;
    private Integer totalDev;
    private BigDecimal totalRiesgo;
    private Integer aprobados;
    private Integer pendientes;
    private Integer totalDevSec1;

    public DevolucionParamDTO() {}

    public DevolucionParamDTO(String fechaInicio, String fechaFin, String estado, Integer diasAlerta, Image logo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.estado      = estado;
        this.diasAlerta  = diasAlerta;
        this.logo        = logo;
    }

    public String getFechaInicio()  { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin()     { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado()       { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getDiasAlerta()  { return diasAlerta; }
    public void setDiasAlerta(Integer diasAlerta) { this.diasAlerta = diasAlerta; }

    public Image getLogo()          { return logo; }
    public void setLogo(Image logo) { this.logo = logo; }

    public BigDecimal getTotalPerdidaReal()  { return totalPerdidaReal; }
    public void setTotalPerdidaReal(BigDecimal totalPerdidaReal) { this.totalPerdidaReal = totalPerdidaReal; }

    public Integer getTotalDev()    { return totalDev; }
    public void setTotalDev(Integer totalDev) { this.totalDev = totalDev; }

    public BigDecimal getTotalRiesgo()       { return totalRiesgo; }
    public void setTotalRiesgo(BigDecimal totalRiesgo) { this.totalRiesgo = totalRiesgo; }

    public Integer getAprobados()   { return aprobados; }
    public void setAprobados(Integer aprobados) { this.aprobados = aprobados; }

    public Integer getPendientes()  { return pendientes; }
    public void setPendientes(Integer pendientes) { this.pendientes = pendientes; }

    public Integer getTotalDevSec1()  { return totalDevSec1; }
    public void setTotalDevSec1(Integer totalDevSec1) { this.totalDevSec1 = totalDevSec1; }
}
