package pe.edu.pucp.shiligama.servicios.notificacion;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pe.edu.pucp.concurrente.JobNotificacionesProgramadas;

/**
 * Arranca el scheduler de notificaciones programadas (promociones por
 * vencer) cuando el servidor despliega la aplicacion, y lo detiene al
 * des-desplegarla. @WebListener lo registra automaticamente sin tocar
 * web.xml (el proyecto no usa web.xml, todo es por anotaciones).
 */
@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JobNotificacionesProgramadas.iniciar();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JobNotificacionesProgramadas.detener();
    }
}
