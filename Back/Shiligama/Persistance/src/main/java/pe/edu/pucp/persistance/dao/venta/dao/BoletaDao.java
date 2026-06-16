package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.venta.Boleta;

public interface BoletaDao {
    int insertar(Boleta boleta);
    Boleta buscarPorVentaId(int ventaId);
    Boleta buscarPorSerieNumero(String serie, int numero);
    int obtenerSiguienteNumero(String serie);
}
