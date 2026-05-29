package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.venta.Boleta;

import java.util.List;

/**
 * DAO de Boleta. NO hereda IDAO porque Boleta es una "fase" de Venta
 * (la tabla 'venta' ya tiene los campos de boleta). Operaciones específicas:
 * - emitir: agrega NUMERO_BOLETA, RUC, CONTACTO, MENSAJE a una venta existente
 * - anular: limpia los campos de boleta de la venta
 */
public interface BoletaDao {
    String emitir(int idVenta, String ruc, String contactoCliente, String mensajeBoleta);
    Boleta buscarPorIdVenta(int idVenta);
    Boleta buscarPorNumero(String numeroBoleta);
    List<Boleta> listarTodas();
    int anular(int idVenta);
}
