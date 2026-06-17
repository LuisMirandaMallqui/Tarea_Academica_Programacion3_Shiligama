using System.Text.Json;
using Microsoft.JSInterop;

namespace shilligama_blazor.Services;

public class AddressService
{
    private static readonly JsonSerializerOptions JsonOptions = new()
    {
        PropertyNameCaseInsensitive = true
    };

    private readonly IJSRuntime _jsRuntime;
    private List<string> _addresses = new();
    private bool _isInitialized;

    public event Action? OnChange;

    public AddressService(IJSRuntime jsRuntime)
    {
        _jsRuntime = jsRuntime;
    }

    public List<string> GetAddresses() => _addresses;

    public async Task InitializeAsync()
    {
        if (_isInitialized) return;

        try
        {
            var json = await _jsRuntime.InvokeAsync<string>("localStorageHelper.getItem", "shiligama-addresses");
            if (!string.IsNullOrEmpty(json))
            {
                _addresses = JsonSerializer.Deserialize<List<string>>(json, JsonOptions) ?? new();
            }
            else
            {
                _addresses = new List<string>();
                await SaveToLocalStorageAsync();
            }

            _isInitialized = true;
            NotifyStateChanged();
        }
        catch
        {
            // Fail silently during server-side prerender
        }
    }

    public async Task AddAddressAsync(string address)
    {
        if (string.IsNullOrWhiteSpace(address)) return;

        var trimmed = address.Trim();
        if (_addresses.Any(a => string.Equals(a, trimmed, StringComparison.OrdinalIgnoreCase)))
            return;

        _addresses.Add(trimmed);
        await SaveToLocalStorageAsync();
        NotifyStateChanged();
    }

    public async Task RemoveAddressAsync(string address)
    {
        var existing = _addresses.FirstOrDefault(a => a == address);
        if (existing == null) return;

        _addresses.Remove(existing);
        await SaveToLocalStorageAsync();
        NotifyStateChanged();
    }

    private async Task SaveToLocalStorageAsync()
    {
        try
        {
            var json = JsonSerializer.Serialize(_addresses, JsonOptions);
            await _jsRuntime.InvokeVoidAsync("localStorageHelper.setItem", "shiligama-addresses", json);
        }
        catch
        {
            // Silently swallow errors during server-side renders
        }
    }

    private void NotifyStateChanged() => OnChange?.Invoke();
}
