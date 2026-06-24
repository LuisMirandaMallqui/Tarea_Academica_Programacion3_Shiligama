package pe.edu.pucp.configuracion.bo;

import pe.edu.pucp.model.configuracion.Configuracion;

public interface ConfiguracionBo {
    Configuracion obtener() throws Exception;
    int actualizar(Configuracion cfg) throws Exception;
}
