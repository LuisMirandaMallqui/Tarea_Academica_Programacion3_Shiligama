package pe.edu.pucp.usuario.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import pe.edu.pucp.config.Config;
import pe.edu.pucp.config.HashUtil;
import pe.edu.pucp.correo.EnvioCorreo;
import pe.edu.pucp.model.seguridad.TokenRecuperacion;
import pe.edu.pucp.model.seguridad.UsuarioBasicoDto;
import pe.edu.pucp.persistance.dao.usuario.dao.RecuperacionDao;
import pe.edu.pucp.persistance.dao.usuario.impl.RecuperacionDaoImpl;
import pe.edu.pucp.usuario.bo.RecuperacionBo;

public class RecuperacionBoImpl implements RecuperacionBo {

    private final RecuperacionDao recuperacionDao;

    public RecuperacionBoImpl() {
        this.recuperacionDao = new RecuperacionDaoImpl();
    }

    @Override
    public boolean solicitarRecuperacion(String correo) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo es obligatorio.");
        }
        String correoNorm = correo.trim();

        UsuarioBasicoDto usuario = recuperacionDao.buscarUsuarioPorCorreo(correoNorm);
        // Por seguridad no revelamos si el correo existe o no.
        if (usuario == null) {
            return true;
        }

        // 1) Generar y persistir el token de un solo uso.
        String tokenValor = UUID.randomUUID().toString().replace("-", "");
        int minutos = parseEntero(Config.get("correo.token.minutos", "60"), 60);

        TokenRecuperacion token = new TokenRecuperacion();
        token.setIdUsuario(usuario.getIdUsuario());
        token.setToken(tokenValor);
        token.setExpiracion(LocalDateTime.now().plusMinutes(minutos));
        token.setUsado(false);
        recuperacionDao.crearToken(token);

        // 2) Construir el enlace de restablecimiento hacia el frontend.
        String baseUrl = Config.get("app.frontend.url", "http://localhost:3000");
        String enlace = baseUrl + "/restablecer-password?token=" + tokenValor;

        // 3) Enviar el correo HTML. Propagar excepción para que el WS la muestre.
        String asunto = "Shiligama — Recuperación de contraseña";
        String html = construirHtml(usuario.getNombres(), enlace, minutos);
        boolean enviado = EnvioCorreo.getInstance()
                .enviarEmail(List.of(usuario.getCorreo()), asunto, html);
        if (!enviado) {
            throw new Exception("No se pudo enviar el correo de recuperación.");
        }

        return true;
    }

    @Override
    public boolean restablecerContrasena(String token, String nuevaContrasena) throws Exception {
        if (token == null || token.trim().isEmpty()) {
            throw new Exception("El token es obligatorio.");
        }
        if (nuevaContrasena == null || nuevaContrasena.length() < 8) {
            throw new Exception("La nueva contraseña debe tener al menos 8 caracteres.");
        }

        TokenRecuperacion recuperado = recuperacionDao.buscarToken(token.trim());
        if (recuperado == null) {
            throw new Exception("El token de recuperación no es válido.");
        }
        if (!recuperado.esValido()) {
            throw new Exception("El token de recuperación expiró o ya fue utilizado.");
        }

        recuperacionDao.actualizarContrasena(recuperado.getIdUsuario(), HashUtil.sha256(nuevaContrasena));
        recuperacionDao.marcarTokenUsado(recuperado.getIdToken());
        return true;
    }

    @Override
    public boolean cambiarContrasena(int idUsuario, String nuevaContrasena) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El ID de usuario es inválido.");
        }
        if (nuevaContrasena == null || nuevaContrasena.length() < 8) {
            throw new Exception("La nueva contraseña debe tener al menos 8 caracteres.");
        }
        recuperacionDao.actualizarContrasena(idUsuario, HashUtil.sha256(nuevaContrasena));
        return true;
    }

    private int parseEntero(String valor, int porDefecto) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            return porDefecto;
        }
    }

    private String construirHtml(String nombres, String enlace, int minutos) {
        String nombre = (nombres != null && !nombres.isBlank()) ? nombres : "usuario";
        return """
               <div style="font-family:Arial,sans-serif;max-width:520px;margin:auto;
                           border:1px solid #eee;border-radius:8px;padding:24px;">
                 <h2 style="color:#2c7a3f;margin-top:0;">Shiligama's Minimarket</h2>
                 <p>Hola <strong>%s</strong>,</p>
                 <p>Recibimos una solicitud para restablecer la contraseña de tu cuenta.
                    Haz clic en el botón para crear una nueva contraseña:</p>
                 <p style="text-align:center;margin:28px 0;">
                   <a href="%s" style="background:#2c7a3f;color:#fff;text-decoration:none;
                      padding:12px 24px;border-radius:6px;display:inline-block;">
                      Restablecer contraseña</a>
                 </p>
                 <p style="font-size:13px;color:#666;">
                    Si el botón no funciona, copia y pega este enlace en tu navegador:<br>
                    <a href="%s">%s</a></p>
                 <p style="font-size:13px;color:#666;">
                    Este enlace caduca en %d minutos. Si no solicitaste el cambio,
                    ignora este correo; tu contraseña no se modificará.</p>
               </div>
               """.formatted(nombre, enlace, enlace, enlace, minutos);
    }
}
