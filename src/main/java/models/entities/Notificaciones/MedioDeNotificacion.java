package models.entities.notificaciones;

import models.entities.comunidad.Miembro;

public interface MedioDeNotificacion {

   boolean enviarNotificacion(Miembro miembro, Notificacion notificacion);

}