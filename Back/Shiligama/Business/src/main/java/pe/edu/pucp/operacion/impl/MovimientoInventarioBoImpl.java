package pe.edu.pucp.operacion.impl;

import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.notificacion.impl.NotificacionHelper;
import pe.edu.pucp.operacion.bo.MovimientoInventarioBO;
import pe.edu.pucp.persistance.dao.operacion.Impl.MovimientoInventarioDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.dao.MovimientoInventarioDao;
import pe.edu.pucp.persistance.dao.producto.Impl.ProductoDaoImpl;
import pe.edu.pucp.persistance.dao.producto.dao.ProductoDao;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoInventarioBoImpl implements MovimientoInventarioBO {
    private final MovimientoInventarioDao daoMovimiento;
    private final ProductoDao daoProducto;

    public MovimientoInventarioBoImpl() {
        this.daoMovimiento = new MovimientoInventarioDaoImpl();
        this.daoProducto = new ProductoDaoImpl();
    }

    @Override
    public int insertar(MovimientoInventario mov) throws Exception {
        validar(mov, false);
        int idMovimiento = daoMovimiento.insertar(mov);

        // Despues de registrar el movimiento, el SP ya actualizo producto.STOCK.
        // Si el stock resultante quedo en o por debajo del minimo, se notifica
        // a los administradores (broadcast: ID_DESTINATARIO = NULL).
        try {
            Producto producto = daoProducto.buscarPorId(mov.getIdProducto());
            if (producto != null && producto.getStock() <= producto.getStockMinimo()) {
                NotificacionHelper.notificarBroadcast(
                        "Stock bajo: " + producto.getNombre(),
                        "El producto \"" + producto.getNombre() + "\" tiene " + producto.getStock()
                                + " unidades (mínimo " + producto.getStockMinimo() + ").",
                        TipoNotificacion.STOCK_BAJO,
                        ReferenciaNotificacion.PRODUCTO, producto.getIdProducto());
            }
        } catch (Exception ex) {
            System.err.println("[MovimientoInventarioBoImpl] Error al evaluar stock bajo: " + ex.getMessage());
        }

        return idMovimiento;
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
    public MovimientoInventario buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del movimiento debe ser mayor que cero.");
        }
        return daoMovimiento.buscarPorId(id);
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
