package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.comunidad.Miembro;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;
import models.entities.servicio.*;
import models.entities.ServicioPublico.Establecimiento;
import models.repositories.*;
import models.entities.incidente.Incidente;


import java.util.*;

public class ServiciosController extends Controller  {

    private RepositorioMiembros repositorioMiembros;
    private RepositorioEstablecimientos repositorioEstablecimientos;
    private RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios;
    private RepositorioServicios repositorioServicios;

    public ServiciosController(RepositorioEstablecimientos repositorioEstablecimientos, RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios, RepositorioMiembros repositorioMiembros, RepositorioServicios repositorioServicios) {

        this.repositorioMiembros = repositorioMiembros;
        this.repositorioEstablecimientos = repositorioEstablecimientos;
        this.repositorioPrestacionesDeServicios = repositorioPrestacionesDeServicios;
        this.repositorioServicios = repositorioServicios;
    }

    public void create(Context context) {
        // Creacion de un servicio

        // Considero que cuando se crea un servicio, necesariamente se instancia tambien una prestacion del mismo
        // Lo que se le va a mostrar a los usuarios en realidad son las prestaciones, eso es en lo que estara interesado
        // Necesitamos cargar a la vista los tipos de servicio y los establecimientos
        // Ademas de esto, tenemos que ver de que tipo de servicio se trata, ya que en base a ello mostraremos cierta informacion extra (ESTO EN EL LISTADO DE SERVICIOS)

        List<Establecimiento> establecimientos = this.repositorioEstablecimientos.buscarTodos();

        Map<String, Object> model = new HashMap<>();
        model.put("establecimientos", establecimientos);
        cargarRolesAModel(context, model);
        context.render("creacionServicios.hbs", model);
    }


    public void mostrarCamposAdicionales(Context context) {

        String tipoServicio = context.queryParam("valor");

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();

        switch (tipoServicio) {
            case "Banio": {

                System.out.println(" ENTRE A BANIOOOO");

                // Mostramos el campo genero

                response.append("<div class=\"input-container\">\n");
                response.append("    <label for=\"genero\"><b>Genero</b></label>\n");
                response.append("    <input\n");
                response.append("            type=\"text\"\n");
                response.append("            placeholder=\"Ingrese el género\"\n");
                response.append("            name=\"genero\"\n");
                response.append("            id=\"genero\"\n");
                response.append("            required\n");
                response.append("    />\n");
                response.append("</div>");

                break;
            }

            case "MedioDeElevacion": {

                // Mostramos los campos tramo incial y final

                // Tramo inicial input container
                response.append("<div class=\"input-container\">\n");
                response.append("    <label for=\"tramo-inicial\"><b>Tramo inicial en mts</b></label>\n");
                response.append("    <input\n");
                response.append("            type=\"number\"\n");
                response.append("            placeholder=\"Ingrese el tramo inicial\"\n");
                response.append("            name=\"tramo-inicial\"\n");
                response.append("            id=\"tramo-inicial\"\n");
                response.append("            required\n");
                response.append("    />\n");
                response.append("</div>\n");

                // Tramo final input container
                response.append("<div class=\"input-container\">\n");
                response.append("    <label for=\"tramo-final\"><b>Tramo final en mts</b></label>\n");
                response.append("    <input\n");
                response.append("            type=\"number\"\n");
                response.append("            placeholder=\"Ingrese el tramo final\"\n");
                response.append("            name=\"tramo-final\"\n");
                response.append("            id=\"tramo-final\"\n");
                response.append("            required\n");
                response.append("    />\n");
                response.append("</div>");

                break;
            }

        }

        context.result(response.toString());
        context.contentType("text/html");
    }



    public void save(Context context) {
        // Guardado de una prestacion de servicio en la base de datos

        // Tenemos el tipo de servicio y el establecimiento en donde se presta

        // Con esta info, podemos obtener la entidad a la que pertenece y la prestadora (esto nos sirve para el listado)

        PrestacionDeServicio prestacion = new PrestacionDeServicio();
        this.asignarParametros(prestacion, context);
        this.repositorioPrestacionesDeServicios.agregar(prestacion);

        context.status(HttpStatus.CREATED);

        Map<String, Object> model = new HashMap<>();
        model.put("creacion_servicio", "creacion_servicio");
        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public void edit(Context context) {

        // Pantalla de edicion del servicio y su prestacion asociada

        long prestacionId = Long.parseLong(context.pathParam("id"));

        PrestacionDeServicio prestacion = this.repositorioPrestacionesDeServicios.buscarPorId(prestacionId);

        List<Establecimiento> establecimientos = this.repositorioEstablecimientos.buscarTodos();

        Map<String, Object> model = new HashMap<>();
        model.put("establecimientos", establecimientos);

        model.put("prestacion", prestacion);

        cargarRolesAModel(context, model);
        context.render("creacionServicios.hbs", model);
    }


    public void update(Context context) {

        //TODO: FALLA ESTO

        long prestacionId = Long.parseLong(context.pathParam("id"));
        PrestacionDeServicio prestacion = this.repositorioPrestacionesDeServicios.buscarPorId(prestacionId);

        System.out.println("AAAAAAAAAAAAAAAA" + prestacionId);

        long establecimientoId = Long.parseLong(context.formParam("establecimiento"));
        Establecimiento establecimiento = this.repositorioEstablecimientos.buscarPorId(establecimientoId);

        List<PrestacionDeServicio> prestacionesEstablecimiento = establecimiento.getPrestaciones();

        // Eliminamos la prestacion del establecimiento
        prestacionesEstablecimiento.removeIf(prestacionABorrar -> Objects.equals(prestacionABorrar.getIdPrestacion(), prestacion.getIdPrestacion()));


        String tipoServicio = context.formParam("tipo-servicio");
        Servicio servicio = FactoryServicio.servicio(tipoServicio, context, establecimiento);

        // Actualizamos la tabla
        this.repositorioServicios.actualizar(servicio);

        prestacion.setServicio(servicio);
        prestacion.setEstablecimiento(establecimiento);


        // Agregamos la prestacion modificada
        establecimiento.agregarPrestacion(prestacion);


        //this.repositorioPrestacionesDeServicios.a(prestacion);

        Map<String, Object> model = new HashMap<>();
        model.put("edicion_servicio", "edicion_servicio");
        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);


    }


