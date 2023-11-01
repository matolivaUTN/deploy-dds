package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;

public interface AdapterWhatsapp {
  public boolean enviarWhatsapp(Miembro miembro, String mensaje);
}
