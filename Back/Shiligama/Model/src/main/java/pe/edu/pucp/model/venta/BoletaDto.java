package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;

import java.time.LocalDateTime;
import java.util.List;

// BoletaDto representa una venta que además emite comprobante de pago.
// Extiende VentaDto porque una boleta es una venta
// Los campos específicos de boleta son simplemente columnas adicionales en la tabla 'ventas'.

public class BoletaDto extends VentaDto {
    private int idBoleta;
    private String numeroBoleta;              // NUMERO_BOLETA
    private String ruc;                       // RUC_EMPRESA
    private String contactoCliente;           // CONTACTO_CLIENTE
    private String mensajeBoleta;             // MENSAJE_BOLETA
    private static final String DIRECCION_LOCAL = "Santa Eulalia 2023 Psje. 14";

    public BoletaDto() {
    }

    public BoletaDto(int idVenta, LocalDateTime fechaHora, double montoTotal,
                     double montoDescuento, CanalVenta canalVenta, EstadoVenta estadoVenta,
                     String observaciones, ClienteDto cliente, TrabajadorDto trabajador,
                     MetodoPagoDto metodoPago, List<DetalleVentaDto> detalles,
                     int idBoleta, String numeroBoleta, String ruc,
                     String contactoCliente, String mensajeBoleta) {
        super(idVenta, fechaHora, montoTotal, montoDescuento, canalVenta, estadoVenta,
                observaciones, cliente, trabajador, metodoPago, detalles);
        this.idBoleta = idBoleta;
        this.numeroBoleta = numeroBoleta;
        this.ruc = ruc;
        this.contactoCliente = contactoCliente;
        this.mensajeBoleta = mensajeBoleta;
    }

    public int getIdBoleta() { return idBoleta; }
    public void setIdBoleta(int idBoleta) { this.idBoleta = idBoleta; }

    public String getNumeroBoleta() { return numeroBoleta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getContactoCliente() { return contactoCliente; }
    public void setContactoCliente(String contactoCliente) { this.contactoCliente = contactoCliente; }

    public String getMensajeBoleta() { return mensajeBoleta; }
    public void setMensajeBoleta(String mensajeBoleta) { this.mensajeBoleta = mensajeBoleta; }

    public static String getDireccionLocal() { return DIRECCION_LOCAL; }
}