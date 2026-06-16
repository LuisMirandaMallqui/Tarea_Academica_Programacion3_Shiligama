package pe.edu.pucp.model.nubefact;

import jakarta.json.bind.annotation.JsonbProperty;

public class NubefactResponseDTO {
    @JsonbProperty("enlace")
    private String enlace;

    @JsonbProperty("enlace_del_pdf")
    private String enlaceDelPdf;

    @JsonbProperty("enlace_del_xml")
    private String enlaceDelXml;

    @JsonbProperty("enlace_del_cdr")
    private String enlaceDelCdr;

    @JsonbProperty("cadena_para_codigo_qr")
    private String cadenaParaCodigoQr;

    @JsonbProperty("codigo_hash")
    private String codigoHash;

    @JsonbProperty("aceptada_por_sunat")
    private boolean aceptadaPorSunat;

    @JsonbProperty("sunat_description")
    private String sunatDescription;

    @JsonbProperty("sunat_note")
    private String sunatNote;

    @JsonbProperty("sunat_responsecode")
    private String sunatResponsecode;

    @JsonbProperty("errors")
    private String errors;

    public NubefactResponseDTO() {}

    public String getEnlace() { return enlace; }
    public void setEnlace(String enlace) { this.enlace = enlace; }

    public String getEnlaceDelPdf() { return enlaceDelPdf; }
    public void setEnlaceDelPdf(String enlaceDelPdf) { this.enlaceDelPdf = enlaceDelPdf; }

    public String getEnlaceDelXml() { return enlaceDelXml; }
    public void setEnlaceDelXml(String enlaceDelXml) { this.enlaceDelXml = enlaceDelXml; }

    public String getEnlaceDelCdr() { return enlaceDelCdr; }
    public void setEnlaceDelCdr(String enlaceDelCdr) { this.enlaceDelCdr = enlaceDelCdr; }

    public String getCadenaParaCodigoQr() { return cadenaParaCodigoQr; }
    public void setCadenaParaCodigoQr(String cadenaParaCodigoQr) { this.cadenaParaCodigoQr = cadenaParaCodigoQr; }

    public String getCodigoHash() { return codigoHash; }
    public void setCodigoHash(String codigoHash) { this.codigoHash = codigoHash; }

    public boolean isAceptadaPorSunat() { return aceptadaPorSunat; }
    public void setAceptadaPorSunat(boolean aceptadaPorSunat) { this.aceptadaPorSunat = aceptadaPorSunat; }

    public String getSunatDescription() { return sunatDescription; }
    public void setSunatDescription(String sunatDescription) { this.sunatDescription = sunatDescription; }

    public String getSunatNote() { return sunatNote; }
    public void setSunatNote(String sunatNote) { this.sunatNote = sunatNote; }

    public String getSunatResponsecode() { return sunatResponsecode; }
    public void setSunatResponsecode(String sunatResponsecode) { this.sunatResponsecode = sunatResponsecode; }

    public String getErrors() { return errors; }
    public void setErrors(String errors) { this.errors = errors; }
}
