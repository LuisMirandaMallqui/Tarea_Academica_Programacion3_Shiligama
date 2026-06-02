using System;
using System.IO;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Components.Forms;

namespace shilligama_blazor.Services;

/// <summary>
/// Servicio para subir imágenes al back (ImagenWS).
/// Convierte el archivo a Base64 y lo envía como JSON a POST /api/imagenes/upload.
/// El back guarda el archivo y devuelve la URL pública.
/// Esa URL se guarda en el campo imagenUrl del producto.
/// </summary>
public class ImagenService
{
    private readonly HttpClient _http;

    // Tamaño máximo permitido: 5 MB (debe coincidir con la validación del back)
    private const long MAX_FILE_SIZE = 5L * 1024 * 1024;

    // Tipos MIME permitidos
    private static readonly string[] TIPOS_PERMITIDOS =
    {
        "image/jpeg", "image/png", "image/gif", "image/webp"
    };

    public ImagenService(HttpClient http)
    {
        _http = http;
    }

    /// <summary>
    /// Sube un archivo de imagen al servidor y devuelve la URL pública resultante.
    /// </summary>
    /// <param name="file">Archivo seleccionado por el usuario vía InputFile.</param>
    /// <returns>URL completa de la imagen en el servidor.</returns>
    /// <exception cref="InvalidOperationException">Si el tipo o tamaño no son válidos.</exception>
    public async Task<string> UploadAsync(IBrowserFile file)
    {
        if (file == null)
            throw new ArgumentNullException(nameof(file));

        // ── Validar tipo ──────────────────────────────────────────────────────
        var tipo = file.ContentType;
        if (Array.IndexOf(TIPOS_PERMITIDOS, tipo) < 0)
            throw new InvalidOperationException(
                "Tipo de archivo no permitido. Use JPG, PNG, GIF o WebP.");

        // ── Validar tamaño ────────────────────────────────────────────────────
        if (file.Size > MAX_FILE_SIZE)
            throw new InvalidOperationException(
                "La imagen no debe superar 5 MB.");

        // ── Leer bytes y convertir a Base64 ───────────────────────────────────
        await using var stream = file.OpenReadStream(maxAllowedSize: MAX_FILE_SIZE);
        using var ms = new MemoryStream();
        await stream.CopyToAsync(ms);
        var base64 = Convert.ToBase64String(ms.ToArray());

        // ── Enviar al back ────────────────────────────────────────────────────
        var cuerpo = new
        {
            nombreArchivo = file.Name,
            datosBase64   = base64,
            tipoContenido = tipo
        };

        var resp = await _http.PostAsJsonAsync("imagenes/upload", cuerpo);
        resp.EnsureSuccessStatusCode();

        // ── Parsear respuesta { "url": "..." } ────────────────────────────────
        using var doc = await JsonDocument.ParseAsync(
                            await resp.Content.ReadAsStreamAsync());

        if (doc.RootElement.TryGetProperty("url", out var urlElem))
            return urlElem.GetString() ?? string.Empty;

        if (doc.RootElement.TryGetProperty("error", out var errElem))
            throw new InvalidOperationException(
                errElem.GetString() ?? "Error desconocido al subir imagen.");

        throw new InvalidOperationException("Respuesta inesperada del servidor.");
    }
}
