package pe.edu.pucp.persistance.dao.proveedor.dao;

import pe.edu.pucp.model.proveedor.OrdenReabastecimientoDto;
import pe.edu.pucp.persistance.dao.dao.IDAO;

public interface OrdenReabastecimientoDtoDAO extends IDAO<OrdenReabastecimientoDto> {
    int modificar(int id, String estado);
    int recibir_orden(int id);
}
