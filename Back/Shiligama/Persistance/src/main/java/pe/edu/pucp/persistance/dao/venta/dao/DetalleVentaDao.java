package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface DetalleVentaDao extends IDAO<DetalleVenta> {
    List<DetalleVenta> listarPorVenta(int idVenta);
}