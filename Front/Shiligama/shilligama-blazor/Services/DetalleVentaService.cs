using System.Net.Http.Json;

public class DetalleVentaService
{
    private readonly HttpClient _http;

    public DetalleVentaService(HttpClient http)
    {
        _http = http;
    }

    // GET api/detalles-venta
    public async Task<List<DetalleVenta>> GetAllDetallesVentaAsync()
    {
        var result = await _http.GetFromJsonAsync<List<DetalleVenta>>("detalles-venta");
        return result ?? new List<DetalleVenta>();
    }

    // GET api/detalles-venta/{id}
    public async Task<DetalleVenta?> GetDetalleVentaByIdAsync(int id)
    {
        return await _http.GetFromJsonAsync<DetalleVenta>($"api/detalles-venta/{id}");
    }

    // GET api/detalles-venta/por-venta/{idVenta}
    public async Task<List<DetalleVenta>> GetDetallesPorVentaAsync(int idVenta)
    {
        var result = await _http.GetFromJsonAsync<List<DetalleVenta>>(
            $"api/detalles-venta/por-venta/{idVenta}"
        );
        return result ?? new List<DetalleVenta>();
    }

    // POST api/detalles-venta
    public async Task<int> InsertarDetalleVentaAsync(DetalleVenta detalle)
    {
        var response = await _http.PostAsJsonAsync("api/detalles-venta", detalle);
        response.EnsureSuccessStatusCode();
        return await response.Content.ReadFromJsonAsync<int>();
    }

    // PUT api/detalles-venta/{id}
    public async Task<bool> ModificarDetalleVentaAsync(DetalleVenta detalle)
    {
        var response = await _http.PutAsJsonAsync(
            $"api/detalles-venta/{detalle.idDetalleVenta}", detalle
        );
        return response.IsSuccessStatusCode;
    }

    // DELETE api/detalles-venta/{id}
    public async Task<bool> EliminarDetalleVentaAsync(int id)
    {
        var response = await _http.DeleteAsync($"api/detalles-venta/{id}");
        return response.IsSuccessStatusCode;
    }
}