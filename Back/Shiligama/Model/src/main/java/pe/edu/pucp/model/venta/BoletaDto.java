package pe.edu.pucp.model.venta;

import java.time.LocalDateTime;

public class BoletaDto {
    private int idBoleta;
    private String RUC;
    private String numContacto;
    private String numeroBoleta;
    private static final String direccionLocal = "Santa Eulalia 2023 Psje. 14";
    private LocalDateTime fechaEmision;
    private VentaDto venta;
    private String mensaje; //Mensaje personalizado para colocar en la boleta

    public BoletaDto() {
    }

    public BoletaDto(int idBoleta, String RUC, String numContacto, String numeroBoleta,
                     LocalDateTime fechaEmision,
                     VentaDto venta,String mensaje) {
        this.idBoleta = idBoleta;
        this.RUC = RUC;
        this.numContacto = numContacto;
        this.numeroBoleta = numeroBoleta;
        this.fechaEmision = fechaEmision;
        this.venta = venta;
        this.mensaje = mensaje;
    }

    public int getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(int idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getRUC(){
        return this.RUC;
    }

    public void setRUC(String RUC){
        this.RUC = RUC;
    }

    public String getNumContacto(){
        return this.numContacto;
    }

    public void setNumContacto(String numContacto){
        this.numContacto = numContacto;
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

    public VentaDto getVenta() {
        return venta;
    }

    public void setVenta(VentaDto venta) {
        this.venta = venta;
    }

    public String getMensaje(){
        return this.mensaje;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }
}

