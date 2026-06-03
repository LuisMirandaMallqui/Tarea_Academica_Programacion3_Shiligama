package pe.edu.pucp.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Lector único de configuración para la capa de negocio.
 *
 * Carga (una sola vez) el archivo {@code shiligama-config.properties} del
 * classpath, que contiene las claves de la pasarela Izipay, las del correo
 * SMTP y la URL del frontend. Mantener los secretos fuera del código fuente
 * permite cumplir RNF006/RNF010 sin hardcodear credenciales.
 *
 * Las variables de entorno tienen prioridad sobre el archivo (útil en EC2):
 * para una clave {@code izipay.merchant.key} se busca primero la variable de
 * entorno {@code IZIPAY_MERCHANT_KEY}.
 */
public final class Config {

    private static final String ARCHIVO = "shiligama-config.properties";
    private static Properties propiedades;

    private Config() {
    }

    private static synchronized Properties cargar() {
        if (propiedades == null) {
            propiedades = new Properties();
            try (InputStream input = Config.class.getClassLoader()
                    .getResourceAsStream(ARCHIVO)) {
                if (input != null) {
                    propiedades.load(input);
                } else {
                    System.err.println("[Config] No se encontró " + ARCHIVO
                            + " en el classpath; se usarán solo variables de entorno.");
                }
            } catch (Exception ex) {
                System.err.println("[Config] Error cargando " + ARCHIVO + ": " + ex.getMessage());
            }
        }
        return propiedades;
    }

    /** Devuelve el valor de la clave, o {@code null} si no existe. */
    public static String get(String clave) {
        String envKey = clave.toUpperCase().replace('.', '_').replace('-', '_');
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) {
            return env;
        }
        return cargar().getProperty(clave);
    }

    /** Devuelve el valor de la clave o {@code porDefecto} si no está definida. */
    public static String get(String clave, String porDefecto) {
        String valor = get(clave);
        return (valor == null || valor.isBlank()) ? porDefecto : valor;
    }
}
