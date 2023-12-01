package controllers;

import lombok.Getter;
import lombok.Setter;
import models.entities.comunidad.Comunidad;

@Setter
@Getter
public class ComunidadConBool {
    private Boolean esAdmin;
    private Comunidad comunidad;

    public ComunidadConBool(Comunidad comunidad, Boolean esAdmin) {
        this.comunidad = comunidad;
        this.esAdmin = esAdmin;
    }
}

