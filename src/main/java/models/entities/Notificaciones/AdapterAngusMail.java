package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AdapterAngusMail implements AdapterEmail{
  @Override
  public boolean enviarEmail(Miembro miembro, String mensaje){
    final String username = "grupo14diseno@outlook.com";
    final String password = "grupo142023";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.office365.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("grupo14diseno@outlook.com"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("grupo14diseno@outlook.com"));
      message.setSubject("Correo de prueba");
      message.setText(mensaje);

      Transport.send(message);
      System.out.println("Correo electrónico enviado correctamente.");
      return true;
    } catch (MessagingException e) {
      e.printStackTrace();
      System.out.println("Error al enviar el correo electrónico.");
      return false;
    }

  }
}
