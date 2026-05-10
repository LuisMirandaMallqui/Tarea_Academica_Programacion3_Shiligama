package pe.edu.pucp.persistance.dao.operacion.dao;

import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

/**
 * Interfaz DAO específica para la entidad Promocion.
 */
public interface PromocionDao extends IDAO<Promocion> {
    // Aquí se pueden agregar firmas de métodos específicos de Promocion
    List<Promocion> listarVigentes();
    int asociarProducto(int idPromocion, int idProducto);
    int desasociarProducto(int idPromocion, int idProducto);
    List<Integer> listarProductosPorPromocion(int idPromocion);
}

