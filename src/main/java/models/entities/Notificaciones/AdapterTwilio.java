package models.entities.notificaciones;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import models.entities.comunidad.Miembro;

public class AdapterTwilio implements AdapterWhatsapp{
  @Override
  public boolean enviarWhatsapp(Miembro miembro, Notificacion notificacion) {
    // Inicializa el cliente de Twilio
    //Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    // Número de teléfono remitente de Twilio (debe ser un número de WhatsApp registrado en Twilio)
    String fromPhoneNumber = "whatsapp:+14155238886";

    // Número de teléfono destinatario (debe ser un número de WhatsApp válido)
    String toPhoneNumber = "whatsapp:+XXXXXXXXXXX";

    // Envía el mensaje de WhatsApp
    Message message = Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(fromPhoneNumber), notificacion.getContenido()).create();

    // Imprime el SID del mensaje enviado
    System.out.println("Mensaje de WhatsApp enviado con SID: " + message.getSid());

    return true;
  }
}
