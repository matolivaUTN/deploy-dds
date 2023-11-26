package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.Comunidad.Miembro;
import models.entities.Comunidad.Comunidad;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.repositories.RepositorioComunidades;
import models.repositories.RepositorioIncidentes;
import models.repositories.RepositorioMiembros;

import javax.persistence.EntityTransaction;
import java.util.*;

public class ComunidadesController implements WithSimplePersistenceUnit {

    private RepositorioComunidades repositorioComunidades;
    private RepositorioMiembros repositorioMiembros;
    private RepositorioIncidentes repositorioIncidentes;

    public ComunidadesController(RepositorioComunidades repositorioComunidades, RepositorioMiembros repositorioMiembros, RepositorioIncidentes repositorioIncidentes) {
        this.repositorioComunidades = repositorioComunidades;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioIncidentes = repositorioIncidentes;
    }

    public void noPerteneciente(Context context) {
        // Listado de todas las comunidades de las que el miembro NO forma parte

        // Buscamos al miembro en la DB
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));




        // Filtramos
        List<Comunidad> comunidadesDeLasQueNoFormaParte = new ArrayList<>();


        //TODO: Esta bien tener esta logica de objetos aca o tenemos que usar las tablas de la base de datos?
        // Aca tenemos que hacerlo consistente con la base de datos

        List<Comunidad> comunidades = this.repositorioComunidades.buscarTodos();
        for(Comunidad comu : comunidades) {
            if(!comu.getMiembros().contains(miembro)) {
                comunidadesDeLasQueNoFormaParte.add(comu);
            }
        }


        // Mostramos las comunidades de las que el miembro no forma parte
        Map<String, Object> model = new HashMap<>();
        model.put("comunidades", comunidadesDeLasQueNoFormaParte);
        context.render("listadoComunidades.hbs", model);
    }


    public void perteneciente(Context context) {
        // Listado de todas las comunidades de las que el miembro SI forma parte

        // Buscamos al miembro en la DB
        Miembro miembro = this.repositorioMiembros.buscarPorId(Long.parseLong(context.cookie("id_miembro")));

        // Filtramos
        List<Comunidad> comunidadesDeLasQueFormaParte = new ArrayList<>();


        //TODO: ¿está bien tener esta logica de objetos aca o tenemos que usar las tablas de la base de datos?
        // Aca tenemos que hacerlo consistente con la base de datos

        List<Comunidad> comunidades = this.repositorioComunidades.buscarTodos();
        for(Comunidad comunidad : comunidades) {
            if(comunidad.getMiembros().contains(miembro)) {
                comunidadesDeLasQueFormaParte.add(comunidad);
            }
        }

        // Mostramos las comunidades de las que el miembro forma parte
        Map<String, Object> model = new HashMap<>();
        model.put("comunidades", comunidadesDeLasQueFormaParte);

        // Le ponemos true para que muestre solo de las que forma parte
        model.put("formaParte", true);
        context.render("listadoComunidades.hbs", model);
    }


    public void show(Context context) {

    }


    public void create(Context context) {
        // Creacion de una comunidad
        context.render("registroComunidad.hbs");
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

            // Ahora debemos setear al miembro que creo la comunidad como admin de la misma

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
            context.render("confirmacion.hbs", model);
        }
    }

    public void edit(Context context) {
        // Buscamos la comunidad en la DB
        long comunidadId = Long.parseLong(context.pathParam("id"));

        Comunidad comunidad = this.repositorioComunidades.buscarPorId(comunidadId);

        Map<String, Object> model = new HashMap<>();
        model.put("comunidad", comunidad);
        context.render("registroComunidad.hbs", model);
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
        context.render("confirmacion.hbs", model);
    }













    //TODO
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
            //miembro.eliminarComunidad(comunidad);
        }


        this.repositorioComunidades.eliminar(comunidad);

        context.redirect("/comunidades/mis-comunidades");

        Map<String, Object> model = new HashMap<>();
        model.put("eliminacion_comunidad", "eliminacion_comunidad");
        model.put("comunidad", comunidad);
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
        }
    }
}