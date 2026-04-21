package pe.edu.pucp.persistance.dao.operaciones.dao;

import pe.edu.pucp.persistance.dao.dao.IDAO;
import pe.edu.pucp.model.operaciones.Promocion;

import java.util.List;

/**
 * Interfaz DAO específica para la entidad Promocion.
 */
public interface PromocionDAO extends IDAO<Promocion> {
    // Aquí se pueden agregar firmas de métodos específicos de Promocion
    List<Promocion> listarVigentes();
    int asociarProducto(int idPromocion, int idProducto);
    int desasociarProducto(int idPromocion, int idProducto);
    List<Integer> listarProductosPorPromocion(int idPromocion);
}
