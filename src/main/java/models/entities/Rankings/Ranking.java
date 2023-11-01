package models.entities.Rankings;

import models.entities.ServicioPublico.Entidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking {
    private CriterioRanking criterio;

    public Ranking(CriterioRanking criterio){
        this.criterio = criterio;
    }

    public List<Entidad> obtener(List<Entidad> entidades) {
        Comparator<Entidad> comparador = (entidad1, entidad2) -> Float.compare(this.criterio.evaluar(entidad2), this.criterio.evaluar(entidad1));

        Collections.sort(entidades, comparador);
        Collections.reverse(entidades); //Para que sea decreciente

        for(Entidad entidad: entidades) {
            System.out.println(entidad.getNombre());
        }

        return entidades;
    }
}
