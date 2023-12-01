package models.entities.notificaciones;

import models.entities.comunidad.Miembro;

public interface AdapterEmail {
  public boolean enviarEmail(Miembro miembro, Notificacion notificacion);
}
