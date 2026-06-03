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

    private final String emailOrigen;
    private final String passwordOrigen;
    private final Properties properties;
    private final Authenticator autenticador;

    private EnvioCorreo() {
        this.emailOrigen = Config.get("correo.emailOrigen", "");
        this.passwordOrigen = Config.get("correo.password", "");

        this.properties = new Properties();
        properties.put("mail.smtp.host", Config.get("correo.smtp.host", "smtp.gmail.com"));
        properties.put("mail.smtp.ssl.trust", Config.get("correo.smtp.host", "smtp.gmail.com"));
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", Config.get("correo.smtp.port", "587"));
        properties.setProperty("mail.smtp.user", emailOrigen);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");

        this.autenticador = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailOrigen, passwordOrigen);
            }
        };
    }

    public static synchronized EnvioCorreo getInstance() {
        if (instancia == null) {
            instancia = new EnvioCorreo();
        }
        return instancia;
    }

    /**
     * Envía un correo HTML a uno o más destinatarios (en copia oculta).
     *
     * @return true si el correo se envió correctamente.
     */
    public boolean enviarEmail(List<String> destinatariosCorreos, String asunto,
                               String contenidoHtml) {
        if (destinatariosCorreos == null || destinatariosCorreos.isEmpty()) {
            return false;
        }
        if (emailOrigen == null || emailOrigen.isBlank()
                || passwordOrigen == null || passwordOrigen.isBlank()) {
            System.err.println("[EnvioCorreo] Falta configurar correo.emailOrigen / correo.password.");
            return false;
        }
        try {
            Session session = Session.getInstance(properties, autenticador);
            MimeMessage correo = new MimeMessage(session);
            correo.setFrom(new InternetAddress(emailOrigen));

            InternetAddress[] destinatarios = new InternetAddress[destinatariosCorreos.size()];
            for (int i = 0; i < destinatariosCorreos.size(); i++) {
                destinatarios[i] = new InternetAddress(destinatariosCorreos.get(i));
            }
            correo.setRecipients(Message.RecipientType.BCC, destinatarios);
            correo.setSubject(asunto, "UTF-8");
            correo.setSentDate(new java.util.Date());

            MimeBodyPart cuerpoHtml = new MimeBodyPart();
            cuerpoHtml.setContent(contenidoHtml, "text/html; charset=utf-8");

            Multipart contenido = new MimeMultipart();
            contenido.addBodyPart(cuerpoHtml);
            correo.setContent(contenido);

            Transport.send(correo);
            System.out.println("[EnvioCorreo] Correo enviado correctamente a " + destinatariosCorreos);
            return true;
        } catch (Exception e) {
            System.err.println("[EnvioCorreo] Error al enviar el correo: " + e.getMessage());
            return false;
        }
    }
}
