package models.entities.Converter;

import models.entities.Notificaciones.AdapterAngusMail;
import models.entities.Notificaciones.AdapterTwilio;
import models.entities.Notificaciones.NotificadorEmail;
import models.entities.Notificaciones.NotificadorWhatsapp;
import models.entities.Notificaciones.Notificador;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MedioDeNotificacionConverter implements AttributeConverter<Notificador, String> {
  @Override
  public String convertToDatabaseColumn(Notificador medioDeNotificacion) {
    String medioEnBase = null;



    if (medioDeNotificacion != null) {

      if (medioDeNotificacion instanceof NotificadorWhatsapp) {
        medioEnBase = "wpp";
      } else if (medioDeNotificacion instanceof NotificadorEmail) {
        medioEnBase = "email";
      }
    }
    return medioEnBase;
  }

  @Override
  public Notificador convertToEntityAttribute(String s) {
    Notificador medio = null;

    if (s != null) {
      if ("wpp".equals(s)) {
        AdapterTwilio adapter = new AdapterTwilio();
        medio = new NotificadorWhatsapp(adapter);
      } else if ("email".equals(s)) {
        AdapterAngusMail adapter = new AdapterAngusMail();
        medio = new NotificadorEmail(adapter);
      }
    }
    return medio;
  }
}
