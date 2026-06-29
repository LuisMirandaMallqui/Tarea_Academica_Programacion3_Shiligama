package pe.edu.pucp.operacion.impl;

import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.model.enums.TipoDevolucion;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.notificacion.impl.NotificacionHelper;
import pe.edu.pucp.operacion.bo.DevolucionBO;
import pe.edu.pucp.persistance.dao.operacion.Impl.DevolucionDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.dao.DevolucionDao;

import java.time.LocalDate;
import java.util.List;

public class DevolucionBoImpl implements DevolucionBO {
    private final DevolucionDao daoDevolucion;

    public DevolucionBoImpl() {
        this.daoDevolucion = new DevolucionDaoImpl();
    }

    @Override
    public int insertar(Devolucion devolucion) throws Exception {
        validar(devolucion, false);
        int idDevolucion = daoDevolucion.insertar(devolucion);

        // Notificacion broadcast a trabajadores: nueva devolucion pendiente de revisar.
        // Solo se notifica si queda en estado PENDIENTE (evita ruido si ya nace
        // APROBADA/RECHAZADA, por ejemplo cuando la registra directamente un admin).
        try {
            if ("PENDIENTE".equalsIgnoreCase(devolucion.getEstadoDevolucion())) {
                String descripcionProducto = obtenerDescripcionProducto(devolucion);
                NotificacionHelper.notificarBroadcast(
                        "Devolución #" + idDevolucion + " pendiente",
                        "Se registró una devolución" + descripcionProducto + " que requiere revisión.",
                        TipoNotificacion.DEVOLUCION_PENDIENTE,
                        ReferenciaNotificacion.DEVOLUCION, idDevolucion);
            }
        } catch (Exception ex) {
            System.err.println("[DevolucionBoImpl] Error al notificar devolucion pendiente: " + ex.getMessage());
        }

        return idDevolucion;
    }

    @Override
    public int modificar(Devolucion devolucion) throws Exception {
        validar(devolucion, true);
        int filas = daoDevolucion.modificar(devolucion);

        // Si el estado cambia a APROBADO/RECHAZADO, se notifica al trabajador que
        // la registro (idUsuarioRegistra) que su devolucion fue resuelta.
        try {
            String estado = devolucion.getEstadoDevolucion();
            if ("APROBADO".equalsIgnoreCase(estado) || "RECHAZADO".equalsIgnoreCase(estado)) {
                Devolucion completa = daoDevolucion.buscarPorId(devolucion.getIdDevolucion());
                if (completa != null && completa.getIdUsuarioRegistra() > 0) {
                    NotificacionHelper.notificar(
                            "Devolución #" + devolucion.getIdDevolucion() + " " + estado.toLowerCase(),
                            "La devolución #" + devolucion.getIdDevolucion() + " fue " + estado.toLowerCase() + ".",
                            TipoNotificacion.DEVOLUCION_RESUELTA,
                            completa.getIdUsuarioRegistra(),
                            ReferenciaNotificacion.DEVOLUCION, devolucion.getIdDevolucion());
                }
            }
        } catch (Exception ex) {
            System.err.println("[DevolucionBoImpl] Error al notificar devolucion resuelta: " + ex.getMessage());
        }

        return filas;
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la devolucion debe ser mayor que cero.");
        }
        return daoDevolucion.eliminar(id);
    }

    @Override
    public Devolucion buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la devolucion debe ser mayor que cero.");
        }
        return daoDevolucion.buscarPorId(id);
    }

    @Override
    public List<Devolucion> listarTodos() throws Exception {
        return daoDevolucion.listarTodos();
    }

    @Override
    public List<Devolucion> listarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        if (fechaInicio == null || fechaFin == null) {
            throw new Exception("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new Exception("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return daoDevolucion.listarPorFechas(fechaInicio, fechaFin);
    }

    // Construye un fragmento de texto con el nombre del producto cuando esta
    // disponible (puede venir nulo si el front solo manda el detalle minimo).
    private String obtenerDescripcionProducto(Devolucion devolucion) {
        if (devolucion.getDetalles() != null && !devolucion.getDetalles().isEmpty()
                && devolucion.getDetalles().get(0).getNombreProducto() != null) {
            return " de \"" + devolucion.getDetalles().get(0).getNombreProducto() + "\"";
        }
        return "";
    }

    private void validar(Devolucion devolucion, boolean esModificacion) throws Exception {
        if (devolucion == null) {
            throw new Exception("La devolucion no puede ser nula.");
        }
        if (esModificacion && devolucion.getIdDevolucion() <= 0) {
            throw new Exception("El ID de la devolucion es obligatorio para la modificacion.");
        }
        // Si hay detalles, validar cada detalle en vez de la cabecera
        if (devolucion.getDetalles() != null && !devolucion.getDetalles().isEmpty()) {
            for (var det : devolucion.getDetalles()) {
                if (det.getIdProducto() <= 0) {
                    throw new Exception("Cada detalle debe tener un ID de producto válido.");
                }
                if (det.getCantidad() <= 0) {
                    throw new Exception("Cada detalle debe tener una cantidad mayor que cero.");
                }
            }
        } else {
            if (devolucion.getIdProducto() <= 0) {
                throw new Exception("El ID del producto es obligatorio.");
            }
            if (devolucion.getCantidad() <= 0) {
                throw new Exception("La cantidad debe ser mayor que cero.");
            }
        }
        if (devolucion.getIdUsuarioRegistra() <= 0) {
            throw new Exception("El ID del usuario que registra es obligatorio.");
        }
        if (devolucion.getMotivo() == null) {
            throw new Exception("El motivo de la devolucion es obligatorio.");
        }
        if (devolucion.getMotivo() == TipoDevolucion.OTRO) {
            if (devolucion.getMotivoDetalle() == null || devolucion.getMotivoDetalle().trim().isEmpty()) {
                throw new Exception("Cuando el motivo es 'OTRO', debe especificar el detalle del motivo.");
            }
        }
    }
}
