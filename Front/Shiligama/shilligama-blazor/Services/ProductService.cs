using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// ProductService — consume el API REST del back.
//
// PUT /api/productos ahora sí actualiza stock y estado en MySQL
// porque el SP MODIFICAR_PRODUCTO fue extendido con esos dos parámetros.
// ============================================================================
public class ProductService
{
    private readonly HttpClient _http;
    private readonly List<Product> _products = new();
    private readonly List<Category> _categories = new();
    private bool _cargado = false;

    public string SearchQuery { get; set; } = string.Empty;
    public event Action? OnSearch;

    private readonly JsonSerializerOptions _json;

    public ProductService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    public void NotifySearch(string query)
    {
        SearchQuery = query;
        OnSearch?.Invoke();
    }

    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;

        try
        {
            var cats = await _http.GetFromJsonAsync<List<Category>>("categorias", _json);
            _categories.Clear();
            if (cats != null) _categories.AddRange(cats);
        }
        catch { }

        try
        {
            var prods = await _http.GetFromJsonAsync<List<Product>>("productos", _json);
            _products.Clear();
            if (prods != null) _products.AddRange(prods);
        }
        catch { }

        _cargado = true;
    }

    public Task RecargarAsync() => EnsureLoadedAsync(recargar: true);

    public List<Product> GetProducts() => _products;
    public List<Category> GetCategories() => _categories;
    public Product? GetProductById(int id) => _products.FirstOrDefault(p => p.Id == id);

    public List<Product> GetRelatedProducts(Product product, int limit = 4) =>
        _products.Where(p => p.Category == product.Category && p.Id != product.Id)
                 .Take(limit).ToList();

    public List<Product> GetDiscountedProducts() =>
        _products.Where(p => p.IsPromo && p.OriginalPrice.HasValue).ToList();

    public string GetCategoryName(string categoryId) =>
        _categories.FirstOrDefault(c => c.Id == categoryId)?.Name ?? categoryId;

    public async Task<PagedResult<Product>> SearchAsync(
        string? categoriaId = null,
        string? q = null,
        decimal? precioMin = null,
        decimal? precioMax = null,
        bool? soloPromo = null,
        int pagina = 1,
        int tamano = 8)
    {
        var filtros = new List<string>();
        if (!string.IsNullOrWhiteSpace(categoriaId) && int.TryParse(categoriaId, out var catId))
            filtros.Add($"categoria={catId}");
        if (!string.IsNullOrWhiteSpace(q))
            filtros.Add($"q={Uri.EscapeDataString(q)}");
        if (precioMin.HasValue)
            filtros.Add($"precioMin={precioMin.Value.ToString(CultureInfo.InvariantCulture)}");
        if (precioMax.HasValue)
            filtros.Add($"precioMax={precioMax.Value.ToString(CultureInfo.InvariantCulture)}");
        if (soloPromo.HasValue)
            filtros.Add($"soloPromo={(soloPromo.Value ? "true" : "false")}");
        filtros.Add($"pagina={pagina}");
        filtros.Add($"tamano={tamano}");

        var url = "productos/buscar?" + string.Join("&", filtros);
        using var resp = await _http.GetAsync(url);
        resp.EnsureSuccessStatusCode();

        var items = await resp.Content.ReadFromJsonAsync<List<Product>>() ?? new List<Product>();
        var total = LeerHeaderInt(resp, "X-Total-Count", items.Count);
        var size = LeerHeaderInt(resp, "X-Page-Size", tamano);
        var totalPaginas = LeerHeaderInt(resp, "X-Total-Pages",
            (int)Math.Ceiling(total / (double)Math.Max(1, size)));

        return new PagedResult<Product>
        {
            Items = items,
            TotalCount = total,
            Page = LeerHeaderInt(resp, "X-Page", pagina),
            PageSize = size,
            TotalPages = Math.Max(1, totalPaginas)
        };
    }

    private static int LeerHeaderInt(HttpResponseMessage resp, string nombre, int porDefecto)
    {
        if (resp.Headers.TryGetValues(nombre, out var v) ||
            resp.Content.Headers.TryGetValues(nombre, out v))
        {
            if (int.TryParse(v.FirstOrDefault(), out var n)) return n;
        }
        return porDefecto;
    }

    public async Task<Product?> GetByBarcodeAsync(string codigo) =>
        await _http.GetFromJsonAsync<Product>($"productos/codigo-barras/{Uri.EscapeDataString(codigo)}");

    public async Task<List<Product>> GetLowStockAsync() =>
        await _http.GetFromJsonAsync<List<Product>>("productos/bajo-stock") ?? new();

    public async Task<Product?> GetProductByIdAsync(int id) =>
        await _http.GetFromJsonAsync<Product>($"productos/{id}");

    // POST /api/productos
    public async Task<int> AddProductAsync(Product product)
    {
        var resp = await _http.PostAsJsonAsync("productos", product);
        resp.EnsureSuccessStatusCode();
        var id = await resp.Content.ReadFromJsonAsync<int>();
        product.Id = id;
        _products.Add(product);
        return id;
    }

    // PUT /api/productos — actualiza todos los campos incluyendo stock y estado
    // (el SP MODIFICAR_PRODUCTO ahora recibe esos dos parámetros adicionales)
    public async Task UpdateProductAsync(Product product)
    {
        var resp = await _http.PutAsJsonAsync("productos", product);
        resp.EnsureSuccessStatusCode();

        var existente = _products.FirstOrDefault(p => p.Id == product.Id);
        if (existente != null)
        {
            existente.Name = product.Name;
            existente.Description = product.Description;
            existente.Price = product.Price;
            existente.Stock = product.Stock;
            existente.MinStock = product.MinStock;
            existente.Estado = product.Estado;
            existente.Category = product.Category;
            existente.Image = product.Image;
            existente.CodigoBarras = product.CodigoBarras;
            existente.UnidadMedida = product.UnidadMedida;
            existente.IsPromo = product.IsPromo;
            existente.OriginalPrice = product.OriginalPrice;
        }
    }

    // DELETE /api/productos/{id}
    public async Task DeleteProductAsync(int id)
    {
        var resp = await _http.DeleteAsync($"productos/{id}");
        resp.EnsureSuccessStatusCode();
        var existente = _products.FirstOrDefault(p => p.Id == id);
        if (existente != null) _products.Remove(existente);
    }
}
