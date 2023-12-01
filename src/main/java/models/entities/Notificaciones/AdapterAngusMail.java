package models.entities.notificaciones;

import models.entities.comunidad.Miembro;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AdapterAngusMail implements AdapterEmail {

  @Override
  public boolean enviarEmail(Miembro miembro, Notificacion notificacion){
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
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(miembro.getEmail()));
      message.setSubject(notificacion.getAsunto());
      message.setText(notificacion.getContenido());

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
