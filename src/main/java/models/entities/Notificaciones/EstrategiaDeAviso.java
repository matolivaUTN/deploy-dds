package models.entities.notificaciones;

import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;


public interface EstrategiaDeAviso {

    public Notificacion avisar(Incidente incidente, Miembro miembro);

}
