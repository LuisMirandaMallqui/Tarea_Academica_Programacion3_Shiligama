package pe.edu.pucp.persistance.dao.configuracion.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.configuracion.Configuracion;
import pe.edu.pucp.persistance.dao.configuracion.dao.ConfiguracionDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracionDaoImpl implements ConfiguracionDao {

    // SP: OBTENER_CONFIGURACION()
    @Override
    public Configuracion obtener() {
        Configuracion cfg = null;
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("OBTENER_CONFIGURACION", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    cfg = mapear(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener configuración: " + ex.getMessage());
        }
        return cfg;
    }

    // SP: ACTUALIZAR_CONFIGURACION(nombre_tienda, moneda, igv, tarifa_envio, minimo_envio_gratis)
    @Override
    public int actualizar(Configuracion cfg) {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, cfg.getNombreTienda());
        params.put(2, cfg.getMoneda());
        params.put(3, cfg.getIgv());
        params.put(4, cfg.getTarifaEnvio());
        params.put(5, cfg.getMinimoEnvioGratis());
        return DBManager.getInstance().ejecutarProcedimiento(
                "ACTUALIZAR_CONFIGURACION", params, null);
    }

    private Configuracion mapear(ResultSet rs) throws SQLException {
        Configuracion cfg = new Configuracion();
        cfg.setNombreTienda(rs.getString("NOMBRE_TIENDA"));
        cfg.setMoneda(rs.getString("MONEDA"));
        cfg.setIgv(rs.getDouble("IGV"));
        cfg.setTarifaEnvio(rs.getDouble("TARIFA_ENVIO"));
        cfg.setMinimoEnvioGratis(rs.getDouble("MINIMO_ENVIO_GRATIS"));
        return cfg;
    }
}
