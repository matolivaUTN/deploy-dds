package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.comunidad.Miembro;
import models.entities.comunidad.Comunidad;
import models.repositories.RepositorioComunidades;
import models.repositories.RepositorioIncidentes;
import models.repositories.RepositorioMiembros;
import models.repositories.RepositorioRoles;

import java.util.*;

public class ComunidadesController extends Controller {
    private RepositorioComunidades repositorioComunidades;
    private RepositorioMiembros repositorioMiembros;
    private RepositorioIncidentes repositorioIncidentes;
    private RepositorioRoles repositorioRoles;

    public ComunidadesController(RepositorioComunidades repositorioComunidades, RepositorioMiembros repositorioMiembros, RepositorioIncidentes repositorioIncidentes, RepositorioRoles repositorioRoles) {
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioIncidentes = repositorioIncidentes;
        this.repositorioRoles = repositorioRoles;
    }

    public void comunidadesQueNoSonDelMiembro(Context context) {
        // Listado de todas las comunidades de las que el miembro NO forma parte

        // Buscamos al miembro en la DB
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

        // Filtramos
        List<Comunidad> comunidadesDeLasQueNoFormaParte = new ArrayList<>();


        List<Comunidad> comunidades = this.repositorioComunidades.buscarTodos();
        for(Comunidad comu : comunidades) {
            if(!comu.getMiembros().contains(miembro)) {
                comunidadesDeLasQueNoFormaParte.add(comu);
            }
        }

        // Para mostrar solamente los botones que le corresponden al admin:
        List<ComunidadConBool> comunidadesConBool = new ArrayList<>();
        for(Comunidad comunidad : comunidadesDeLasQueNoFormaParte) {
            comunidadesConBool.add(new ComunidadConBool(comunidad, false));
        }


        // Mostramos las comunidades de las que el miembro no forma parte
        Map<String, Object> model = new HashMap<>();
        model.put("comunidades", comunidadesConBool);
        cargarRolesAModel(context, model);
        context.render("listadoComunidades.hbs", model);
    }

    public void comunidadesDeMiembro(Context context) {
        // Listado de todas las comunidades de las que el miembro SI forma parte

        // Buscamos al miembro en la DB
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

        // Filtramos
        List<Comunidad> comunidadesDeLasQueFormaParte = new ArrayList<>();


        List<Comunidad> comunidades = this.repositorioComunidades.buscarTodos();
        for(Comunidad comunidad : comunidades) {
            if(comunidad.getMiembros().contains(miembro)) {
                comunidadesDeLasQueFormaParte.add(comunidad);
            }
        }

        // Mostramos las comunidades de las que el miembro forma parte
        Map<String, Object> model = new HashMap<>();

        // Para mostrar solamente los botones que le corresponden al admin:
        List<ComunidadConBool> comunidadesConBool = new ArrayList<>();
        for(Comunidad comunidad : comunidadesDeLasQueFormaParte) {
            comunidadesConBool.add(new ComunidadConBool(comunidad, comunidad.esAdmin(miembro)));
        }

        model.put("comunidades", comunidadesConBool);

        // Le ponemos true para que muestre solo de las que forma parte
        model.put("formaParte", true);

        cargarRolesAModel(context, model);
        context.render("listadoComunidades.hbs", model);
    }

    public void create(Context context) {
        // Creacion de una comunidad
        Map<String, Object> model = new HashMap<>();

        cargarRolesAModel(context, model);
        context.render("registroComunidad.hbs", model);
    }

    public void save(Context context) {
        // Guardado de una comunidad en la base de datos
        String nombre = "";

        if(!Objects.equals(context.formParam("comunidadForm"), "")) {
            nombre = context.formParam("nombre");
        }

        // Validamos que la comunidad no este creada
        Comunidad unaComunidad = this.repositorioComunidades.buscarPorNombre(nombre);

        if(unaComunidad != null) {
            // Si encuentra a una comunidad con ese nombre, entonces le pedimos que ingrese otro nombre
            Map<String, Object> model = new HashMap<>();
            model.put("errorComunidad", "Ya existe una comunidad con ese nombre.");
            context.render("registroComunidad.hbs", model);
        }
        else {
            // Si es valida, proseguimos con el guardado de la comunidad en la base de datos y redirigimos a la pantalla de home

            Comunidad comunidad = new Comunidad();
            this.asignarParametros(comunidad, context);
            this.repositorioComunidades.agregar(comunidad);

            // Ahora debemos setear al miembro que creo la comunidad como admin de la misma y ademas le ponemos nuevo rol

            long id_creador = Long.parseLong(context.cookie("id_miembro"));

            Miembro miembro = this.repositorioMiembros.buscarPorId(id_creador);

            // Aca guardamos en la tabla de admins x comunidad

            comunidad.agregarAdmin(miembro);
            comunidad.agregarMiembros(miembro);
            miembro.agregarComunidad(comunidad);


            this.repositorioComunidades.agregar(comunidad);

            context.status(HttpStatus.CREATED);

            Map<String, Object> model = new HashMap<>();
            model.put("creacion_comunidad", "creacion_comunidad");

            cargarRolesAModel(context, model);
            context.render("confirmacion.hbs", model);
        }
    }

