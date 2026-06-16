package pe.edu.pucp.venta.bo;

import pe.edu.pucp.model.venta.Boleta;

public interface BoletaBo {
    Boleta generarBoleta(int ventaId) throws Exception;
    Boleta buscarPorVentaId(int ventaId) throws Exception;
}
