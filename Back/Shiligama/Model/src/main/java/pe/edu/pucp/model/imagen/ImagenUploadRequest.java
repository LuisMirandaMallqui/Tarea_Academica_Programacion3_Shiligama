package pe.edu.pucp.model.imagen;

/**
 * DTO para recibir una imagen codificada en Base64 desde el front.
 * El front convierte el archivo a base64 antes de enviarlo como JSON.
 */
public class ImagenUploadRequest {

    /** Nombre original del archivo (solo para extraer la extensión). */
    private String nombreArchivo;

    /** Contenido del archivo codificado en Base64. */
    private String datosBase64;

    /** MIME type del archivo: image/jpeg, image/png, image/webp, image/gif */
    private String tipoContenido;

    public ImagenUploadRequest() {}

    public ImagenUploadRequest(String nombreArchivo, String datosBase64, String tipoContenido) {
        this.nombreArchivo = nombreArchivo;
        this.datosBase64 = datosBase64;
        this.tipoContenido = tipoContenido;
    }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getDatosBase64() { return datosBase64; }
    public void setDatosBase64(String datosBase64) { this.datosBase64 = datosBase64; }

    public String getTipoContenido() { return tipoContenido; }
    public void setTipoContenido(String tipoContenido) { this.tipoContenido = tipoContenido; }
}
