package models.entities.rankings;

import lombok.Getter;
import lombok.Setter;
import models.entities.ServicioPublico.Entidad;

@Setter
@Getter
public class PuntajeEntidad {

    private Entidad entidad;
    private Float puntaje;


    public PuntajeEntidad(Entidad entidad, Float puntaje) {
        this.entidad = entidad;
        this.puntaje = puntaje;
    }
}
