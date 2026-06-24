using System.Text.Json;
using Microsoft.AspNetCore.SignalR;
using shilligama_blazor.Components;
using shilligama_blazor.Services;
using shilligama_blazor.Shared;

// ── Diagnóstico: loguea excepciones no manejadas para encontrar la causa del crash ──
// Ver salida en el panel "Output" de Visual Studio (categoría "Debug").
AppDomain.CurrentDomain.UnhandledException += (_, e) =>
{
    Console.Error.WriteLine($"[CRASH] UnhandledException (IsTerminating={e.IsTerminating}): {e.ExceptionObject}");
};
TaskScheduler.UnobservedTaskException += (_, e) =>
{
    Console.Error.WriteLine($"[WARN] UnobservedTaskException: {e.Exception?.Message}");
    e.SetObserved(); // evita crash por tarea olvidada
};

var builder = WebApplication.CreateBuilder(args);

// ── Razor / Blazor Server ────────────────────────────────────────────────────
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents(options =>
    {
        // Evita que el circuito se pause durante uploads largos de imágenes.
        options.DisconnectedCircuitRetentionPeriod = TimeSpan.FromMinutes(5);
        options.JSInteropDefaultCallTimeout = TimeSpan.FromMinutes(2);
    })
    .AddHubOptions(options =>
    {
        // Límite de mensaje SignalR: 10 MB.
        // El JS interop devuelve el archivo como Base64 (un 5 MB raw → ~7 MB Base64),
        // por lo que necesitamos un margen mayor que el tamaño máximo del archivo.
        options.MaximumReceiveMessageSize = 10L * 1024 * 1024;
    });

// ── HttpClient apuntando al API REST del back ────────────────────────────────
// Base URL configurable en appsettings.json (clave "ApiBaseUrl").
var apiBaseUrl = builder.Configuration["ApiBaseUrl"]
                 ?? "http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/";

// Opciones JSON globales: case-insensitive para que los nombres del JSON del
// backend Java (camelCase: idVenta, fechaHora…) mapeen automáticamente a las
// propiedades C# (PascalCase: IdVenta, FechaHora…) sin atributos extra.
var jsonOpts = new JsonSerializerOptions
{
    PropertyNameCaseInsensitive = true,
    // Converters que hacen que DateTime se serialice como "yyyy-MM-ddTHH:mm:ss"
    // (sin offset ni microsegundos) para que el backend Java pueda parsearlo
    // con @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss") sobre LocalDateTime.
    Converters =
    {
        new JavaLocalDateTimeConverter(),
        new NullableJavaLocalDateTimeConverter()
    }
};
builder.Services.AddSingleton(jsonOpts);

// Singleton compartido por todos los servicios (sin estado de sesión HTTP).
var httpClient = new HttpClient { BaseAddress = new Uri(apiBaseUrl) };
builder.Services.AddSingleton(httpClient);

// ── Servicios de la app ──────────────────────────────────────────────────────
// AuthService y CartService son Scoped (dependen del usuario actual).
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<CartService>();
builder.Services.AddScoped<AddressService>();

// Los demás son Singleton: comparten caché en memoria entre peticiones
// (correcto para un sistema single-tenant de minimarket). Si fueran Scoped, cada usuario tendría su propia instancia sin compartir datos cacheados (ej. lista de productos)
builder.Services.AddSingleton<ProductService>();
builder.Services.AddSingleton<CategoryService>();
builder.Services.AddSingleton<SalesService>();
builder.Services.AddSingleton<ReturnsService>();
builder.Services.AddSingleton<StaffService>();
builder.Services.AddSingleton<PromocionService>();
builder.Services.AddSingleton<NotificacionService>();
builder.Services.AddSingleton<ReporteService>();
builder.Services.AddSingleton<ImagenService>();
builder.Services.AddSingleton<PagoService>();
builder.Services.AddScoped<RecuperacionService>();

// SupplierService no tiene endpoint en el backend por lo tanto sigue siendo local.
builder.Services.AddSingleton<SupplierService>();
builder.Services.AddSingleton<DetalleVentaService>();
builder.Services.AddSingleton<LotesService>();

// ── Pipeline HTTP ────────────────────────────────────────────────────────────
var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();
app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

// ── Proxy de imágenes: evita mixed-content y CORS en Brave/Edge ─────────────
//
// El browser SIEMPRE habla con https://localhost:7282 (mismo origen).
// Blazor reenvía la petición a GlassFish server-to-server (sin restricciones).
//
// POST /api/img-upload  → recibe JSON, lo pasa a GlassFish, devuelve URL proxy
// GET  /api/img/{file}  → descarga la imagen de GlassFish y la sirve en HTTPS

app.MapPost("/api/img-upload", async (HttpRequest req, HttpClient glassHttp) =>
{
    // Leer body del browser
    using var reader = new StreamReader(req.Body);
    var jsonBody = await reader.ReadToEndAsync();

    // Reenviar a GlassFish (server-to-server, sin restricciones de browser)
    using var content = new StringContent(jsonBody, System.Text.Encoding.UTF8, "application/json");
    HttpResponseMessage glassResp;
    try
    {
        glassResp = await glassHttp.PostAsync("imagenes/upload", content);
    }
    catch (Exception ex)
    {
        return Results.Json(new { error = "No se pudo conectar con el servidor: " + ex.Message },
                            statusCode: 502);
    }

    var respStr = await glassResp.Content.ReadAsStringAsync();

    if (!glassResp.IsSuccessStatusCode)
    {
        // Propagar error de GlassFish al browser manteniendo el formato {"error":"..."}
        string errorMsg = "Error al subir imagen";
        try
        {
            using var errDoc = JsonDocument.Parse(respStr);
            if (errDoc.RootElement.TryGetProperty("error", out var errProp))
                errorMsg = errProp.GetString() ?? errorMsg;
        }
        catch { }
        return Results.Json(new { error = errorMsg }, statusCode: (int)glassResp.StatusCode);
    }

    // Transformar URL de GlassFish (http://localhost:8080/…/imagenes/file.jpg)
    // → URL proxy Blazor (/api/img/file.jpg) que es HTTPS y mismo origen
    try
    {
        using var doc = JsonDocument.Parse(respStr);
        if (doc.RootElement.TryGetProperty("url", out var urlProp))
        {
            var glassUrl = urlProp.GetString() ?? "";
            var filename = glassUrl[(glassUrl.LastIndexOf('/') + 1)..];
            return Results.Json(new { url = "/api/img/" + filename });
        }
    }
    catch { }

    return Results.Json(new { error = "Respuesta inesperada del servidor" }, statusCode: 500);
}).DisableAntiforgery();

app.MapGet("/api/img/{filename}", async (string filename, HttpClient glassHttp) =>
{
    // Seguridad: prevenir path traversal
    if (string.IsNullOrWhiteSpace(filename) ||
        filename.Contains("..") || filename.Contains('/') || filename.Contains('\\'))
        return Results.BadRequest();

    HttpResponseMessage resp;
    try { resp = await glassHttp.GetAsync("imagenes/" + filename); }
    catch { return Results.StatusCode(502); }

    if (!resp.IsSuccessStatusCode) return Results.NotFound();

    var bytes = await resp.Content.ReadAsByteArrayAsync();
    var ct = resp.Content.Headers.ContentType?.ToString() ?? "image/jpeg";
    // Cache pública 24 h (igual que GlassFish)
    return Results.File(bytes, ct, enableRangeProcessing: false);
});

app.Run();
