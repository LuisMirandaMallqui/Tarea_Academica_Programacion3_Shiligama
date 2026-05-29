package pe.edu.pucp.producto.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.producto.Producto;

import java.util.List;

public interface ProductoBo extends BaseBo<Producto> {

    List<Producto> buscarPaginado(Integer categoriaId, String q,
                                  Double precioMin, Double precioMax,
                                  Boolean soloPromo, int pagina, int tamano) throws Exception;

    int contarFiltrados(Integer categoriaId, String q,
                        Double precioMin, Double precioMax,
                        Boolean soloPromo) throws Exception;

    Producto buscarPorCodigoBarras(String codigo) throws Exception;

    List<Producto> listarBajoStock() throws Exception;
}
