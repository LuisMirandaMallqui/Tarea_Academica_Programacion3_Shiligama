package pe.edu.pucp.model.dashboard;

/**
 * KPIs del panel principal del Administrador.
 * Se llena con una sola consulta al SP KPI_ADMIN_DASHBOARD.
 */
public class KpiAdminDto {
    private double ventasHoy;            // monto total facturado en el día actual
    private int pedidosPendientes;       // pedidos en estado RECIBIDO o EN_PROCESO
    private int productosBajoStock;      // STOCK <= STOCK_MINIMO
    private double ingresosMes;          // monto total facturado en el mes actual

    public KpiAdminDto() {
    }

    public KpiAdminDto(double ventasHoy, int pedidosPendientes,
                       int productosBajoStock, double ingresosMes) {
        this.ventasHoy = ventasHoy;
        this.pedidosPendientes = pedidosPendientes;
        this.productosBajoStock = productosBajoStock;
        this.ingresosMes = ingresosMes;
    }

    public double getVentasHoy() { return ventasHoy; }
    public void setVentasHoy(double ventasHoy) { this.ventasHoy = ventasHoy; }

    public int getPedidosPendientes() { return pedidosPendientes; }
    public void setPedidosPendientes(int pedidosPendientes) { this.pedidosPendientes = pedidosPendientes; }

    public int getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(int productosBajoStock) { this.productosBajoStock = productosBajoStock; }

    public double getIngresosMes() { return ingresosMes; }
    public void setIngresosMes(double ingresosMes) { this.ingresosMes = ingresosMes; }
}
