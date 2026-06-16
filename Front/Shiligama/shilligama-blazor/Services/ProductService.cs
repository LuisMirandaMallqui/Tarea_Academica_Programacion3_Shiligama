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
// ProductService — ANTES tenía la lista de productos "hardcodeada" en memoria.
// AHORA consume el API REST del back (ProductoWS / CategoriaWS) vía HttpClient.
// Se mantienen los MISMOS métodos que ya usaban las páginas (GetProducts,
// GetCategories, GetCategoryName, etc.) para no tener que reescribir los .razor;
// solo cambian sus "tripas": ahora los datos vienen del servidor.
//
// Endpoints usados:
//   GET /api/categorias                  -> lista de categorías
//   GET /api/productos                   -> lista completa (caché del front)
//   GET /api/productos/{id}              -> un producto
//   GET /api/productos/buscar?...        -> búsqueda PAGINADA (headers X-Total-*)
//   GET /api/productos/codigo-barras/{c} -> POS
//   GET /api/productos/bajo-stock        -> inventario
//   POST/PUT/DELETE /api/productos       -> alta/edición/baja
// ============================================================================
public class ProductService
{
    private readonly HttpClient _http;

    // Caché en memoria: la llena EnsureLoadedAsync() una sola vez y la leen
    // los getters síncronos que ya usaban las páginas.
    private readonly List<Product> _products = new();
    private readonly List<Category> _categories = new();
    private bool _cargado = false;

    // Búsqueda del navbar (se mantiene igual que antes).
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

    // Carga la caché desde el API (productos + categorías) una sola vez.
    // Las páginas que usan los getters síncronos deben llamar esto (await)
    // en su OnInitializedAsync antes de leer la lista.
    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;

        var cats = await _http.GetFromJsonAsync<List<Category>>("categorias", _json);
        _categories.Clear();
        if (cats != null) _categories.AddRange(cats);

        var prods = await _http.GetFromJsonAsync<List<Product>>("productos", _json);
        _products.Clear();
        if (prods != null) _products.AddRange(prods);

        _cargado = true;
    }

    // ----- Getters síncronos sobre la caché (firmas iguales que antes) -----
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

    // ----- Búsqueda PAGINADA en el servidor (filtros + LIMIT/OFFSET) -----
    // Llama a /api/productos/buscar y lee el total de los headers HTTP que
    // expone el back (X-Total-Count, X-Total-Pages). Devuelve solo la página.
    public async Task<PagedResult<Product>> SearchAsync(
        string? categoriaId = null,
        string? q = null,
        decimal? precioMin = null,
        decimal? precioMax = null,
        bool? soloPromo = null,
        int pagina = 1,
        int tamano = 8)
    {
        // Armamos el query string solo con los filtros que vienen con valor
        // (el back interpreta los parámetros ausentes como "sin filtro").
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

    // Lee un header entero (los X-* pueden venir como header de respuesta o de contenido).
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

    // ----- CRUD admin (cableado al API). Las páginas actuales son de lectura,
    // pero quedan listos para los formularios de alta/edición. -----
    public async Task<int> AddProductAsync(Product product)
    {
        var resp = await _http.PostAsJsonAsync("productos", product);
        resp.EnsureSuccessStatusCode();
        var id = await resp.Content.ReadFromJsonAsync<int>();
        product.Id = id;
        _products.Add(product);
        return id;
    }

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
            existente.Category = product.Category;

            // Agrega aquí cualquier otro campo que tenga tu modelo Product
            existente.Image = product.Image;
            existente.IsPromo = product.IsPromo;
            existente.OriginalPrice = product.OriginalPrice;
        }
    }

    public async Task DeleteProductAsync(int id)
    {
        var resp = await _http.DeleteAsync($"productos/{id}");
        resp.EnsureSuccessStatusCode();
        var existente = GetProductById(id);
        if (existente != null) _products.Remove(existente);
    }
}
