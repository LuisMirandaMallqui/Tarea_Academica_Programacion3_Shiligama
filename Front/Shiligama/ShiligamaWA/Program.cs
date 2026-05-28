using ShiligamaWA.Components;
using ShiligamaWA.Services;
using ShiligamaWA.Services.Mock;

var builder = WebApplication.CreateBuilder(args);

// Razor Components + Interactive Server
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// ========================================================
//  Servicios de dominio (mock por ahora - swap a API luego)
// ========================================================
// Singleton: catalogos/maestros estables durante la vida del servidor
builder.Services.AddSingleton<IProductoService, ProductoServiceMock>();
builder.Services.AddSingleton<ICategoriaService, CategoriaServiceMock>();
builder.Services.AddSingleton<IUsuarioService, UsuarioServiceMock>();

// Scoped: estado por usuario (carrito, sesion)
builder.Services.AddScoped<ICarritoService, CarritoServiceMock>();
builder.Services.AddScoped<ISesionService, SesionServiceMock>();

// Singleton para datos transaccionales mockeados (en memoria, compartidos)
builder.Services.AddSingleton<IPedidoService, PedidoServiceMock>();
builder.Services.AddSingleton<IVentaService, VentaServiceMock>();
builder.Services.AddSingleton<IDevolucionService, DevolucionServiceMock>();
builder.Services.AddSingleton<INotificacionService, NotificacionServiceMock>();
builder.Services.AddSingleton<IReporteService, ReporteServiceMock>();
builder.Services.AddSingleton<IPersonalService, PersonalServiceMock>();
builder.Services.AddSingleton<IInventarioService, InventarioServiceMock>();
builder.Services.AddSingleton<IOfertaService, OfertaServiceMock>();

var app = builder.Build();

// Configure the HTTP request pipeline.
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
