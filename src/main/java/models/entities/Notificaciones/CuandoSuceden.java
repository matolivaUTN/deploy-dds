package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


public class CuandoSuceden implements EstrategiaDeAviso {



    @Override
    public Notificacion avisar(Incidente incidente, Miembro miembro) {
        Notificacion notificacion = new Notificacion(incidente, miembro);
        notificacion.setFueEnviada(true);

        String mensaje = this.resumen(incidente);
        miembro.enviarNotificacion(mensaje);

        return notificacion;
    }

    private String resumen(Incidente incidente){
        return "Hubo un incidente llamado: " + incidente.getDescripcion();
    }
}
