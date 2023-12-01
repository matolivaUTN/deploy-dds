package controllers;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;
import models.entities.localizacion.Localizacion;
import models.entities.notificaciones.*;
import models.entities.servicio.PrestacionDeServicio;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.repositories.*;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IncidentesController extends Controller {
    private RepositorioIncidentes repositorioIncidentes;
    private RepositorioMiembros repositorioMiembros;
    private RepositorioComunidades repositorioComunidades;
    private RepositorioEntidades repositorioEntidades;
    private RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios;
    private RepositorioEstablecimientos repositorioEstablecimientos;
    private RepositorioNotificacion repositorioNotificaciones;
    private RepositorioNotificacionPorMiembro repositorioNotificacionesPorMiembro;

    public IncidentesController(RepositorioIncidentes repositorioIncidentes, RepositorioMiembros repositorioMiembros, RepositorioComunidades repositorioComunidades, RepositorioEntidades repositorioEntidades, RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios, RepositorioEstablecimientos repositorioEstablecimientos, RepositorioNotificacion repositorioNotificaciones, RepositorioNotificacionPorMiembro repositorioNotificacionesPorMiembro) {
        this.repositorioIncidentes = repositorioIncidentes;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioEntidades = repositorioEntidades;
        this.repositorioPrestacionesDeServicios = repositorioPrestacionesDeServicios;
        this.repositorioEstablecimientos = repositorioEstablecimientos;
        this.repositorioNotificaciones = repositorioNotificaciones;
        this.repositorioNotificacionesPorMiembro = repositorioNotificacionesPorMiembro;
    }

    public void index(Context context) {
        //Listado de incidentes

        Map<String, Object> model = new HashMap<>();

        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));
        List<Comunidad> comunidades = miembro.getComunidadesDeLasQueFormaParte();
        model.put("comunidades", comunidades);

        cargarRolesAModel(context, model);
        context.render("listadoIncidentes.hbs", model);
    }

    public void incidentesComunidad(Context context) {
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));
        List<Comunidad> comunidades = miembro.getComunidadesDeLasQueFormaParte();

        long idComunidad;
        Comunidad unaComunidad = null;

        // Nos traemos los parametros de la AJAX query
        String opcionValue = context.queryParam("opcion");
        String comunidadIncidenteValue = context.queryParam("comunidadIncidente");
        String tipoIncidenteValue = context.queryParam("tipoIncidente");

        if(!Objects.equals(comunidadIncidenteValue, "")) {
            idComunidad = Long.parseLong(context.queryParam("comunidadIncidente"));
            unaComunidad = this.repositorioComunidades.buscarPorId(idComunidad);
        }

        // Buscamos los incidentes con los filtros correspondientes

        List<Incidente> incidentes = this.repositorioIncidentes.buscarTodos();


        // Aplicamos los filtros (abierto, cerrado, todos)

        if(!Objects.equals(tipoIncidenteValue, "incidentes-todos")) {
            this.aplicarFiltro(tipoIncidenteValue, incidentes, unaComunidad);
        }

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<table class=\"content-table\">")
                .append("<tr>")
                .append("<th>Descripcion</th>")
                .append("<th>Observaciones</th>")
                .append("<th>Prestación afectada</th>")
                .append("<th>Fecha de alta</th>")
                .append("<th>Fecha de cierre</th>")
                .append("<th>Miembro creador</th>")
                .append("<th>Miembro cerrador</th>")
                .append("<th>Estado</th>")
                .append("</tr>")
                .append("<tbody>");

        for (Incidente incidente : incidentes) {
            response.append("<tr>")
                    .append("<td>").append(incidente.getDescripcion()).append("</td>")
                    .append("<td>").append(incidente.getObservaciones()).append("</td>")
                    .append("<td>").append(incidente.getPrestacionAfectada().getServicio().getNombre()).append("</td>")
                    .append("<td>").append(incidente.getFechaDeApertura().toLocalDate()).append("</td>")
                    .append("<td>").append(incidente.getFechaDeCierre() != null ? incidente.getFechaDeCierre().toLocalDate() : "-").append("</td>")
                    .append("<td>").append(incidente.getMiembroCreador().getNombre()).append("</td>")
                    .append("<td>").append(incidente.getCerrador() != null ? incidente.getCerrador().getNombre() : "-").append("</td>")
                    .append("<td>").append(Boolean.TRUE.equals(incidente.getEstaAbierto()) ? "Abierto" : "Cerrado").append("</td>")
                    .append("</tr>");
        }

        response.append("</tbody>")
                .append("</table>");

        context.result(response.toString());
        context.contentType("text/html");
    }

    private void aplicarFiltro(String filtro, List<Incidente> incidentes, Comunidad comunidad) {
        switch (filtro) {
            case "incidentes-cerrados": {
                incidentes.removeIf(incidente -> Objects.equals(incidente.getEstaAbierto(), true));
                break;
            }
            case "incidentes-abiertos": {
                incidentes.removeIf(incidente -> Objects.equals(incidente.getEstaAbierto(), false));
                break;
            }
        }
    }

    public void create(Context context) {
        // Pantalla de creacion de incidentes

        Map<String, Object> model = new HashMap<>();

        Miembro creador = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

        List<Comunidad> comunidadesCreador = creador.getComunidadesDeLasQueFormaParte();


        model.put("comunidades", comunidadesCreador);

        // Agregamos las entidades y prestaciones
        List<Entidad> entidades = this.repositorioEntidades.buscarTodos();


        model.put("entidades", entidades);

        cargarRolesAModel(context, model);
        context.render("registroIncidente.hbs", model);
    }

    public void save(Context context) {
        // Guardamos un incidente en la base de datos

        Incidente incidente = new Incidente();
        this.asignarParametros(incidente, context);

        Comunidad comunidad = this.repositorioComunidades.buscarPorId(Long.parseLong(context.formParam("comunidad-incidente")));

        this.repositorioIncidentes.agregar(incidente);


        context.status(HttpStatus.CREATED);


        EnviadorDeNotificaciones enviadorDeNotificaciones = new EnviadorDeNotificaciones();

        Notificacion notificacionSugerencia = new Notificacion();

        String contenido = this.armarSugerencia(incidente);
        notificacionSugerencia.setContenido(contenido);
        notificacionSugerencia.setAsunto("⚠️ ServiceNow - Sugerencia de revisión incidente");


        // Agarramos a todos los miembros de la base de datos
        List<Miembro> todosLosMiembros = this.repositorioMiembros.buscarTodos();


        // Filtramos por aquellos miembros que compartan la misma localización que el establecimiento donde se produjo el incidente
        Localizacion localizacionIncidente = incidente.getPrestacionAfectada().getEstablecimiento().getLocalizacion();
        List<Miembro> miembrosCercanos = todosLosMiembros.stream().filter(m -> Objects.equals(m.getLocalizacion().getIdLocalizacion(), localizacionIncidente.getIdLocalizacion())).toList();


        enviadorDeNotificaciones.enviarNotificacion(notificacionSugerencia, miembrosCercanos);


        // Ahora instanciamos una notificacion para enviar a los usuarios de la comunidad y miembros interesados en la prestacion afectada


        Notificacion notificacion = new Notificacion();

        String noti = this.armarNotificacion(incidente);
        notificacion.setContenido(noti);
        notificacion.setAsunto("⚠️ ServiceNow - Apertura de Incidente");
        notificacion.setNotificacionesPorMiembros(new ArrayList<>());


        // Primero se la mandamos a todos los miembros de la comunidad y miembros interesados que tengan la estrategia "cuando suceden"
        List<Miembro> miembrosComunidadCuandoSuceden = comunidad.getMiembros().stream().filter(miembro -> miembro.getEstrategiaDeAviso() instanceof CuandoSuceden).toList();
        List<Miembro> miembrosInteresadosCuandoSuceden = incidente.getPrestacionAfectada().getMiembrosInteresados().stream().filter(miembro -> miembro.getEstrategiaDeAviso() instanceof CuandoSuceden).toList();

        List<Miembro> miembrosCuandoSuceden = Stream.concat(
                miembrosComunidadCuandoSuceden.stream(),
                miembrosInteresadosCuandoSuceden.stream()
        ).collect(Collectors.toList());


        // Enviamos las notificaciones
        enviadorDeNotificaciones.enviarNotificacion(notificacion, miembrosCuandoSuceden);


        // Luego obtenemos la lista de miembros a los que hay que enviarle con el cron task
        List<Miembro> miembrosComunidadSinApuros = comunidad.getMiembros().stream().filter(miembro -> miembro.getEstrategiaDeAviso() instanceof SinApuros).toList();
        List<Miembro> miembrosInteresadosSinApuros = incidente.getPrestacionAfectada().getMiembrosInteresados().stream().filter(miembro -> miembro.getEstrategiaDeAviso() instanceof SinApuros).toList();

        List<Miembro> miembrosSinApuros = Stream.concat(
                miembrosComunidadSinApuros.stream(),
                miembrosInteresadosSinApuros.stream()
        ).collect(Collectors.toList());


        // Ahora tenemos que persistir la notificacion para que despues el Cron Task la agarre y la envie cada cierto tiempo (los miembros tienen seteados cada uno los horarios)
        this.repositorioNotificaciones.agregar(notificacion);


        // Ahora tenemos que guardar en el listado de notificaciones por miembro a todos los miembros que

        for(Miembro miembro : miembrosSinApuros) {
            NotificacionPorMiembro notificacionMiembro = new NotificacionPorMiembro();
            notificacionMiembro.setNotificacion(notificacion);
            notificacionMiembro.setMiembro(miembro);
            notificacionMiembro.setFueEnviada(false);

            notificacion.agregarNotificacionPorMiembro(notificacionMiembro);

            this.repositorioNotificacionesPorMiembro.agregar(notificacionMiembro);
        }


        Map<String, Object> model = new HashMap<>();
        model.put("apertura_incidente", "apertura_incidente");

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public String armarNotificacion(Incidente incidente) {
        return "Se ha abierto un incidente" +
                ". El miembro " + incidente.getMiembroCreador().getNombre() + " ha brindado la siguiente información: " + "\n\n" +
                "📝 Descripción: " + incidente.getDescripcion() + "\n" +
                "🔍 Observaciones: " + incidente.getObservaciones() + "\n" +
                "🏛️ Entidad: " + incidente.getPrestacionAfectada().getEstablecimiento().getEntidad().getNombre() + "\n" +
                "📍 Establecimiento: " + incidente.getPrestacionAfectada().getEstablecimiento().getNombre() + "\n" +
                "🚧 Prestación afectada: " + incidente.getPrestacionAfectada().getServicio().getNombre() + "\n\n" +
                "ServiceNow.";
    }

    public String armarSugerencia(Incidente incidente) {

        String mensaje = armarNotificacion(incidente);

        mensaje += "\n\nHemos detectado que te encuentras cerca de este incidente, te invitamos a revisar el estado del mismo. Para ello, haz click en el siguiente link:\n" +
                "http://localhost:8080/sugerencia-revision-incidente/" + incidente.getIdIncidente() + "\n\n";

        return mensaje;
    }

    public void obtenerPrestacionesEstablecimiento(Context context) {

        // ID establecimineto
        String valorCampo1 = context.queryParam("valor");

        Establecimiento establecimiento = this.repositorioEstablecimientos.buscarPorId(Long.parseLong(valorCampo1));

        System.out.println("EL ESTABLECIMIENTO ES: " + establecimiento.getNombre());


        // Buscamos las prestaciones que se den en ese establecimiento y QUE ESTEN DISPONIBLES (NO TIENE SENTIDO ABRIR INCIDENTE SOBRE ALGO QUE YA NO ANDA)
        List<PrestacionDeServicio> prestacionesDeEstablecimiento = establecimiento.getPrestaciones();

        // Filtramos por aquellas prestaciones que estan disponibles
        List<PrestacionDeServicio> prestacionesDeEstablecimientoDisponibles = prestacionesDeEstablecimiento.stream().filter(p -> Boolean.TRUE.equals(p.getEstaDisponible())).toList();

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
        Incidente incidenteACerrar = this.repositorioIncidentes.buscarPorId(idIncidenteACerrar);


        // Le cambiamos el estado a cerrado, agregamos la fecha y updateamos en la base de datos
        incidenteACerrar.cerrar(cerrador);
        repositorioNotificacionesPorMiembro.buscarPorIdIncidente(incidenteACerrar.getIdIncidente()).forEach(n -> n.setFueEnviada(true));




        PrestacionDeServicio prestacionAfectada = incidenteACerrar.getPrestacionAfectada();

        prestacionAfectada.setEstaDisponible(true);

        // Cuando se cierra el incidente, la prestacion afectada vuelve a estar disponible
        this.repositorioPrestacionesDeServicios.actualizar(prestacionAfectada);


        Map<String, Object> model = new HashMap<>();
        model.put("cierre_incidente", "cierre_incidente");

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public void delete(Context context) {

        // Mostramos el formulario de cierre de incidentes

        Map<String, Object> model = new HashMap<>();
        Miembro cerrador = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));
        List<Comunidad> comunidadesCerrador = cerrador.getComunidadesDeLasQueFormaParte();

        model.put("comunidades", comunidadesCerrador);

        cargarRolesAModel(context, model);
        context.render("cierreIncidente.hbs", model);
    }

    public void sugerenciaRevisionIncidente(Context context) {
        Incidente incidente = repositorioIncidentes.buscarPorId(Long.parseLong(context.pathParam("id")));

        Map<String, Object> model = new HashMap<>();
        model.put("incidente", incidente);

        cargarRolesAModel(context, model);
        context.render("sugerenciaRevisionIncidente.hbs", model);
    }

    private void asignarParametros(Incidente incidente, Context context) {

        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("incidenteForm"), "")) {

            Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

            incidente.setDescripcion(context.formParam("descripcion"));
            incidente.setObservaciones(context.formParam("observaciones"));
            incidente.setMiembroCreador(miembro);
            incidente.setFechaDeApertura(LocalDateTime.now());
            incidente.setEstaAbierto(true);
            incidente.setDeleted(false);

            PrestacionDeServicio prestacionDeServicio = this.repositorioPrestacionesDeServicios.buscarPorId(Long.parseLong(context.formParam("prestacion-afectada")));


            // Cuando abrimos un incidente informamos que deja de estar disponible un servicio porque el mismo no funciona
            prestacionDeServicio.setEstaDisponible(false);
            this.repositorioPrestacionesDeServicios.actualizar(prestacionDeServicio);

            incidente.setPrestacionAfectada(prestacionDeServicio);
        }
    }

    public void obtenerIncidentes(Context context) {
        String valorCampo1 = context.queryParam("valor");

        // Obtenemos la comunidad
        Comunidad comunidad = this.repositorioComunidades.buscarPorId(Long.parseLong(valorCampo1));


        // Buscamos los incidentes que pertenezcan a esa comunidad (TIENEN QUE ESTAR ABIERTOS)
        // Estaria bueno que esta logica la hagamos directamente en el repositorio de incidentes capaz

        List<Incidente> incidentesComunidad = this.repositorioIncidentes.buscarTodos();

        List<Incidente> incidentesAbiertos = incidentesComunidad.stream().filter(incidente -> Boolean.TRUE.equals(incidente.getEstaAbierto())).toList();

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<option value='' disabled selected>Seleccione el incidente que desea cerrar</option>");

        for (Incidente incidente : incidentesAbiertos) {
            response.append("<option value='").append(incidente.getIdIncidente()).append("'>").append(incidente.getDescripcion()).append("</option>");
        }

        context.result(response.toString());
        context.contentType("text/html"); // Cambia el tipo de contenido según tus necesidades
    }
}