    public void delete(Context context) {

    }





    public void listadoServicios(Context context) {

        // Listamos todos los servicios que el usuario NO marco como de interes

        // Buscamos al miembro en la DB
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));


        // Filtramos las prestaciones que no le interesan
        List<PrestacionDeServicio> prestacionesNoInteresantes = new ArrayList<>();

        List<PrestacionDeServicio> todasLasPrestaciones = this.repositorioPrestacionesDeServicios.buscarTodos();

        for(PrestacionDeServicio prestacion : todasLasPrestaciones) {
            if(!prestacion.getMiembrosInteresados().contains(miembro)) {
                prestacionesNoInteresantes.add(prestacion);
            }
        }

        // Renderizamos la vista
        Map<String, Object> model = new HashMap<>();
        model.put("prestaciones", prestacionesNoInteresantes);

        cargarRolesAModel(context, model);
        context.render("listadoServicios.hbs", model);
    }

    public void serviciosDeInteres(Context context) {

        // Listado de todos los servicios de interes

        // Buscamos al miembro en la DB
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

        // Filtramos
        List<PrestacionDeServicio> prestacionesDeInteres = new ArrayList<>();


        List<PrestacionDeServicio> todasLasPrestaciones = this.repositorioPrestacionesDeServicios.buscarTodos();

        for(PrestacionDeServicio prestacion : todasLasPrestaciones) {
            if(prestacion.getMiembrosInteresados().contains(miembro)) {
                prestacionesDeInteres.add(prestacion);
            }
        }

        // Mostramos las comunidades de las que el miembro forma parte
        Map<String, Object> model = new HashMap<>();
        model.put("prestaciones", prestacionesDeInteres);


        // Le ponemos true para que muestre solo de las que forma parte
        model.put("estaInteresado", true);

        cargarRolesAModel(context, model);
        context.render("listadoServicios.hbs", model);
    }

    public void marcarInteres(Context context) {

        // Agregamos un miembro nuevo a la lista de interesados en esa prestacion

        // Buscamos el miembro en la DB
        long id_miembro = Long.parseLong(context.cookie("id_miembro"));
        Miembro miembro = this.repositorioMiembros.buscarPorId(id_miembro);

        // Buscamos la prestacion de servicio en la DB
        long prestacionId = Long.parseLong(context.formParam("id"));
        PrestacionDeServicio prestacion = this.repositorioPrestacionesDeServicios.buscarPorId(prestacionId);


        // Agregamos el miembro interesado a la lista dentro de la prestacion de servicio

        prestacion.agregarMiembroInteresado(miembro);
        this.repositorioPrestacionesDeServicios.actualizar(prestacion);


        Map<String, Object> model = new HashMap<>();
        model.put("follow_servicio", "follow_servicio");

        model.put("prestacion", prestacion);

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public void eliminarInteres(Context context) {

        // Eliminar a un miembro de la lista de interesados

        // Buscamos al miembro en la DB
        long id_miembro = Long.parseLong(context.cookie("id_miembro"));
        Miembro miembro = this.repositorioMiembros.buscarPorId(id_miembro);

        // Buscamos la prestacion de servicio en la DB
        long prestacionId = Long.parseLong(context.formParam("id"));
        PrestacionDeServicio prestacion = this.repositorioPrestacionesDeServicios.buscarPorId(prestacionId);


        // Actualizamos las tablas de comunidad y de miembro
        prestacion.eliminarMiembroInteresado(miembro);


        this.repositorioPrestacionesDeServicios.actualizar(prestacion);


        Map<String, Object> model = new HashMap<>();
        model.put("unfollow_servicio", "unfollow_servicio");
        model.put("prestacion", prestacion);

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }



    private void asignarParametros(PrestacionDeServicio prestacion, Context context) {
        if(!Objects.equals(context.formParam("signUpForm"), "")) {

            // Aca instanciamos un servicio en base al string y los atributos recibidos en el form

            String tipoServicio = context.formParam("tipo-servicio");

            Establecimiento establecimiento = this.repositorioEstablecimientos.buscarPorId(Long.parseLong(context.formParam("establecimiento")));
            establecimiento.agregarPrestacion(prestacion);
            // TODO: fijarse si hay que actualizar el establecimiento en la BD

            Servicio servicio = FactoryServicio.servicio(tipoServicio, context, establecimiento);

            // Agregamos el servicio creado a la tabla de servicios
            this.repositorioServicios.agregar(servicio);

            prestacion.setServicio(servicio);
            prestacion.setEstablecimiento(establecimiento);

            // Seteamos el estado de la prestacion del servicio a "funcionando"
            prestacion.setEstaDisponible(true);

            // Inicializamos las listas
            prestacion.setMiembrosInteresados(new ArrayList<Miembro>());    // Pusimos los miembros interesados aca porque nos facilita el tema de las notificaciones
            prestacion.setIncidentes(new ArrayList<Incidente>());

            prestacion.setDeleted(false);
        }
    }
}