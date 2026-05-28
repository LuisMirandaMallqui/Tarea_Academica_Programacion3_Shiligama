using ShiligamaWA.Models;

namespace ShiligamaWA.Services.Mock;

public class UsuarioServiceMock : IUsuarioService
{
    private readonly List<Usuario> _data = SeedData.Usuarios();
    private readonly object _lock = new();
    private int _nextId;

    public UsuarioServiceMock() => _nextId = _data.Max(u => u.Id) + 1;

    public Usuario? AutenticarPorEmail(string email, string password, RolUsuario rolEsperado)
    {
        lock (_lock)
        {
            return _data.FirstOrDefault(u =>
                u.Activo &&
                u.Email.Equals(email, StringComparison.OrdinalIgnoreCase) &&
                u.Password == password &&
                u.Rol == rolEsperado);
        }
    }

    public Usuario? ObtenerPorEmail(string email)
    {
        lock (_lock) return _data.FirstOrDefault(u => u.Email.Equals(email, StringComparison.OrdinalIgnoreCase));
    }

    public Usuario? Obtener(int id)
    {
        lock (_lock) return _data.FirstOrDefault(u => u.Id == id);
    }

    public Usuario Registrar(Usuario u)
    {
        lock (_lock)
        {
            if (_data.Any(x => x.Email.Equals(u.Email, StringComparison.OrdinalIgnoreCase)))
                throw new InvalidOperationException("El correo ya está registrado");
            u.Id = _nextId++;
            u.FechaRegistro = DateTime.Now;
            u.Rol = RolUsuario.Cliente;
            _data.Add(u);
            return u;
        }
    }

    public IReadOnlyList<Usuario> ListarClientes()
    {
        lock (_lock) return _data.Where(u => u.Rol == RolUsuario.Cliente && u.Activo).ToList();
    }
}

public class SesionServiceMock : ISesionService
{
    public event Action? OnChange;

    public SesionActual Actual { get; private set; } = new();

    public void IniciarSesion(Usuario u)
    {
        Actual = new SesionActual
        {
            Autenticado = true,
            UsuarioId = u.Id,
            Email = u.Email,
            NombreCompleto = u.NombreCompleto,
            Iniciales = u.Iniciales,
            Rol = u.Rol
        };
        OnChange?.Invoke();
    }

    public void CerrarSesion()
    {
        Actual = new SesionActual();
        OnChange?.Invoke();
    }
}
