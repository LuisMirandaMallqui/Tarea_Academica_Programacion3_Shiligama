package pe.edu.pucp.persistance.dao.producto.dao;

import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface ProductoDao extends IDAO<Producto> {

    List<Producto> buscarPaginado(Integer categoriaId, String q,
                                  Double precioMin, Double precioMax,
                                  Boolean soloPromo, int pagina, int tamano);

    int contarFiltrados(Integer categoriaId, String q,
                        Double precioMin, Double precioMax,
                        Boolean soloPromo);

    Producto buscarPorCodigoBarras(String codigo);

    List<Producto> listarBajoStock();
}
