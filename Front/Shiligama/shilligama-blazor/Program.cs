using System.Text.Json;
using Microsoft.AspNetCore.SignalR;
using shilligama_blazor.Components;
using shilligama_blazor.Services;
using shilligama_blazor.Shared;

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
        // Límite de mensaje SignalR: 5 MB (necesario para InputFile subir imágenes).
        options.MaximumReceiveMessageSize = 5L * 1024 * 1024;
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

app.Run();
