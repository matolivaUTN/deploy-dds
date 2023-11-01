package models.entities.Rankings;

import models.entities.ServicioPublico.Entidad;

public class MayorPromedioDeTiempoDeCierreDeIncidentes implements CriterioRanking {
    public float evaluar(Entidad entidad) {
        return entidad.promedioDeTiempoDeCierreDeIncidentes();
    }
}