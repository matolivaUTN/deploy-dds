package controllers;

import io.javalin.http.Context;
import models.entities.rankings.CriterioRanking;
import models.entities.rankings.FactoryCriterio;
import models.entities.rankings.PuntajeEntidad;
import models.entities.rankings.Ranking;
import models.entities.ServicioPublico.Entidad;
import models.repositories.RepositorioEntidades;
import models.repositories.RepositorioIncidentes;
import server.App;
import server.utils.ICrudViewsHandler;

import java.util.*;

public class RankingsController extends Controller implements ICrudViewsHandler {
    private RepositorioIncidentes repositorioIncidentes;
    private RepositorioEntidades repositorioEntidades;

    public RankingsController(RepositorioIncidentes repositorioIncidentes, RepositorioEntidades repositorioEntidades) {
        this.repositorioIncidentes = repositorioIncidentes;
        this.repositorioEntidades = repositorioEntidades;
    }

    @Override
    public void index(Context context) {
        // TODO
    }

    @Override
    public void show(Context context) {

        List<String> criterios = new ArrayList<>();
        criterios.add("Mayor cantidad de incidentes reportados en la semana");
        criterios.add("Mayor promedio de tiempo de cierre de incidentes");

        Map<String, Object> model = new HashMap<>();
        model.put("criterios", criterios);

        cargarRolesAModel(context, model);
        context.render("rankings.hbs", model);
    }

    public void rankingSeleccionado(Context context) {
        App.entityManager().clear();

        String rankingElegido = context.queryParam("ranking-incidente");

        CriterioRanking criterioElegido = FactoryCriterio.criterio(rankingElegido);

        Ranking ranking = new Ranking(criterioElegido);


        List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

        List<PuntajeEntidad> entidadesOrdenadas = ranking.obtener(entidades);


        List<String> criterios = new ArrayList<>();
        criterios.add("Mayor promedio de tiempo de cierre de incidentes");
        criterios.add("Mayor cantidad de incidentes reportados en la semana");

        Map<String, Object> model = new HashMap<>();
        model.put("criterios", criterios);
        model.put("entidadesConPuntaje", entidadesOrdenadas);

        cargarRolesAModel(context, model);
        context.render("rankings.hbs", model);
    }


    // TODO: persistir rankings para el armado de informes

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}