package pe.edu.pucp.model.nubefact;

import java.util.ArrayList;
import java.util.List;
import jakarta.json.bind.annotation.JsonbProperty;

public class NubefactRequestDTO {
    @JsonbProperty("operacion")
    private String operacion = "generar_comprobante";

    @JsonbProperty("tipo_de_comprobante")
    private int tipoDeComprobante = 2; // 2 = Boleta de Venta

    @JsonbProperty("serie")
    private String serie;

    @JsonbProperty("numero")
    private int numero;

    @JsonbProperty("sunat_transaction")
    private int sunatTransaction = 1;

    @JsonbProperty("cliente_tipo_de_documento")
    private String clienteTipoDeDocumento;

    @JsonbProperty("cliente_numero_de_documento")
    private String clienteNumeroDeDocumento;

    @JsonbProperty("cliente_denominacion")
    private String clienteDenominacion;

    @JsonbProperty("cliente_direccion")
    private String clienteDireccion;

    @JsonbProperty("cliente_email")
    private String clienteEmail;

    @JsonbProperty("fecha_de_emision")
    private String fechaDeEmision; // Formato: DD-MM-YYYY

    @JsonbProperty("moneda")
    private int moneda = 1; // 1 = Soles

    @JsonbProperty("porcentaje_de_igv")
    private double porcentajeDeIgv = 18.00;

    @JsonbProperty("total_gravada")
    private double totalGravada;

    @JsonbProperty("total_igv")
    private double totalIgv;

    @JsonbProperty("total")
    private double total;

    @JsonbProperty("enviar_automaticamente_a_la_sunat")
    private boolean enviarAutomaticamenteALaSunat = true;

    @JsonbProperty("items")
    private List<NubefactItemDTO> items = new ArrayList<>();

    public NubefactRequestDTO() {}

    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }

    public int getTipoDeComprobante() { return tipoDeComprobante; }
    public void setTipoDeComprobante(int tipoDeComprobante) { this.tipoDeComprobante = tipoDeComprobante; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getSunatTransaction() { return sunatTransaction; }
    public void setSunatTransaction(int sunatTransaction) { this.sunatTransaction = sunatTransaction; }

    public String getClienteTipoDeDocumento() { return clienteTipoDeDocumento; }
    public void setClienteTipoDeDocumento(String clienteTipoDeDocumento) { this.clienteTipoDeDocumento = clienteTipoDeDocumento; }

    public String getClienteNumeroDeDocumento() { return clienteNumeroDeDocumento; }
    public void setClienteNumeroDeDocumento(String clienteNumeroDeDocumento) { this.clienteNumeroDeDocumento = clienteNumeroDeDocumento; }

    public String getClienteDenominacion() { return clienteDenominacion; }
    public void setClienteDenominacion(String clienteDenominacion) { this.clienteDenominacion = clienteDenominacion; }

    public String getClienteDireccion() { return clienteDireccion; }
    public void setClienteDireccion(String clienteDireccion) { this.clienteDireccion = clienteDireccion; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getFechaDeEmision() { return fechaDeEmision; }
    public void setFechaDeEmision(String fechaDeEmision) { this.fechaDeEmision = fechaDeEmision; }

    public int getMoneda() { return moneda; }
    public void setMoneda(int moneda) { this.moneda = moneda; }

    public double getPorcentajeDeIgv() { return porcentajeDeIgv; }
    public void setPorcentajeDeIgv(double porcentajeDeIgv) { this.porcentajeDeIgv = porcentajeDeIgv; }

    public double getTotalGravada() { return totalGravada; }
    public void setTotalGravada(double totalGravada) { this.totalGravada = totalGravada; }

    public double getTotalIgv() { return totalIgv; }
    public void setTotalIgv(double totalIgv) { this.totalIgv = totalIgv; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public boolean isEnviarAutomaticamenteALaSunat() { return enviarAutomaticamenteALaSunat; }
    public void setEnviarAutomaticamenteALaSunat(boolean enviarAutomaticamenteALaSunat) {
        this.enviarAutomaticamenteALaSunat = enviarAutomaticamenteALaSunat;
    }

    public List<NubefactItemDTO> getItems() { return items; }
    public void setItems(List<NubefactItemDTO> items) { this.items = items; }
}
