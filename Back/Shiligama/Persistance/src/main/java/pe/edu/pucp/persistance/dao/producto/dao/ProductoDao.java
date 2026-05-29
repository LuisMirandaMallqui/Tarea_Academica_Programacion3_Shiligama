package pe.edu.pucp.persistance.dao.producto.dao;

import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface ProductoDao extends IDAO<Producto> {

    /**
     * Búsqueda paginada con filtros opcionales (null = sin filtro).
     * @param categoriaId ID de categoría o null
     * @param q texto a buscar en NOMBRE o CODIGO_BARRAS, o null
     * @param precioMin precio mínimo o null
     * @param precioMax precio máximo o null
     * @param soloPromo true = solo productos con promoción vigente
     * @param pagina 1-based
     * @param tamano elementos por página
     */
    List<Producto> buscarPaginado(Integer categoriaId, String q,
                                  Double precioMin, Double precioMax,
                                  Boolean soloPromo, int pagina, int tamano);

    int contarFiltrados(Integer categoriaId, String q,
                        Double precioMin, Double precioMax,
                        Boolean soloPromo);

    Producto buscarPorCodigoBarras(String codigo);

    List<Producto> listarBajoStock();
}
