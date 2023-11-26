package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;

public class NotificadorEmail implements MedioDeNotificacion {

  private AdapterEmail adapter;

  public NotificadorEmail(AdapterEmail adapter) {
    this.adapter = adapter;
  }
  @Override
  public boolean enviarNotificacion(Miembro miembro, String mensaje){
    try{
      adapter.enviarEmail(miembro, mensaje);
      return true;
    }
    catch (Exception e) {
      return false;
    }

  }
}
