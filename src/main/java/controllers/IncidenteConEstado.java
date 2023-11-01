package controllers;

import lombok.Getter;
import lombok.Setter;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;

@Getter
@Setter
public class IncidenteConEstado {
    private Incidente incidente;
    private EstadoPorComunidad estado;
}