    public void edit(Context context) {
        // Buscamos la comunidad en la DB
        long comunidadId = Long.parseLong(context.pathParam("id"));
        Comunidad comunidad = this.repositorioComunidades.buscarPorId(comunidadId);

        long id_miembro = Long.parseLong(context.cookie("id_miembro"));
        Miembro miembro = repositorioMiembros.buscarPorId(id_miembro);

        Map<String, Object> model = new HashMap<>();
        model.put("comunidad", comunidad);

        cargarRolesAModel(context, model);
        if(comunidad.esAdmin(miembro)) {
            context.render("registroComunidad.hbs", model);
        }
        else {
            context.render("home.hbs", model);
        }
    }

    public void update(Context context) {
        //Edicion de comunidad

        long comunidadId = Long.parseLong(context.pathParam("id"));
        Comunidad comunidad = (Comunidad) this.repositorioComunidades.buscarPorId(comunidadId);

        comunidad.setNombre(context.formParam("nombre"));
        comunidad.setDescripcion(context.formParam("descripcion"));

        this.repositorioComunidades.actualizar(comunidad);

        Map<String, Object> model = new HashMap<>();
        model.put("edicion_comunidad", "edicion_comunidad");

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public void agregarMiembro(Context context) {
        // Agregamos un miembro nuevo a la comunidad

        // Buscamos el miembro en la DB
        long id_miembro = Long.parseLong(context.cookie("id_miembro"));
        Miembro miembro = this.repositorioMiembros.buscarPorId(id_miembro);

        System.out.println(miembro.getNombre());

        // Buscamos la comunidad en la DB
        long comunidadId = Long.parseLong(context.formParam("id"));
        Comunidad comunidad = this.repositorioComunidades.buscarPorId(comunidadId);


        // Agregamos la comunidad a la lista del miembro y el miembro a la lista de la comunidad
        // Aca guardamos en la tabla de miembros x comunidad (o sea agregamos al miembro a la lista de miembros de la comunidad)

        comunidad.agregarMiembros(miembro);
        miembro.agregarComunidad(comunidad);
        this.repositorioComunidades.actualizar(comunidad);
        this.repositorioMiembros.actualizar(miembro);

        Map<String, Object> model = new HashMap<>();
        model.put("union_comunidad", "union_comunidad");
        model.put("comunidad", comunidad);

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public void borrarMiembro(Context context) {

        // Eliminar a un miembro de una comunidad

        // Buscamos al miembro en la DB
        long id_miembro = Long.parseLong(context.cookie("id_miembro"));
        Miembro miembro = this.repositorioMiembros.buscarPorId(id_miembro);

        // Buscamos la comunidad en la DB
        long comunidadId = Long.parseLong(context.formParam("id"));
        Comunidad comunidad = this.repositorioComunidades.buscarPorId(comunidadId);


        // Actualizamos las tablas de comunidad y de miembro
        comunidad.eliminarMiembros(miembro);
        miembro.eliminarComunidad(comunidad);


        this.repositorioComunidades.actualizar(comunidad);
        this.repositorioMiembros.actualizar(miembro);


        Map<String, Object> model = new HashMap<>();
        model.put("abandono_comunidad", "abandono_comunidad");
        model.put("comunidad", comunidad);

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    public void delete(Context context) {
        // Eliminar la comunidad de la base de datos

        // Buscamos la comunidad en la DB
        long comunidadId = Long.parseLong(context.formParam("id"));
        Comunidad comunidad = this.repositorioComunidades.buscarPorId(comunidadId);

        // Eliminamos a los miembros de la comunidad

        List<Miembro> todosLosMiembros = new ArrayList<>();

        todosLosMiembros.addAll(comunidad.getMiembros());
        todosLosMiembros.addAll(comunidad.getAdmins());

        for(Miembro miembro : todosLosMiembros) {
            comunidad.eliminarMiembros(miembro);
            //miembro.eliminarComunidad(comunidad); //CHEQUEAR ESTO CASCADA
        }

        comunidad.setDeleted(true);
        this.repositorioComunidades.actualizar(comunidad);

        //context.redirect("/comunidades/mis-comunidades");

        Map<String, Object> model = new HashMap<>();
        model.put("eliminacion_comunidad", "eliminacion_comunidad");
        model.put("comunidad", comunidad);

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    private void asignarParametros(Comunidad comunidad, Context context) {
        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("signUpForm"), "")) {
            comunidad.setNombre(context.formParam("nombre"));
            comunidad.setDescripcion(context.formParam("descripcion"));
            comunidad.setMiembros(new ArrayList<>());
            comunidad.setAdmins(new ArrayList<>());
            comunidad.setPrestaciones(new ArrayList<>());
            comunidad.setPuntaje(0.0);
            comunidad.setDeleted(false);
        }
    }
}