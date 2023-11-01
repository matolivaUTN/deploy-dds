package models.entities.Rankings;

import models.entities.ServicioPublico.Entidad;

public interface CriterioRanking {
    public float evaluar(Entidad entidad);
}
