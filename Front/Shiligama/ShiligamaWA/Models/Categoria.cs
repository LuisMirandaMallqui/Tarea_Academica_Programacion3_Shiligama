namespace ShiligamaWA.Models;

public class Categoria
{
    public int Id { get; set; }
    public string Codigo { get; set; } = string.Empty;   // "abarrotes", "bebidas"
    public string Nombre { get; set; } = string.Empty;    // "Abarrotes"
    public string Icono { get; set; } = "fa-solid fa-box"; // FontAwesome
    public int Productos { get; set; }
}
