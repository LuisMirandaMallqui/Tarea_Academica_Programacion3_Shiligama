using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.JSInterop;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

public class CartService
{
    private readonly IJSRuntime _jsRuntime;
    private List<CartItem> _cartItems = new();
    private bool _isInitialized = false;

    public event Action? OnChange;

    public CartService(IJSRuntime jsRuntime)
    {
        _jsRuntime = jsRuntime;
    }

    public List<CartItem> GetCartItems()
    {
        return _cartItems;
    }

    public int GetCartCount()
    {
        return _cartItems.Sum(item => item.Quantity);
    }

    public decimal GetSubtotal()
    {
        return _cartItems.Sum(item => item.Price * item.Quantity);
    }

    // ── Tarifa de delivery (regla única del negocio) ─────────────────────
    // Tarifa base de S/ 5.00; gratis a partir de S/ 50.00 de subtotal.
    public const decimal DeliveryFeeBase = 5.00m;
    public const decimal FreeDeliveryThreshold = 50.00m;

    /// Tarifa de envío según la modalidad y el subtotal actual.
    /// isDelivery=false (recojo en tienda) => sin costo.
    public decimal GetDeliveryFee(bool isDelivery)
    {
        if (!isDelivery) return 0m;
        return GetSubtotal() > FreeDeliveryThreshold ? 0m : DeliveryFeeBase;
    }

    public async Task InitializeAsync()
    {
        if (_isInitialized) return;
        try
        {
            var json = await _jsRuntime.InvokeAsync<string>("localStorageHelper.getItem", "shiligama-cart");
            if (!string.IsNullOrEmpty(json))
            {
                _cartItems = JsonSerializer.Deserialize<List<CartItem>>(json) ?? new();
            }
            // Si no hay carrito guardado, simplemente empieza vacío
            _isInitialized = true;
            NotifyStateChanged();
        }
        catch
        {
            // Fail silently during server-side prerender
        }
    }

    public async Task AddToCartAsync(Product product)
    {
        var existing = _cartItems.FirstOrDefault(i => i.Id == product.Id);
        if (existing != null)
        {
            existing.Quantity += 1;
        }
        else
        {
            _cartItems.Add(new CartItem
            {
                Id       = product.Id,
                Name     = product.Name,
                Price    = product.Price,
                Image    = product.Image,
                Quantity = 1
            });
        }
        await SaveToLocalStorage();
        NotifyStateChanged();
    }

    public async Task RemoveFromCartAsync(int id)
    {
        var existing = _cartItems.FirstOrDefault(i => i.Id == id);
        if (existing != null)
        {
            _cartItems.Remove(existing);
            await SaveToLocalStorage();
            NotifyStateChanged();
        }
    }

    public async Task UpdateQuantityAsync(int id, int quantity)
    {
        if (quantity <= 0)
        {
            await RemoveFromCartAsync(id);
            return;
        }

        var existing = _cartItems.FirstOrDefault(i => i.Id == id);
        if (existing != null)
        {
            existing.Quantity = quantity;
            await SaveToLocalStorage();
            NotifyStateChanged();
        }
    }

    public async Task ClearCartAsync()
    {
        _cartItems.Clear();
        try
        {
            await _jsRuntime.InvokeVoidAsync("localStorageHelper.removeItem", "shiligama-cart");
        }
        catch { }
        NotifyStateChanged();
    }

    private async Task SaveToLocalStorage()
    {
        try
        {
            var json = JsonSerializer.Serialize(_cartItems);
            await _jsRuntime.InvokeVoidAsync("localStorageHelper.setItem", "shiligama-cart", json);
        }
        catch
        {
            // Silently swallow errors during server-side renders
        }
    }

    private void NotifyStateChanged() => OnChange?.Invoke();
}
