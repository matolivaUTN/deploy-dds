package models.entities.notificaciones;

import models.entities.comunidad.Miembro;

public interface AdapterWhatsapp {
  public boolean enviarWhatsapp(Miembro miembro, Notificacion notificacion);
}
