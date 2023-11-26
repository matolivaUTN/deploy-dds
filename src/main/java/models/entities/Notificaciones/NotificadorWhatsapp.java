package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;


public class NotificadorWhatsapp implements MedioDeNotificacion {
  private AdapterWhatsapp adapter;

  public NotificadorWhatsapp(AdapterWhatsapp adapter){
    this.adapter = adapter;
  }
  public NotificadorWhatsapp(){}

  @Override
  public boolean enviarNotificacion(Miembro miembro, String mensaje) {
    try{
      adapter.enviarWhatsapp(miembro, mensaje);
      return true;
    }
    catch(Exception e){
      return false;
    }

  }
}
