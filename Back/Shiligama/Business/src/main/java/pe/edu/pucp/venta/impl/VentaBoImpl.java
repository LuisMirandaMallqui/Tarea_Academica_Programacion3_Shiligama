package pe.edu.pucp.venta.impl;

import java.time.LocalDateTime;
import java.util.List;
import pe.edu.pucp.config.Config;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.model.venta.TopProductoDto;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.persistance.dao.usuario.impl.ClienteDaoImpl;
import pe.edu.pucp.persistance.dao.venta.Impl.VentaDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDao;
import pe.edu.pucp.venta.bo.VentaBo;

public class VentaBoImpl implements VentaBo {
    private final VentaDao daoVenta;
    private final ClienteDaoImpl clienteDao;

    public VentaBoImpl() {
        daoVenta = new VentaDaoImpl();
        clienteDao = new ClienteDaoImpl();
    }

    @Override
    public int insertar(Venta venta) throws Exception {
        asignarClientePresencial(venta);
        validar(venta, false);
        return daoVenta.insertar(venta);
    }

    @Override
    public int modificar(Venta venta) throws Exception {
        validar(venta, true);
        return daoVenta.modificar(venta);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return daoVenta.eliminar(id);
    }

    @Override
    public Venta buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return daoVenta.buscarPorId(id);
    }

    @Override
    public List<Venta> listarTodos() throws Exception {
        return daoVenta.listarTodos();
    }

    private void asignarClientePresencial(Venta venta) throws Exception {
        if (venta == null || venta.getCanalVenta() != CanalVenta.PRESENCIAL) {
            return;
        }
        if (venta.getCliente() != null && venta.getCliente().getIdUsuario() > 0) {
            return;
        }

        int idCliente = resolverClientePublicoGeneral();
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(idCliente);
        venta.setCliente(cliente);
    }

    private int resolverClientePublicoGeneral() throws Exception {
        String configured = Config.get("pos.cliente.default.id", "");
        if (configured != null && !configured.isBlank()) {
            return Integer.parseInt(configured.trim());
        }

        Cliente consumidor = clienteDao.obtenerPorDNI("00000000");
        if (consumidor != null && consumidor.getIdUsuario() > 0) {
            return consumidor.getIdUsuario();
        }

        throw new Exception(
                "No existe el cliente Público General (DNI 00000000). "
                        + "Ejecute el INSERT del usuario/cliente 9 en ShiligamaInserts.sql.");
    }

    private void validar(Venta venta, boolean esModificacion) throws Exception {
        if (venta == null) {
            throw new Exception("La venta no puede ser nula.");
        }
        if (esModificacion && venta.getIdVenta() <= 0) {
            throw new Exception("El ID de la venta es obligatorio para la modificacion.");
        }
        if (venta.getCliente() == null || venta.getCliente().getIdUsuario() <= 0) {
            throw new Exception("La venta debe tener un cliente asignado.");
        }
        if (venta.getTrabajador() == null || venta.getTrabajador().getIdUsuario() <= 0) {
            throw new Exception("La venta debe tener un trabajador asignado.");
        }
        if (venta.getMetodoPago() == null) {
            throw new Exception("La venta debe tener un metodo de pago asignado.");
        }
        if (venta.getCanalVenta() == null) {
            throw new Exception("El canal de venta es obligatorio.");
        }
        if (!esModificacion && (venta.getDetalles() == null || venta.getDetalles().isEmpty())) {
            throw new Exception("La venta debe tener al menos un producto.");
        }
        if (venta.getMontoTotal() < 0) {
            throw new Exception("El monto total no puede ser negativo.");
        }
        if (venta.getMontoDescuento() < 0) {
            throw new Exception("El monto de descuento no puede ser negativo.");
        }
    }

    @Override
    public List<VentaReporteDto> reporteVentasPorPeriodo(String fechaInicio, String fechaFin) throws Exception {
        if (fechaInicio == null || fechaInicio.isBlank()) {
            throw new Exception("La fecha de inicio es obligatoria.");
        }
        if (fechaFin == null || fechaFin.isBlank()) {
            throw new Exception("La fecha de fin es obligatoria.");
        }
        return daoVenta.reporteVentasPorPeriodo(fechaInicio, fechaFin);
    }

    @Override
    public List<Venta> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws Exception {
        if (fechaInicio == null || fechaFin == null) {
            throw new Exception("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new Exception("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return daoVenta.listarPorFechas(fechaInicio, fechaFin);
    }

    @Override
    public List<Venta> listarPorTrabajador(int idTrabajador) throws Exception {
        if (idTrabajador <= 0) {
            throw new Exception("El ID del trabajador debe ser mayor que cero.");
        }
        return daoVenta.listarPorTrabajador(idTrabajador);
    }

    @Override
    public List<TopProductoDto> topProductosVendidos() throws Exception {
        return daoVenta.topProductosVendidos();
    }

    @Override
    public int confirmarVenta(int idVenta) throws Exception {
        if (idVenta <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return daoVenta.confirmarVenta(idVenta);
    }
}
