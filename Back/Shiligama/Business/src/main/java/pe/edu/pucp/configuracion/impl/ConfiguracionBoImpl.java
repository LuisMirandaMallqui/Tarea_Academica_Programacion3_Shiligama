package pe.edu.pucp.configuracion.impl;

import pe.edu.pucp.configuracion.bo.ConfiguracionBo;
import pe.edu.pucp.model.configuracion.Configuracion;
import pe.edu.pucp.persistance.dao.configuracion.dao.ConfiguracionDao;
import pe.edu.pucp.persistance.dao.configuracion.impl.ConfiguracionDaoImpl;

public class ConfiguracionBoImpl implements ConfiguracionBo {

    private final ConfiguracionDao configuracionDao;

    public ConfiguracionBoImpl() {
        this.configuracionDao = new ConfiguracionDaoImpl();
    }

    @Override
    public Configuracion obtener() throws Exception {
        Configuracion cfg = configuracionDao.obtener();
        if (cfg == null) {
            throw new Exception("No se encontró la configuración del sistema.");
        }
        return cfg;
    }

    @Override
    public int actualizar(Configuracion cfg) throws Exception {
        if (cfg == null) throw new Exception("La configuración no puede ser nula.");
        if (cfg.getNombreTienda() == null || cfg.getNombreTienda().isBlank())
            throw new Exception("El nombre de la tienda es obligatorio.");
        if (cfg.getTarifaEnvio() < 0)
            throw new Exception("La tarifa de envío no puede ser negativa.");
        if (cfg.getMinimoEnvioGratis() < 0)
            throw new Exception("El mínimo para envío gratis no puede ser negativo.");
        return configuracionDao.actualizar(cfg);
    }
}
