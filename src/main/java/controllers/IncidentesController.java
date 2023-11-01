package controllers;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.repositories.*;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

public class IncidentesController implements WithSimplePersistenceUnit {
    private RepositorioIncidentes repositorioIncidentes;
    private RepositorioMiembros repositorioMiembros;
    private RepositorioComunidades repositorioComunidades;
    private RepositorioEstadosPorComunidad repositorioEstadosPorComunidad;
    private RepositorioEntidades repositorioEntidades;


    public IncidentesController(RepositorioIncidentes repositorioIncidentes, RepositorioMiembros repositorioMiembros, RepositorioComunidades repositorioComunidades, RepositorioEstadosPorComunidad repositorioEstadosPorComunidad, RepositorioEntidades repositorioEntidades) {
        this.repositorioIncidentes = repositorioIncidentes;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioEstadosPorComunidad = repositorioEstadosPorComunidad;
        this.repositorioEntidades = repositorioEntidades;
    }

    public void index(Context context) {
        //Aca hay que mostrar los incidentes en general y hacer alguna especie de IF para permitir que los muestre por estado

        Map<String, Object> model = new HashMap<>();

        //TODO: Aca llamamos a la API de listado de servicios

        Miembro miembro = this.repositorioMiembros.buscarPorId(context.sessionAttribute("id_usuario"));
        List<Comunidad> comunidades = miembro.getComunidadesDeLasQueFormaParte();
        model.put("comunidades", comunidades);
        context.render("listadoIncidentes.hbs", model);
    }

