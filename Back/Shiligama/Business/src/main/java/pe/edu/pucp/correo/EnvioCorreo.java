package pe.edu.pucp.correo;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.List;
import java.util.Properties;

import pe.edu.pucp.config.Config;

/**
 * Servicio de envío de correos por SMTP (Gmail), basado en el enfoque del
 * proyecto de referencia SanchezLovers (Sirgep): un Singleton que configura
 * una {@link Session} autenticada y arma mensajes MIME en HTML.
 *
 * <p>Las credenciales se leen de {@code shiligama-config.properties} vía
 * {@link Config}:
 * <ul>
 *   <li>{@code correo.emailOrigen}   — cuenta Gmail remitente.</li>
 *   <li>{@code correo.password}      — contraseña de aplicación (App Password) de Gmail.</li>
 * </ul>
 * (En el proyecto de referencia la contraseña iba cifrada con ChaCha20/AES;
 * aquí se lee directamente para simplificar — colócala como App Password.)
 */
public class EnvioCorreo {

    private static EnvioCorreo instancia;

    private EnvioCorreo() { }

    public static synchronized EnvioCorreo getInstance() {
        if (instancia == null) instancia = new EnvioCorreo();
        return instancia;
    }

    public static synchronized void resetInstancia() {
        instancia = null;
    }

    /**
     * Envía un correo HTML a uno o más destinatarios.
     * Las credenciales se leen en CADA llamada desde Config para que un cambio
     * en shiligama-config.properties + reinicio de Tomcat sea suficiente.
     *
     * @return true si el correo se envió correctamente.
     * @throws Exception con el mensaje de error SMTP para que el WS lo propague.
     */
    public boolean enviarEmail(List<String> destinatariosCorreos, String asunto,
                               String contenidoHtml) throws Exception {
        if (destinatariosCorreos == null || destinatariosCorreos.isEmpty()) return false;

        // Leer credenciales frescas cada vez
        String emailOrigen   = Config.get("correo.emailOrigen", "");
        String passwordOrigen = Config.get("correo.password", "");
        String smtpHost      = Config.get("correo.smtp.host", "smtp.gmail.com");
        String smtpPort      = Config.get("correo.smtp.port", "587");

        if (emailOrigen.isBlank() || passwordOrigen.isBlank()) {
            throw new Exception(
                "Falta configurar correo.emailOrigen / correo.password en shiligama-config.properties.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host",             smtpHost);
        props.put("mail.smtp.ssl.trust",        smtpHost);
        props.put("mail.smtp.starttls.enable",  "true");
        props.put("mail.smtp.port",             smtpPort);
        props.put("mail.smtp.user",             emailOrigen);
        props.put("mail.smtp.ssl.protocols",    "TLSv1.2");
        props.put("mail.smtp.auth",             "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailOrigen, passwordOrigen);
            }
        };

        Session session = Session.getInstance(props, auth);
        // Activar debug SMTP en consola para ver el handshake completo
        session.setDebug(true);

        MimeMessage correo = new MimeMessage(session);
        correo.setFrom(new InternetAddress(emailOrigen));

        InternetAddress[] dests = destinatariosCorreos.stream()
                .map(d -> { try { return new InternetAddress(d); }
                            catch (Exception ex) { throw new RuntimeException(ex); } })
                .toArray(InternetAddress[]::new);
        // TO (no BCC) para evitar problemas con filtros anti-spam en pruebas
        correo.setRecipients(Message.RecipientType.TO, dests);
        correo.setSubject(asunto, "UTF-8");
        correo.setSentDate(new java.util.Date());

        MimeBodyPart cuerpoHtml = new MimeBodyPart();
        cuerpoHtml.setContent(contenidoHtml, "text/html; charset=utf-8");
        Multipart contenido = new MimeMultipart();
        contenido.addBodyPart(cuerpoHtml);
        correo.setContent(contenido);

        Transport.send(correo);
        System.out.println("[EnvioCorreo] Correo enviado a " + destinatariosCorreos);
        return true;
    }
}
