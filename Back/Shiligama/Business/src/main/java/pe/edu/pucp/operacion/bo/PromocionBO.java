package pe.edu.pucp.operacion.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.promocion.Promocion;

import java.util.List;

public interface PromocionBO extends BaseBo<Promocion> {
    List<Promocion> listarVigentes() throws Exception;
    int asociarProducto(int idPromocion, int idProducto) throws Exception;
    int desasociarProducto(int idPromocion, int idProducto) throws Exception;
    List<Integer> listarProductosPorPromocion(int idPromocion) throws Exception;
    // Nuevo: una sola llamada en vez del loop N+1
    List<Promocion> listarTodasConProductos() throws Exception;
}
