package models.entities.notificaciones;

import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;


public class CuandoSuceden implements EstrategiaDeAviso {


    private String resumen(Incidente incidente){
        return "Hubo un incidente llamado: " + incidente.getDescripcion();
    }

    @Override
    public Notificacion avisar(Incidente incidente, Miembro miembro) {
        return null;
    }
}
