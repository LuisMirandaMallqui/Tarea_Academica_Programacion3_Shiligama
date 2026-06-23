package pe.edu.pucp.operacion.impl;

import pe.edu.pucp.model.operacion.Devolucion;
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
        return daoDevolucion.insertar(devolucion);
    }

    @Override
    public int modificar(Devolucion devolucion) throws Exception {
        validar(devolucion, true);
        return daoDevolucion.modificar(devolucion);
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
        if (devolucion.getMotivo() == null || devolucion.getMotivo().trim().isEmpty()) {
            throw new Exception("El motivo de la devolucion es obligatorio.");
        }
    }
}
