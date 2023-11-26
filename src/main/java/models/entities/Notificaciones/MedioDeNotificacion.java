package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;

public interface MedioDeNotificacion {

   boolean enviarNotificacion(Miembro miembro, String mensaje);

}