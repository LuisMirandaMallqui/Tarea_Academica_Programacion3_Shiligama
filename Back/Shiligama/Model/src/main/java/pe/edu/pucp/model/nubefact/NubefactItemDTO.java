package pe.edu.pucp.model.nubefact;

import jakarta.json.bind.annotation.JsonbProperty;

public class NubefactItemDTO {
    @JsonbProperty("unidad_de_medida")
    private String unidadDeMedida = "NIU";

    @JsonbProperty("codigo")
    private String codigo;

    @JsonbProperty("descripcion")
    private String descripcion;

    @JsonbProperty("cantidad")
    private double cantidad;

    @JsonbProperty("valor_unitario")
    private double valorUnitario;

    @JsonbProperty("precio_unitario")
    private double precioUnitario;

    @JsonbProperty("subtotal")
    private double subtotal;

    @JsonbProperty("tipo_de_igv")
    private int tipoDeIgv = 1; // 1 = Gravado - Operación Onerosa

    @JsonbProperty("igv")
    private double igv;

    @JsonbProperty("total")
    private double total;

    @JsonbProperty("anticipo_regularizacion")
    private boolean anticipoRegularizacion = false;

    public NubefactItemDTO() {}

    public String getUnidadDeMedida() { return unidadDeMedida; }
    public void setUnidadDeMedida(String unidadDeMedida) { this.unidadDeMedida = unidadDeMedida; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public int getTipoDeIgv() { return tipoDeIgv; }
    public void setTipoDeIgv(int tipoDeIgv) { this.tipoDeIgv = tipoDeIgv; }

    public double getIgv() { return igv; }
    public void setIgv(double igv) { this.igv = igv; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public boolean isAnticipoRegularizacion() { return anticipoRegularizacion; }
    public void setAnticipoRegularizacion(boolean anticipoRegularizacion) { this.anticipoRegularizacion = anticipoRegularizacion; }
}
