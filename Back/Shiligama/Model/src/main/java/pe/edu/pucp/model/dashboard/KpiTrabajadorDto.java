package pe.edu.pucp.model.dashboard;

/**
 * KPIs del panel principal del Trabajador.
 * Se llena con una sola consulta al SP KPI_TRABAJADOR_DASHBOARD.
 */
public class KpiTrabajadorDto {
    private int pedidosPendientesHoy;    // pedidos del día en RECIBIDO o EN_PROCESO
    private int ventasHoy;               // cantidad de ventas registradas hoy por el trabajador
    private double montoRecaudadoHoy;    // suma de ventas hoy por el trabajador
    private int devolucionesAtendidasHoy;// devoluciones aprobadas hoy por el trabajador

    public KpiTrabajadorDto() {
    }

    public KpiTrabajadorDto(int pedidosPendientesHoy, int ventasHoy,
                            double montoRecaudadoHoy, int devolucionesAtendidasHoy) {
        this.pedidosPendientesHoy = pedidosPendientesHoy;
        this.ventasHoy = ventasHoy;
        this.montoRecaudadoHoy = montoRecaudadoHoy;
        this.devolucionesAtendidasHoy = devolucionesAtendidasHoy;
    }

    public int getPedidosPendientesHoy() { return pedidosPendientesHoy; }
    public void setPedidosPendientesHoy(int pedidosPendientesHoy) { this.pedidosPendientesHoy = pedidosPendientesHoy; }

    public int getVentasHoy() { return ventasHoy; }
    public void setVentasHoy(int ventasHoy) { this.ventasHoy = ventasHoy; }

    public double getMontoRecaudadoHoy() { return montoRecaudadoHoy; }
    public void setMontoRecaudadoHoy(double montoRecaudadoHoy) { this.montoRecaudadoHoy = montoRecaudadoHoy; }

    public int getDevolucionesAtendidasHoy() { return devolucionesAtendidasHoy; }
    public void setDevolucionesAtendidasHoy(int devolucionesAtendidasHoy) { this.devolucionesAtendidasHoy = devolucionesAtendidasHoy; }
}
