package models.entities.rankings;

import models.entities.ServicioPublico.Entidad;

public class MayorCantidadDeIncidentesReportadosEnLaSemana implements CriterioRanking {
    public float evaluar(Entidad entidad) {
        return entidad.cantidadDeIncidentesReportadosEnLaSemana();
    }
}
