package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;

public interface EstrategiaDeAviso {
    public Notificacion avisar(Incidente incidente, Miembro miembro);

}
