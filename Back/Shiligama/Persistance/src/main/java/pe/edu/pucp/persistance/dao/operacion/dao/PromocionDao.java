package pe.edu.pucp.persistance.dao.operacion.dao;

import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface PromocionDao extends IDAO<Promocion> {
    List<Promocion> listarVigentes();
    int asociarProducto(int idPromocion, int idProducto);
    int desasociarProducto(int idPromocion, int idProducto);
    List<Integer> listarProductosPorPromocion(int idPromocion);
    // Nuevo: devuelve todas las promos con sus IDs de productos en una sola query (sin N+1)
    List<Promocion> listarTodasConProductos();
}
