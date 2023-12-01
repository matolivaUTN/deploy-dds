package models.entities.notificaciones;

import models.entities.comunidad.Miembro;

public class NotificadorEmail implements MedioDeNotificacion {

  private AdapterEmail adapter;

  public NotificadorEmail(AdapterEmail adapter) {
    this.adapter = adapter;
  }

  @Override
  public boolean enviarNotificacion(Miembro miembro, Notificacion notificacion){
    try{
      adapter.enviarEmail(miembro, notificacion);
      return true;
    }
    catch (Exception e) {
      return false;
    }

  }
}
