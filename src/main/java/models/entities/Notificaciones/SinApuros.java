package models.entities.notificaciones;

import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;


public class SinApuros implements EstrategiaDeAviso {


    @Override
    public Notificacion avisar(Incidente incidente, Miembro miembro) {
        return null;
    }
}
