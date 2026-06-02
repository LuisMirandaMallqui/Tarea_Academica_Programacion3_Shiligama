package pe.edu.pucp.shiligama.servicios.imagen;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.imagen.ImagenUploadRequest;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

/**
 * Servicio REST para gestión de imágenes de productos.
 *
 * POST /api/imagenes/upload  → recibe JSON con datos en base64, guarda el archivo
 *                              y devuelve la URL pública.
 * GET  /api/imagenes/{filename} → sirve la imagen almacenada en el servidor.
 *
 * Las imágenes se guardan en ${user.home}/shiligama-imagenes/ por defecto.
 * Para cambiar la ruta, pasa la propiedad de sistema:
 *   -Dshiligama.images.dir=/ruta/personalizada
 */
@Path("/imagenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImagenWS {

    private static final long MAX_BYTES = 5L * 1024 * 1024; // 5 MB

    // ── Configuración ─────────────────────────────────────────────────────────

    /** Directorio local donde se guardan las imágenes. */
    private static final String IMAGES_DIR = resolverDirectorio();

    /**
     * URL base pública para construir la URL de cada imagen.
     * Debe coincidir con la URL del back tal como lo accede el front.
     */
    private static final String IMAGES_URL_BASE = resolverUrlBase();

    private static String resolverDirectorio() {
        String dir = System.getProperty("shiligama.images.dir");
        if (dir == null || dir.isBlank()) {
            dir = System.getProperty("user.home") + File.separator + "shiligama-imagenes";
        }
        // Asegurar separador final
        return dir.endsWith(File.separator) ? dir : dir + File.separator;
    }

    private static String resolverUrlBase() {
        String url = System.getProperty("shiligama.images.url");
        if (url == null || url.isBlank()) {
            url = "http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/imagenes/";
        }
        return url.endsWith("/") ? url : url + "/";
    }

    // ── Endpoints ─────────────────────────────────────────────────────────────

    /**
     * Sube una imagen recibida en Base64.
     * Body JSON: { "nombreArchivo": "foto.jpg", "datosBase64": "...", "tipoContenido": "image/jpeg" }
     * Respuesta: { "url": "http://localhost:8080/.../api/imagenes/abc123.jpg" }
     */
    @POST
    @Path("/upload")
    public Response subirImagen(ImagenUploadRequest req) {
        try {
            // ── Validar tipo de contenido ──────────────────────────────────────
            String tipo = req.getTipoContenido();
            if (tipo == null || !tipo.startsWith("image/")) {
                return respuestaError(Response.Status.BAD_REQUEST,
                        "Solo se permiten imágenes (image/jpeg, image/png, etc.)");
            }

            // ── Validar datos base64 ───────────────────────────────────────────
            String datos = req.getDatosBase64();
            if (datos == null || datos.isBlank()) {
                return respuestaError(Response.Status.BAD_REQUEST,
                        "No se recibieron datos de imagen.");
            }

            // Decodificar y validar tamaño
            byte[] bytes;
            try {
                bytes = Base64.getDecoder().decode(datos);
            } catch (IllegalArgumentException ex) {
                return respuestaError(Response.Status.BAD_REQUEST,
                        "Los datos base64 no son válidos.");
            }

            if (bytes.length > MAX_BYTES) {
                return respuestaError(Response.Status.BAD_REQUEST,
                        "La imagen supera el tamaño máximo permitido de 5 MB.");
            }

            // ── Preparar directorio ────────────────────────────────────────────
            File dir = new File(IMAGES_DIR);
            if (!dir.exists() && !dir.mkdirs()) {
                return respuestaError(Response.Status.INTERNAL_SERVER_ERROR,
                        "No se pudo crear el directorio de imágenes.");
            }

            // ── Nombre de archivo único (UUID + extensión) ─────────────────────
            String extension = resolverExtension(tipo, req.getNombreArchivo());
            String nombreUnico = UUID.randomUUID().toString() + extension;
            File archivo = new File(IMAGES_DIR + nombreUnico);

            // ── Guardar ────────────────────────────────────────────────────────
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                fos.write(bytes);
            }

            String url = IMAGES_URL_BASE + nombreUnico;
            return Response.ok("{\"url\":\"" + url + "\"}").build();

        } catch (Exception ex) {
            return respuestaError(Response.Status.INTERNAL_SERVER_ERROR,
                    "Error al subir imagen: " + ex.getMessage());
        }
    }

    /**
     * Sirve una imagen almacenada por su nombre de archivo.
     * GET /api/imagenes/abc123.jpg
     */
    @GET
    @Path("/{filename}")
    @Produces("*/*")
    public Response servirImagen(@PathParam("filename") String filename) {
        // Seguridad: prevenir path traversal
        if (filename == null || filename.contains("..") ||
                filename.contains("/") || filename.contains("\\")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        File archivo = new File(IMAGES_DIR + filename);
        if (!archivo.exists() || !archivo.isFile()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String contentType = resolverMime(filename);
        return Response.ok(archivo, contentType)
                .header("Cache-Control", "public, max-age=86400")
                .build();
    }

    // ── Utilidades privadas ───────────────────────────────────────────────────

    private String resolverExtension(String mimeType, String nombreArchivo) {
        switch (mimeType) {
            case "image/jpeg": return ".jpg";
            case "image/png":  return ".png";
            case "image/gif":  return ".gif";
            case "image/webp": return ".webp";
            default:
                // Intentar obtener extensión del nombre original
                if (nombreArchivo != null) {
                    int idx = nombreArchivo.lastIndexOf('.');
                    if (idx >= 0) {
                        return nombreArchivo.substring(idx).toLowerCase();
                    }
                }
                return ".jpg";
        }
    }

    private String resolverMime(String filename) {
        String f = filename.toLowerCase();
        if (f.endsWith(".jpg") || f.endsWith(".jpeg")) return "image/jpeg";
        if (f.endsWith(".png"))  return "image/png";
        if (f.endsWith(".gif"))  return "image/gif";
        if (f.endsWith(".webp")) return "image/webp";
        return "application/octet-stream";
    }

    private Response respuestaError(Response.Status status, String mensaje) {
        String json = "{\"error\":\"" + mensaje.replace("\"", "\\\"") + "\"}";
        return Response.status(status).entity(json).build();
    }
}