    public void incidentesComunidad(Context context) {

        Miembro miembro = this.repositorioMiembros.buscarPorId(context.sessionAttribute("id_usuario"));
        List<Comunidad> comunidades = miembro.getComunidadesDeLasQueFormaParte();


        long idComunidad = Long.parseLong(context.queryParam("incidente-comunidad"));
        Comunidad unaComunidad = this.repositorioComunidades.buscarPorId(idComunidad);

        List<Incidente> incidentes = this.repositorioIncidentes.buscarTodos();
        List<IncidenteConEstado> incidentesConEstado = new ArrayList<>();

        for(Incidente incidente : incidentes) {
            EstadoPorComunidad unEstado = incidente.estadoDeComunidad(unaComunidad);
            if(unEstado != null) {
                IncidenteConEstado incidenteConEstado = new IncidenteConEstado();
                incidenteConEstado.setIncidente(incidente);
                incidenteConEstado.setEstado(unEstado);
                incidentesConEstado.add(incidenteConEstado);
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("comunidades", comunidades);
        model.put("incidentesConEstado", incidentesConEstado);

        model.put("hayQueMostrarIncidentes", true);
        context.render("listadoIncidentes.hbs", model);
    }

    public void show(Context context) {

    }

    public void create(Context context) {
        // Pantalla de creacion de incidentes

        Map<String, Object> model = new HashMap<>();
        Miembro creador = this.repositorioMiembros.buscarPorId(context.sessionAttribute("id_usuario"));


        List<Comunidad> comunidadesCreador = creador.getComunidadesDeLasQueFormaParte();

        model.put("comunidades", comunidadesCreador);


        // Agregamos las entidades
        List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

        model.put("entidades", entidades);


        context.render("registroIncidente.hbs", model);
    }

    public void save(Context context) {
        // Guardamos un incidente en la base de datos

        Incidente incidente = new Incidente();
        this.asignarParametros(incidente, context);

        EstadoPorComunidad estadoIncidente = new EstadoPorComunidad();


        Comunidad comunidad = this.repositorioComunidades.buscarPorId(Long.parseLong(context.formParam("comunidad-incidente")));



        estadoIncidente.setIncidente(incidente);
        estadoIncidente.setComunidad(comunidad);
        estadoIncidente.setEstaAbierto(true);

        incidente.agregarEstado(estadoIncidente);



        this.repositorioIncidentes.agregar(incidente);

        this.repositorioEstadosPorComunidad.agregar(estadoIncidente);


        context.status(HttpStatus.CREATED);


        //TODO: avisar a todas las comunidades de las que el miembro forma parte que se creó el incidente
        /*this.comunidadesDeLasQueFormaParte.forEach(unaComunidad -> {
            EstadoPorComunidad unEstado = new EstadoPorComunidad(unaComunidad);
            nuevoIncidente.agregarEstado(unEstado);
        });*/

        context.redirect("/incidentes");
    }

    public void edit(Context context) {

    }


    public void obtenerEstablecimientosIncidente(Context context) {

        // Id entidad
        String valorCampo1 = context.queryParam("valor");


        Entidad entidad = this.repositorioEntidades.buscarPorId(Long.parseLong(valorCampo1));

        // Buscamos los establecimientos que pertenezcan a esa entidad
        List<Establecimiento> establecimientos = entidad.getEstablecimientos();


        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<option value='' disabled selected>Seleccione el establecimiento</option>");


        for (Establecimiento establecimiento : establecimientos) {
            response.append("<option value='").append(establecimiento.getIdEstablecimiento()).append("'>").append(establecimiento.getNombre()).append("</option>");
        }

        context.result(response.toString());
        context.contentType("text/html"); // Cambia el tipo de contenido según tus necesidades


    }


    public void update(Context context) {

        //Cierre de incidentes -> no se borra, sino que se le cambia el estado

        // Buscamos el id del incidente a cerrar y el miembro que lo cierra
        long idIncidenteACerrar = Long.parseLong(context.formParam("incidente"));
        Miembro cerrador = this.repositorioMiembros.buscarPorId(context.sessionAttribute("id_usuario"));

        System.out.println("EL ID DEL INCIDENTE QUE HAY QUE CERRAR ES: " + idIncidenteACerrar);

        // Buscamos el registro en el repositorio de estados por coumunidad y cambiamos el estado a cerrado
        EstadoPorComunidad incidenteACerrar = this.repositorioEstadosPorComunidad.buscarPorIdIncidente(idIncidenteACerrar);


        // Le cambiamos el estado a cerrado, agregamos la fecha y updateamos en la base de datos
        incidenteACerrar.setFechaDeCierre(LocalDateTime.now());
        incidenteACerrar.setEstaAierto(false);
        incidenteACerrar.setCerrador(cerrador);

        this.repositorioEstadosPorComunidad.actualizar(incidenteACerrar);


        context.redirect("/home");

    }

    public void delete(Context context) {

        // Mostramos el formulario de cierre de incidentes

        Map<String, Object> model = new HashMap<>();
        Miembro cerrador = this.repositorioMiembros.buscarPorId(context.sessionAttribute("id_usuario"));
        List<Comunidad> comunidadesCerrador = cerrador.getComunidadesDeLasQueFormaParte();

        model.put("comunidades", comunidadesCerrador);
        context.render("cierreIncidente.hbs", model);


    }

    private void asignarParametros(Incidente incidente, Context context) {

        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("incidenteForm"), "")) {

            Miembro miembro = this.repositorioMiembros.buscarPorId(context.sessionAttribute("id_usuario"));

            incidente.setDescripcion(context.formParam("descripcion"));
            incidente.setObservaciones(context.formParam("observaciones"));
            incidente.setMiembroCreador(miembro);
            incidente.setTiempoInicial(LocalDateTime.now());
            incidente.setEstados(new ArrayList<EstadoPorComunidad>());

            //TODO: incidente.setPrestacionAfectada(context.formParam("prestacionAfectada"));

        }
    }

    public void obtenerIncidentes(Context context) {

        String valorCampo1 = context.queryParam("valor");

        System.out.println(valorCampo1);


        // Lógica para obtener las opciones para campo2 basadas en valorCampo1


        // Obtenemos la comunidad
        Comunidad comunidad = this.repositorioComunidades.buscarPorId(Long.parseLong(valorCampo1));


        // Buscamos los incidentes que pertenezcan a esa comunidad (TIENEN QUE ESTAR ABIERTOS)
        // Estaria bueno que esta logica la hagamos directamente en el repositorio de incidentes capaz

        List<Incidente> incidentes = this.repositorioIncidentes.buscarTodos();
        List<Incidente> incidentesComunidad = new ArrayList<>();

        for(Incidente incidente : incidentes) {
            EstadoPorComunidad unEstado = incidente.estadoDeComunidad(comunidad);
            if(unEstado != null && unEstado.getEstaAbierto()) {
                incidentesComunidad.add(incidente);
            }
        }

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<option value='' disabled selected>Seleccione el incidente que desea cerrar</option>");

        for (Incidente incidente : incidentesComunidad) {
            response.append("<option value='").append(incidente.getIdIncidente()).append("'>").append(incidente.getDescripcion()).append("</option>");
        }

        context.result(response.toString());
        context.contentType("text/html"); // Cambia el tipo de contenido según tus necesidades


    }
}

