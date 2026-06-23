using System.Net.Http.Json;
using System.Text.Json;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

public class CategoryService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;
    private readonly List<Category> _categories = new();
    private bool _cargado;

    public CategoryService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
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
        _cargado = true;
    }

    public List<Category> GetAll() => _categories;

    public async Task<List<Category>> GetAllAsync()
    {
        await EnsureLoadedAsync();
        return _categories;
    }

    public async Task<int> CreateAsync(Category cat)
    {
        try
        {
            var resp = await _http.PostAsJsonAsync("categorias", cat, _json);
            if (resp.IsSuccessStatusCode)
            {
                int id = await resp.Content.ReadFromJsonAsync<int>();
                cat.IdCategoria = id;
                _categories.Add(cat);
                return id;
            }
        }
        catch { }
        return 0;
    }

    public async Task<bool> UpdateAsync(Category cat)
    {
        try
        {
            var resp = await _http.PutAsJsonAsync("categorias", cat, _json);
            if (resp.IsSuccessStatusCode)
            {
                var idx = _categories.FindIndex(c => c.Id == cat.Id);
                if (idx >= 0) _categories[idx] = cat;
                return true;
            }
        }
        catch { }
        return false;
    }

    public async Task<bool> DeleteAsync(int id)
    {
        try
        {
            var resp = await _http.DeleteAsync($"categorias/{id}");
            if (resp.IsSuccessStatusCode)
            {
                _categories.RemoveAll(c => c.IdCategoria == id);
                return true;
            }
        }
        catch { }
        return false;
    }
}
