package pe.edu.pucp.operacion.impl;

import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.operacion.bo.PromocionBO;
import pe.edu.pucp.persistance.dao.operacion.Impl.PromocionDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.dao.PromocionDao;

import java.util.List;

public class PromocionBoImpl implements PromocionBO {
    private final PromocionDao daoPromocion;

    public PromocionBoImpl() {
        this.daoPromocion = new PromocionDaoImpl();
    }

    @Override
    public int insertar(Promocion promocion) throws Exception {
        validar(promocion, false);
        return daoPromocion.insertar(promocion);
    }

    @Override
    public int modificar(Promocion promocion) throws Exception {
        validar(promocion, true);
        return daoPromocion.modificar(promocion);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la promocion debe ser mayor que cero.");
        }
        return daoPromocion.eliminar(id);
    }

    @Override
    public Promocion buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la promocion debe ser mayor que cero.");
        }
        return daoPromocion.buscarPorId(id);
    }

    @Override
    public List<Promocion> listarTodos() throws Exception {
        return daoPromocion.listarTodos();
    }

    @Override
    public List<Promocion> listarVigentes() throws Exception {
        return daoPromocion.listarVigentes();
    }

    @Override
    public int asociarProducto(int idPromocion, int idProducto) throws Exception {
        if (idPromocion <= 0 || idProducto <= 0) {
            throw new Exception("Los IDs de promocion y producto deben ser mayores que cero.");
        }
        return daoPromocion.asociarProducto(idPromocion, idProducto);
    }

    @Override
    public int desasociarProducto(int idPromocion, int idProducto) throws Exception {
        if (idPromocion <= 0 || idProducto <= 0) {
            throw new Exception("Los IDs de promocion y producto deben ser mayores que cero.");
        }
        return daoPromocion.desasociarProducto(idPromocion, idProducto);
    }

    @Override
    public List<Integer> listarProductosPorPromocion(int idPromocion) throws Exception {
        if (idPromocion <= 0) {
            throw new Exception("El ID de la promocion debe ser mayor que cero.");
        }
        return daoPromocion.listarProductosPorPromocion(idPromocion);
    }

    private void validar(Promocion promocion, boolean esModificacion) throws Exception {
        if (promocion == null) {
            throw new Exception("La promocion no puede ser nula.");
        }
        if (esModificacion && promocion.getIdPromocion() <= 0) {
            throw new Exception("El ID de la promocion es obligatorio para la modificacion.");
        }
        if (promocion.getNombre() == null || promocion.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la promocion es obligatorio.");
        }
        if (promocion.getTipoDescuento() == null) {
            throw new Exception("El tipo de descuento es obligatorio.");
        }
        if (promocion.getValorDescuento() < 0) {
            throw new Exception("El valor del descuento no puede ser negativo.");
        }
        if (promocion.getFechaInicio() == null || promocion.getFechaFin() == null) {
            throw new Exception("Las fechas de inicio y fin son obligatorias.");
        }
        if (promocion.getFechaInicio().isAfter(promocion.getFechaFin())) {
            throw new Exception("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }
}
