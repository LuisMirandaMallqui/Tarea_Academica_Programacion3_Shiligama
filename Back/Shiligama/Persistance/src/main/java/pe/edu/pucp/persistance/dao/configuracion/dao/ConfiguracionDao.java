package pe.edu.pucp.persistance.dao.configuracion.dao;

import pe.edu.pucp.model.configuracion.Configuracion;

public interface ConfiguracionDao {
    Configuracion obtener();
    int actualizar(Configuracion cfg);
}
