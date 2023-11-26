package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;


public class SinApuros implements EstrategiaDeAviso {

    @Override
    public Notificacion avisar(Incidente incidente, Miembro miembro){
        Notificacion notificacion = new Notificacion(incidente,miembro);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setFueLeida(false);
        notificacion.setFueEnviada(false);
        return  notificacion;
    }
}
