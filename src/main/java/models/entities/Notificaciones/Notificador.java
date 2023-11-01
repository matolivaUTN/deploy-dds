package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;

public interface Notificador {

  public boolean enviarNotificacion(Miembro miembro,String mensaje);
}