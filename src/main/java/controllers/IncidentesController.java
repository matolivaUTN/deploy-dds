package controllers;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.entities.ServicioPublico.Prestadora;
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
    private RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios;
    private RepositorioEstablecimientos repositorioEstablecimientos;

    public IncidentesController(RepositorioIncidentes repositorioIncidentes, RepositorioMiembros repositorioMiembros, RepositorioComunidades repositorioComunidades, RepositorioEstadosPorComunidad repositorioEstadosPorComunidad, RepositorioEntidades repositorioEntidades, RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios, RepositorioEstablecimientos repositorioEstablecimientos) {
        this.repositorioIncidentes = repositorioIncidentes;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioEstadosPorComunidad = repositorioEstadosPorComunidad;
        this.repositorioEntidades = repositorioEntidades;
        this.repositorioPrestacionesDeServicios = repositorioPrestacionesDeServicios;
        this.repositorioEstablecimientos = repositorioEstablecimientos;

    }

    public void index(Context context) {
        //Listado de incidentes

        Map<String, Object> model = new HashMap<>();


        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));
        List<Comunidad> comunidades = miembro.getComunidadesDeLasQueFormaParte();
        model.put("comunidades", comunidades);
        context.render("listadoIncidentes.hbs", model);
    }

    public void incidentesComunidad(Context context) {

        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));
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

        Miembro creador = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));


        List<Comunidad> comunidadesCreador = creador.getComunidadesDeLasQueFormaParte();



        model.put("comunidades", comunidadesCreador);


        // Agregamos las entidades y prestaciones
        List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

        //TODO: Aca AJAX QUERY para condicionar las prestaciones de servicio en base al establecimiento seleccionado


        model.put("entidades", entidades);
        context.render("registroIncidente.hbs", model);
    }

    public void save(Context context) {
        // Guardamos un incidente en la base de datos

        // TODO: agregar la logica de cambiar el estado del servicio cuando se abre un incidente

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



        Map<String, Object> model = new HashMap<>();
        model.put("apertura_incidente", "apertura_incidente");
        context.render("confirmacion.hbs", model);
    }

    public void edit(Context context) {

    }


    public void obtenerPrestacionesEstablecimiento(Context context) {

        // ID establecimineto
        String valorCampo1 = context.queryParam("valor");

        Establecimiento establecimiento = this.repositorioEstablecimientos.buscarPorId(Long.parseLong(valorCampo1));

        System.out.println("EL ESTABLECIMIENTO ES: " + establecimiento.getNombre());

        // TODO: HACER ESTO PRIMERO
        // Buscamos las prestaciones que se den en ese establecimiento y QUE ESTEN DISPONIBLES (NO TIENE SENTIDO ABRIR INCIDENTE SOBRE ALGO QUE YA NO ANDA)
        List<PrestacionDeServicio> prestacionesDeEstablecimiento = establecimiento.getPrestaciones();

        // Filtramos por aquellas prestaciones que estan disponibles
        List<PrestacionDeServicio> prestacionesDeEstablecimientoDisponibles = new ArrayList<>();

        for(PrestacionDeServicio prestacion : prestacionesDeEstablecimiento) {
            if(prestacion.getEstaDisponible()) {
                prestacionesDeEstablecimientoDisponibles.add(prestacion);
            }
        }



        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<option value='' disabled selected>Seleccione la prestación afectada</option>");


        for (PrestacionDeServicio prestacion : prestacionesDeEstablecimientoDisponibles) {
            response.append("<option value='").append(prestacion.getIdPrestacion()).append("'>").append(prestacion.getServicio().getNombre()).append("</option>");
        }

        context.result(response.toString());
        context.contentType("text/html"); // Cambia el tipo de contenido según tus necesidades


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
        Miembro cerrador = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));



        // Buscamos el registro en el repositorio de estados por coumunidad y cambiamos el estado a cerrado
        EstadoPorComunidad incidenteACerrar = this.repositorioEstadosPorComunidad.buscarPorIdIncidente(idIncidenteACerrar);


        // Le cambiamos el estado a cerrado, agregamos la fecha y updateamos en la base de datos
        incidenteACerrar.setFechaDeCierre(LocalDateTime.now());
        incidenteACerrar.setEstaAierto(false);
        incidenteACerrar.setCerrador(cerrador);

        PrestacionDeServicio prestacionAfectada = incidenteACerrar.getIncidente().getPrestacionAfectada();

        prestacionAfectada.setEstaDisponible(true);



        // Cuando se cierra el incidente, la prestacion afectada vuelve a estar disponible
        this.repositorioPrestacionesDeServicios.actualizar(prestacionAfectada);


        // Tambien se elimina el incidente involucrado en la lista


        this.repositorioEstadosPorComunidad.actualizar(incidenteACerrar);


        Map<String, Object> model = new HashMap<>();
        model.put("cierre_incidente", "cierre_incidente");
        context.render("confirmacion.hbs", model);

    }

    public void delete(Context context) {

        // Mostramos el formulario de cierre de incidentes

        Map<String, Object> model = new HashMap<>();
        Miembro cerrador = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));
        List<Comunidad> comunidadesCerrador = cerrador.getComunidadesDeLasQueFormaParte();

        model.put("comunidades", comunidadesCerrador);
        context.render("cierreIncidente.hbs", model);


    }

    private void asignarParametros(Incidente incidente, Context context) {

        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("incidenteForm"), "")) {

            Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

            incidente.setDescripcion(context.formParam("descripcion"));
            incidente.setObservaciones(context.formParam("observaciones"));
            incidente.setMiembroCreador(miembro);
            incidente.setTiempoInicial(LocalDateTime.now());
            incidente.setEstados(new ArrayList<EstadoPorComunidad>());

            PrestacionDeServicio prestacionDeServicio = this.repositorioPrestacionesDeServicios.buscarPorId(Long.parseLong(context.formParam("prestacion-afectada")));


            // Cuando abrimos un incidente informamos que deja de estar disponible un servicio porque el mismo no funciona
            prestacionDeServicio.setEstaDisponible(false);
            this.repositorioPrestacionesDeServicios.actualizar(prestacionDeServicio);

            incidente.setPrestacionAfectada(prestacionDeServicio);
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

