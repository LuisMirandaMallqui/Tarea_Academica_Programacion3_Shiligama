package pe.edu.pucp.venta.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.venta.DetalleVenta;

import java.util.List;

public interface DetalleVentaBo extends BaseBo<DetalleVenta> {

    List<DetalleVenta> listarPorVenta(int idVenta) throws Exception;
}