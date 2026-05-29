package pe.edu.pucp.operacion.impl;

import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.operacion.bo.MovimientoInventarioBO;
import pe.edu.pucp.persistance.dao.operacion.Impl.MovimientoInventarioDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.dao.MovimientoInventarioDao;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoInventarioBoImpl implements MovimientoInventarioBO {
    private final MovimientoInventarioDao daoMovimiento;

    public MovimientoInventarioBoImpl() {
        this.daoMovimiento = new MovimientoInventarioDaoImpl();
    }

    @Override
    public int insertar(MovimientoInventario mov) throws Exception {
        validar(mov, false);
        return daoMovimiento.insertar(mov);
    }

    @Override
    public int modificar(MovimientoInventario mov) throws Exception {
        // Log inmutable
        throw new Exception("No se permite modificar un movimiento de inventario.");
    }

    @Override
    public int eliminar(int id) throws Exception {
        // Log inmutable
        throw new Exception("No se permite eliminar un movimiento de inventario.");
    }

    @Override
    public MovimientoInventario buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del movimiento debe ser mayor que cero.");
        }
        return daoMovimiento.buscarPorID(id);
    }

    @Override
    public List<MovimientoInventario> listarTodos() throws Exception {
        return daoMovimiento.listarTodos();
    }

    @Override
    public List<MovimientoInventario> listarPorProducto(int idProducto) throws Exception {
        if (idProducto <= 0) {
            throw new Exception("El ID del producto debe ser mayor que cero.");
        }
        return daoMovimiento.listarPorProducto(idProducto);
    }

    @Override
    public List<MovimientoInventario> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws Exception {
        if (fechaInicio == null || fechaFin == null) {
            throw new Exception("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new Exception("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return daoMovimiento.listarPorFechas(fechaInicio, fechaFin);
    }

    private void validar(MovimientoInventario mov, boolean esModificacion) throws Exception {
        if (mov == null) {
            throw new Exception("El movimiento no puede ser nulo.");
        }
        if (mov.getIdProducto() <= 0) {
            throw new Exception("El ID del producto es obligatorio.");
        }
        if (mov.getIdTrabajador() <= 0) {
            throw new Exception("El ID del trabajador es obligatorio.");
        }
        if (mov.getTipoMovimiento() == null || mov.getTipoMovimiento().trim().isEmpty()) {
            throw new Exception("El tipo de movimiento es obligatorio.");
        }
        if (mov.getCantidad() <= 0) {
            throw new Exception("La cantidad debe ser mayor que cero.");
        }
        if (mov.getMotivo() == null || mov.getMotivo().trim().isEmpty()) {
            throw new Exception("El motivo del movimiento es obligatorio.");
        }
    }
}
