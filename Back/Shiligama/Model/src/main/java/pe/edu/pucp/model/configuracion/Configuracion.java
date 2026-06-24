package pe.edu.pucp.model.configuracion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * Parámetros globales del sistema (tabla configuracion, fila única CONFIG_ID=1).
 * Editables por el administrador desde la pantalla de Configuración.
 */
@XmlType(name = "Configuracion")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuracion {

    @XmlElement(name = "nombreTienda")
    @JsonbProperty("nombreTienda")
    private String nombreTienda = "Shiligama Minimarket";

    @XmlElement(name = "moneda")
    @JsonbProperty("moneda")
    private String moneda = "PEN (S/.)";

    @XmlElement(name = "igv")
    @JsonbProperty("igv")
    private double igv = 18.0;

    @XmlElement(name = "tarifaEnvio")
    @JsonbProperty("tarifaEnvio")
    private double tarifaEnvio = 5.0;

    @XmlElement(name = "minimoEnvioGratis")
    @JsonbProperty("minimoEnvioGratis")
    private double minimoEnvioGratis = 50.0;

    public Configuracion() {}

    public String getNombreTienda()              { return nombreTienda; }
    public void setNombreTienda(String v)        { this.nombreTienda = v; }

    public String getMoneda()                    { return moneda; }
    public void setMoneda(String v)              { this.moneda = v; }

    public double getIgv()                       { return igv; }
    public void setIgv(double v)                 { this.igv = v; }

    public double getTarifaEnvio()               { return tarifaEnvio; }
    public void setTarifaEnvio(double v)         { this.tarifaEnvio = v; }

    public double getMinimoEnvioGratis()         { return minimoEnvioGratis; }
    public void setMinimoEnvioGratis(double v)   { this.minimoEnvioGratis = v; }
}
