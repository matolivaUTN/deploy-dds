package models.entities.notificaciones;

import models.entities.comunidad.Miembro;


public class NotificadorWhatsapp implements MedioDeNotificacion {
  private AdapterWhatsapp adapter;

  public NotificadorWhatsapp(AdapterWhatsapp adapter){
    this.adapter = adapter;
  }
  public NotificadorWhatsapp(){}

  @Override
  public boolean enviarNotificacion(Miembro miembro, Notificacion notificacion) {
    try{
      adapter.enviarWhatsapp(miembro, notificacion);
      return true;
    }
    catch(Exception e){
      return false;
    }

  }
}
