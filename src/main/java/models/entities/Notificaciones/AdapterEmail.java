package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;

public interface AdapterEmail {
  public boolean enviarEmail(Miembro miembro, String mensaje);
}
