package pe.edu.pucp.usuario.impl;

import java.util.List;
import pe.edu.pucp.correo.EnvioCorreo;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.persistance.dao.usuario.impl.ClienteDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.usuario.bo.ClienteBo;

public class ClienteBoImpl implements ClienteBo {
    private final UsuarioDao<Cliente> daoCliente;

    public ClienteBoImpl() {
        daoCliente = new ClienteDaoImpl();
    }

    @Override
    public int insertar(Cliente cliente) throws Exception {
        validar(cliente, false);
        int idGenerado = daoCliente.insertar(cliente);

        // Enviar correo de bienvenida (fallo silencioso: no bloquea el registro)
        try {
            String html = construirHtmlBienvenida(cliente.getNombres(), cliente.getCorreo());
            EnvioCorreo.getInstance().enviarEmail(
                List.of(cliente.getCorreo()),
                "¡Bienvenido a Shiligama! 🎉",
                html
            );
        } catch (Exception e) {
            System.err.println("[ClienteBoImpl] No se pudo enviar correo de bienvenida: " + e.getMessage());
        }

        return idGenerado;
    }

    private String construirHtmlBienvenida(String nombres, String correo) {
        String nombre = (nombres != null && !nombres.isBlank()) ? nombres : "cliente";
        return """
               <div style="font-family:Arial,sans-serif;max-width:540px;margin:auto;
                           border:1px solid #e5e7eb;border-radius:10px;overflow:hidden;">

                 <!-- Header verde -->
                 <div style="background:#0D4525;padding:28px 24px;text-align:center;">
                   <h1 style="color:#ffffff;margin:0;font-size:1.6rem;letter-spacing:-.01em;">
                     Shiligama's Minimarket
                   </h1>
                   <p style="color:#a7f3d0;margin:6px 0 0;font-size:.9rem;">
                     Tu tienda de confianza
                   </p>
                 </div>

                 <!-- Cuerpo -->
                 <div style="padding:28px 24px;background:#ffffff;">
                   <p style="font-size:1.1rem;color:#111827;margin-top:0;">
                     ¡Hola, <strong>%s</strong>! 👋
                   </p>
                   <p style="color:#374151;line-height:1.6;">
                     Tu cuenta en <strong>Shiligama</strong> fue creada exitosamente.
                     Ya puedes explorar nuestro catálogo, hacer pedidos y disfrutar
                     de todas las ventajas de ser cliente registrado.
                   </p>

                   <!-- Beneficios -->
                   <div style="background:#f0fdf4;border-radius:8px;padding:16px 20px;
                               margin:20px 0;border-left:4px solid #0D4525;">
                     <p style="margin:0 0 8px;font-weight:700;color:#0D4525;">
                       ¿Qué puedes hacer ahora?
                     </p>
                     <ul style="margin:0;padding-left:18px;color:#374151;line-height:1.8;">
                       <li>🛒 Explorar el catálogo completo</li>
                       <li>📦 Hacer pedidos con delivery o recojo en tienda</li>
                       <li>💳 Pagar con tarjeta, Yape o efectivo</li>
                       <li>📋 Revisar el historial de tus compras</li>
                     </ul>
                   </div>

                   <p style="text-align:center;margin:24px 0 8px;">
                     <a href="http://localhost:5273/catalogo"
                        style="background:#0D4525;color:#ffffff;text-decoration:none;
                               padding:13px 28px;border-radius:8px;display:inline-block;
                               font-weight:700;font-size:.95rem;">
                       Ir al catálogo →
                     </a>
                   </p>
                 </div>

                 <!-- Footer -->
                 <div style="background:#f9fafb;padding:16px 24px;text-align:center;
                             border-top:1px solid #e5e7eb;">
                   <p style="margin:0;font-size:.78rem;color:#9ca3af;">
                     Este correo fue enviado a %s porque te registraste en Shiligama.<br>
                     Si no creaste esta cuenta, ignora este mensaje.
                   </p>
                 </div>

               </div>
               """.formatted(nombre, correo);
    }

    @Override
    public int modificar(Cliente cliente) throws Exception {
        validar(cliente, true);
        return daoCliente.modificar(cliente);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del cliente debe ser mayor que cero.");
        }
        return daoCliente.eliminar(id);
    }

    @Override
    public Cliente buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del cliente debe ser mayor que cero.");
        }
        return daoCliente.buscarPorId(id);
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        return daoCliente.listarTodos();
    }

    @Override
    public Cliente buscarPorCorreo(String correo) throws Exception {
        validarTextoObligatorio(correo, "El correo es obligatorio.");
        return daoCliente.buscarPorCorreo(correo);
    }

    @Override
    public Cliente obtenerPorDNI(String dni) throws Exception {
        validarDNI(dni);
        return daoCliente.obtenerPorDNI(dni);
    }

    private void validar(Cliente cliente, boolean esModificacion) throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente no puede ser nulo.");
        }
        if (esModificacion && cliente.getIdUsuario() <= 0) {
            throw new Exception("El ID del cliente es obligatorio para la modificacion.");
        }
        validarDNI(cliente.getDni());
        validarTextoObligatorio(cliente.getNombres(), "El nombre del cliente es obligatorio.");
        validarTextoObligatorio(cliente.getApellidos(), "Los apellidos del cliente son obligatorios.");
        validarTextoObligatorio(cliente.getCorreo(), "El correo del cliente es obligatorio.");
        validarTextoObligatorio(cliente.getContrasena(), "La contrasena es obligatoria.");
        if (!esModificacion && daoCliente.existeUsuarioEnBD(cliente)) {
            throw new Exception("Ya existe un usuario con ese DNI o correo.");
        }
    }

    private void validarDNI(String dni) throws Exception {
        if (dni == null || dni.trim().isEmpty()) {
            throw new Exception("El DNI es obligatorio.");
        }
        if (!dni.trim().matches("\\d{8}")) {
            throw new Exception("El DNI debe tener exactamente 8 digitos numericos.");
        }
    }

    private void validarTextoObligatorio(String texto, String mensaje) throws Exception {
        if (texto == null || texto.trim().isEmpty()) {
            throw new Exception(mensaje);
        }
    }
}
