using System;
using System.Collections.Generic;
using System.Linq;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

public class SupplierService
{
    private readonly List<Supplier> _suppliers;

    public SupplierService()
    {
        _suppliers = new List<Supplier>
        {
            new()
            {
                Id = 1,
                RazonSocial = "Distribuidora San Jorge S.A.C.",
                Ruc = "20458796321",
                Contacto = "Jorge Pérez",
                Telefono = "987654321",
                Correo = "jorge@sanjorge.com",
                Direccion = "Av. Larco 456, Trujillo",
                Categorias = new List<string> { "Abarrotes", "Snacks" },
                Estado = "activo",
                UltimoPedido = DateTime.Now.AddDays(-5)
            },
            new()
            {
                Id = 2,
                RazonSocial = "Gloria S.A.",
                Ruc = "20100058620",
                Contacto = "Ana María Mendoza",
                Telefono = "963258741",
                Correo = "ventas@gloria.com.pe",
                Direccion = "Av. República de Panamá 2461, Lima",
                Categorias = new List<string> { "Lácteos" },
                Estado = "activo",
                UltimoPedido = DateTime.Now.AddDays(-2)
            },
            new()
            {
                Id = 3,
                RazonSocial = "Bebidas del Norte S.R.L.",
                Ruc = "20569874123",
                Contacto = "Luis Flores",
                Telefono = "954123687",
                Correo = "lflores@bebidasnorte.pe",
                Direccion = "Jr. Pizarro 789, Trujillo",
                Categorias = new List<string> { "Bebidas" },
                Estado = "activo",
                UltimoPedido = DateTime.Now.AddDays(-10)
            },
            new()
            {
                Id = 4,
                RazonSocial = "Consorcio Avícola del Norte",
                Ruc = "20784512963",
                Contacto = "Héctor Espinoza",
                Telefono = "912457896",
                Correo = "contacto@avicolanorte.com",
                Direccion = "Av. Industrial 120, Chiclayo",
                Categorias = new List<string> { "Carnes y Embutidos" },
                Estado = "inactivo",
                UltimoPedido = DateTime.Now.AddMonths(-1)
            }
        };
    }

    public List<Supplier> GetSuppliers() => _suppliers;

    public Supplier? GetSupplierById(int id) => _suppliers.FirstOrDefault(s => s.Id == id);

    public void AddSupplier(Supplier supplier)
    {
        supplier.Id = _suppliers.Count > 0 ? _suppliers.Max(s => s.Id) + 1 : 1;
        _suppliers.Insert(0, supplier);
    }

    public void UpdateSupplier(Supplier supplier)
    {
        var existing = GetSupplierById(supplier.Id);
        if (existing != null)
        {
            existing.RazonSocial = supplier.RazonSocial;
            existing.Ruc = supplier.Ruc;
            existing.Contacto = supplier.Contacto;
            existing.Telefono = supplier.Telefono;
            existing.Correo = supplier.Correo;
            existing.Direccion = supplier.Direccion;
            existing.Categorias = supplier.Categorias;
            existing.Estado = supplier.Estado;
            existing.UltimoPedido = supplier.UltimoPedido;
        }
    }

    public void DeleteSupplier(int id)
    {
        var existing = GetSupplierById(id);
        if (existing != null)
        {
            _suppliers.Remove(existing);
        }
    }

    public void ToggleStatus(int id)
    {
        var existing = GetSupplierById(id);
        if (existing != null)
        {
            existing.Estado = existing.Estado == "activo" ? "inactivo" : "activo";
        }
    }
}
