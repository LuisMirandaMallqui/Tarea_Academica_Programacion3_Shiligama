package pe.edu.pucp.shiligama.servicios;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Filtro CORS para GlassFish / JAX-RS.
 *
 * Necesario para que el navegador pueda hacer fetch() directo al back
 * (localhost:8080) desde la app Blazor (localhost:5xxx/7xxx).
 * Sin este filtro el browser bloquea la petición con "CORS policy" error.
 *
 * En producción reemplazar "*" por el dominio real.
 */
@Provider
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // ── Preflight OPTIONS ────────────────────────────────────────────────────
    @Override
    public void filter(ContainerRequestContext req) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            req.abortWith(Response.ok()
                    .header("Access-Control-Allow-Origin",  "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .header("Access-Control-Max-Age",       "86400")
                    .build());
        }
    }

    // ── Respuestas reales ────────────────────────────────────────────────────
    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext resp) {
        resp.getHeaders().putSingle("Access-Control-Allow-Origin",  "*");
        resp.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
