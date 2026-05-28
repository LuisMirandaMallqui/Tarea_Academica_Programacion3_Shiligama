package pe.edu.pucp.shiligama.servicios;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// Define que todas las rutas REST empiezan con /api
// URL completa: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/clientes
@ApplicationPath("/api")
public class ShiligamaApp extends Application {
}