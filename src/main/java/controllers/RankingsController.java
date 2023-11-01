package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Contrasenias.*;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.Rankings.ArmadorDeInformes;
import models.entities.Rankings.CriterioRanking;
import models.entities.Rankings.FactoryCriterio;
import models.entities.Rankings.Ranking;
import models.entities.ServicioPublico.Entidad;
import models.repositories.RepositorioEntidades;
import models.repositories.RepositorioIncidentes;
import models.repositories.RepositorioMiembros;
import server.utils.ICrudViewsHandler;

import java.util.*;

public class RankingsController implements ICrudViewsHandler {
    private RepositorioIncidentes repositorioIncidentes;
    private RepositorioEntidades repositorioEntidades;

    public RankingsController(RepositorioIncidentes repositorioIncidentes, RepositorioEntidades repositorioEntidades) {
        this.repositorioIncidentes = repositorioIncidentes;
        this.repositorioEntidades = repositorioEntidades;
    }

    @Override
    public void index(Context context) {

    }



    @Override
    public void show(Context context) {

        List<String> criterios = new ArrayList<>();
        criterios.add("Mayor cantidad de incidentes reportados en la semana");
        criterios.add("Mayor promedio de tiempo de cierre de incidentes");

        Map<String, Object> model = new HashMap<>();
        model.put("criterios", criterios);

        context.render("rankings.hbs", model);
    }


    public void rankingSeleccionado(Context context) {
        String rankingElegido = context.queryParam("ranking-incidente");

        CriterioRanking criterioElegido = FactoryCriterio.criterio(rankingElegido);

        Ranking ranking = new Ranking(criterioElegido);


        List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

        List<Entidad> entidadesOrdenadas = ranking.obtener(entidades);






        List<String> criterios = new ArrayList<>();
        criterios.add("Mayor cantidad de incidentes reportados en la semana");
        criterios.add("Mayor promedio de tiempo de cierre de incidentes");

        Map<String, Object> model = new HashMap<>();
        model.put("entidades", entidadesOrdenadas);
        model.put("criterios", criterios);

        context.render("rankings.hbs", model);
    }



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



    private void asignarParametros(Miembro miembro, Context context) {
        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("signUpForm"), "")) {


        }
    }


}