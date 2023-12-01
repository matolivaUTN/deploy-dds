package models.entities.rankings;

import models.entities.ServicioPublico.Entidad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking {
    private CriterioRanking criterio;

    public Ranking(CriterioRanking criterio) {
        this.criterio = criterio;
    }

    public List<PuntajeEntidad> obtener(List<Entidad> entidades) {

        //Comparator<Entidad> comparador = (entidad1, entidad2) -> Float.compare(this.criterio.evaluar(entidad2), this.criterio.evaluar(entidad1));
        Comparator<Entidad> comparador = Comparator.comparing(entidad -> criterio.evaluar(entidad));

        entidades.sort(comparador);
        Collections.reverse(entidades); //Para que sea decreciente

        List<PuntajeEntidad> entidadesConPuntaje = new ArrayList<>();

        for(Entidad entidad: entidades) {
            System.out.println("NOMBRE: " + entidad.getNombre() + " - PUNTAJE: " + criterio.evaluar(entidad));
            entidadesConPuntaje.add(new PuntajeEntidad(entidad, criterio.evaluar(entidad)));
        }

        return entidadesConPuntaje;
    }
}


