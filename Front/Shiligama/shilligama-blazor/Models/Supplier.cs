using System;
using System.Collections.Generic;

namespace shilligama_blazor.Models;

public class Supplier
{
    public int Id { get; set; }
    public string RazonSocial { get; set; } = string.Empty;
    public string Ruc { get; set; } = string.Empty;
    public string Contacto { get; set; } = string.Empty;
    public string Telefono { get; set; } = string.Empty;
    public string Correo { get; set; } = string.Empty;
    public string Direccion { get; set; } = string.Empty;
    public List<string> Categorias { get; set; } = new();
    public string Estado { get; set; } = "activo"; // "activo" | "inactivo"
    public DateTime UltimoPedido { get; set; } = DateTime.Now;
}
