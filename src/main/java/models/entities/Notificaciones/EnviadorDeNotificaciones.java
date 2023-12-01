package models.entities.notificaciones;

import models.entities.comunidad.Miembro;

import java.util.List;

public class EnviadorDeNotificaciones {
    public void enviarNotificacion(Notificacion notificacion, List<Miembro> miembrosANotificar) {
        for(Miembro miembro : miembrosANotificar) {
            miembro.getNotificador().enviarNotificacion(miembro, notificacion);
        }
    }
}
