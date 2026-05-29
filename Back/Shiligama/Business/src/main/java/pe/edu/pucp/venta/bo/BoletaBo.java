package pe.edu.pucp.venta.bo;

import pe.edu.pucp.model.venta.Boleta;

import java.util.List;

/**
 * BO de Boleta. Operaciones específicas — no extiende BaseBo porque Boleta
 * es una fase de Venta, no una entidad CRUD independiente.
 */
public interface BoletaBo {
    String emitir(int idVenta, String ruc, String contactoCliente, String mensajeBoleta) throws Exception;
    Boleta buscarPorIdVenta(int idVenta) throws Exception;
    Boleta buscarPorNumero(String numeroBoleta) throws Exception;
    List<Boleta> listarTodas() throws Exception;
    int anular(int idVenta) throws Exception;
}
