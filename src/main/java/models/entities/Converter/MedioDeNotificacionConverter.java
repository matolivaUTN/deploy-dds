package models.entities.converter;

import models.entities.notificaciones.AdapterAngusMail;
import models.entities.notificaciones.AdapterTwilio;
import models.entities.notificaciones.NotificadorEmail;
import models.entities.notificaciones.NotificadorWhatsapp;
import models.entities.notificaciones.MedioDeNotificacion;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MedioDeNotificacionConverter implements AttributeConverter<MedioDeNotificacion, String> {

  @Override
  public String convertToDatabaseColumn(MedioDeNotificacion medioDeNotificacion) {
    String medioEnBase = null;

    if (medioDeNotificacion != null) {

      if (medioDeNotificacion instanceof NotificadorWhatsapp) {
        medioEnBase = "WhatsApp";
      } else if (medioDeNotificacion instanceof NotificadorEmail) {
        medioEnBase = "Email";
      }
    }
    return medioEnBase;
  }

  @Override
  public MedioDeNotificacion convertToEntityAttribute(String s) {
    MedioDeNotificacion medio = null;

    if (s != null) {
      if ("WhatsApp".equals(s)) {
        AdapterTwilio adapter = new AdapterTwilio();
        medio = new NotificadorWhatsapp(adapter);
      }
      else if ("Email".equals(s)) {
        AdapterAngusMail adapter = new AdapterAngusMail();
        medio = new NotificadorEmail(adapter);
      }
    }
    return medio;
  }
}
