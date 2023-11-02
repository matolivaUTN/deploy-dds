package server;


import com.twilio.rest.verify.v2.service.entity.Factor;
import controllers.*;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Servicio.Servicio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {

    public static void init(EntityManager entityManager) {

        Server.app().routes(() -> {


            /* ------------------------------------------------ SIGN UP / ELIMINAR CUENTA  ------------------------------------------------ */

            // Registro de miembros
            get("sign-up", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::create);

            // Guardado de miembros en la base de datos luego de su registro
            post("sign-up", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::save);


            // Eliminar miembro de la base de datos
            get("miembro/eliminar", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::deleteConfirmation);

            // Eliminar miembro de la base de datos
            post("miembro/eliminar", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::delete);


            /* ------------------------------------------------ LOG IN/LOG OUT ------------------------------------------------ */

            // Pantalla de inicio de sesion
            get("login", ((LogInController) FactoryController.controller("LogIn", entityManager))::show);

            // Validacion de los datos ingresados en el login
            post("login", ((LogInController) FactoryController.controller("LogIn", entityManager))::validate);


            // Pantalla de logout
            get("logout", ((LogInController) FactoryController.controller("LogIn", entityManager))::logout);


            /* ------------------------------------------------ COMUNIDADES ------------------------------------------------ */

            // Registro de comunidades
            get("comunidades/crear", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::create);

            // Guardado de comunidades en la base de datos luego de su registro
            post("comunidades", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::save);

            // Listado de comunidades de las que el miembro NO forma parte
            get("comunidades/unirse", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::noPerteneciente);


            // Unirse a una comunidad
            post("comunidades/unirse", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::agregarMiembro);


            // Listado de comunidades de las que el miembro SÍ forma parte
            get("comunidades/mis-comunidades", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::perteneciente);

            // Abandonar una comunidad
            post("comunidades/abandonar", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::borrarMiembro);


            get("comunidades/editar/{id}", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::edit);

            // Editar una comunidad
            post("comunidades/editar/{id}", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::update);

            // Eliminar una comunidad
            post("comunidades/eliminar", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::delete);


            /* ------------------------------------------------ HOME ------------------------------------------------ */
            get("home", ctx -> {
                ctx.render("home.hbs");
            });


            /* ------------------------------------------------ INCIDENTES ------------------------------------------------ */

            // Listado de incidentes
            get("incidentes", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::index);

            get("incidentes-de-comunidad", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::incidentesComunidad);

            // Apertura de incidentes
            get("incidentes/abrir", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::create);

            // Guardado de incidentes en la base de datos luego de crearlos
            post("incidentes", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::save);


            // Formulario de cierre de incidentes
            get("incidentes/cerrar", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::delete);

            get("obtenerIncidentesComunidad", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::obtenerIncidentes);


            get("obtenerEstablecimientosIncidente", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::obtenerEstablecimientosIncidente);





            // Guardado de incidentes en la base de datos luego de crearlos
            post("incidentes/cerrar", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::update);



            // Sugerencia de revision de incidentes







            /* ------------------------------------------------ CARGA MASIVA DE DATOS ------------------------------------------------ */

            // Guardado
            get("carga-prestadoras-organismos", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::showCargaPrestadorasYOrganismos);

            // Guardado de las entidades en la base de datos
            post("carga-prestadoras-organismos", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::saveCargaPrestadorasYOrganismos);




            // Guardado
            get("carga-entidades-establecimientos", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::showCargaEntidadesYEstablecimientos);

            // Guardado de las entidades en la base de datos
            //post("carga-entidades-establecimientos", ((EntidadesController) FactoryController.controller("Entidades"))::saveCargaEntidadesYEstablecimientos);


            /* ------------------------------------------------ ENTIDADES ------------------------------------------------ */

            get("entidades/crear", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::create);

            post("entidades", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::save);


            /* ------------------------------------------------ ESTABLECIMIENTOS ------------------------------------------------ */

            get("establecimientos/crear", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::create);

            post("establecimientos", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::save);



            /* ------------------------------------------------ RANKINGS ------------------------------------------------ */

            // Pantalla de rankings
            get("rankings", ((RankingsController) FactoryController.controller("Rankings", entityManager))::show);

            // Rankings segun el seleccionado
            get("rankings-incidentes", ((RankingsController) FactoryController.controller("Rankings", entityManager))::rankingSeleccionado);



            /* ------------------------------------------------ LOCALIZACION ------------------------------------------------ */

            get("obtener-municipios-provincia", ((LocalizacionController) FactoryController.controller("Localizacion", entityManager))::obtenerMunicipiosProvincia);

            get("obtener-departamentos-provincia", ((LocalizacionController) FactoryController.controller("Localizacion", entityManager))::obtenerDepartamentosProvincia);


        });
    }
}