package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;

import java.time.LocalDateTime;

public class SinApuros implements EstrategiaDeAviso {

    public Notificacion avisar(Incidente incidente, Miembro miemrbo){
        Notificacion notificacion = new Notificacion(incidente,miemrbo);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setFueLeida(false);
        notificacion.setFueEnviada(false);
        return  notificacion;
    }
}
