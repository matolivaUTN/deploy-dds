package server;


import controllers.*;
import models.entities.roles.TipoRol;

import javax.persistence.EntityManager;

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


            // Edicion de perfil
            get("miembro/editar/{id}", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::edit);

            post("miembro/editar/{id}", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::update);


            // Edicion de horarios
            get("miembro/editar-notificaciones/{id}", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::editTime, TipoRol.MIEMBRO);

            post("miembro/editar-notificaciones/{id}", ((MiembrosController) FactoryController.controller("Miembros", entityManager))::updateTime, TipoRol.MIEMBRO);

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
            get("comunidades/crear", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::create, TipoRol.MIEMBRO);

            // Guardado de comunidades en la base de datos luego de su registro
            post("comunidades", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::save, TipoRol.MIEMBRO);

            // Listado de comunidades de las que el miembro NO forma parte
            get("comunidades/unirse", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::comunidadesQueNoSonDelMiembro, TipoRol.MIEMBRO);


            // Unirse a una comunidad
            post("comunidades/unirse", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::agregarMiembro, TipoRol.MIEMBRO);


            // Listado de comunidades de las que el miembro SÍ forma parte
            get("comunidades/mis-comunidades", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::comunidadesDeMiembro, TipoRol.MIEMBRO);

            // Abandonar una comunidad
            post("comunidades/abandonar", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::borrarMiembro, TipoRol.MIEMBRO);



            get("comunidades/editar/{id}", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::edit, TipoRol.MIEMBRO);

            // Editar una comunidad
            post("comunidades/editar/{id}", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::update, TipoRol.MIEMBRO);

            // Eliminar una comunidad
            post("comunidades/eliminar", ((ComunidadesController) FactoryController.controller("Comunidades", entityManager))::delete, TipoRol.MIEMBRO);


            /* ------------------------------------------------ HOME ------------------------------------------------ */

            get("home", ctx -> {
                Map<String, Object> model = new HashMap<>();
                model.put("rol", ctx.cookie("tipo_rol"));
                ctx.render("home.hbs", model);
            });


            /* ------------------------------------------------ INCIDENTES ------------------------------------------------ */

            // Listado de incidentes
            get("incidentes", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::index, TipoRol.MIEMBRO);

            get("incidentes-de-comunidad", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::incidentesComunidad, TipoRol.MIEMBRO);

            // Apertura de incidentes
            get("incidentes/abrir", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::create, TipoRol.MIEMBRO);

            // Guardado de incidentes en la base de datos luego de crearlos
            post("incidentes", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::save, TipoRol.MIEMBRO);


            // Formulario de cierre de incidentes
            get("incidentes/cerrar", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::delete, TipoRol.MIEMBRO);

            // Guardado de incidentes en la base de datos luego de crearlos
            post("incidentes/cerrar", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::update, TipoRol.MIEMBRO);

            get("obtenerIncidentesComunidad", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::obtenerIncidentes);

            get("obtenerEstablecimientosIncidente", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::obtenerEstablecimientosIncidente);


            get("obtenerPrestacionesEstablecimiento",((IncidentesController) FactoryController.controller("Incidentes", entityManager))::obtenerPrestacionesEstablecimiento);



            get("sugerencia-revision-incidente/{id}", ((IncidentesController) FactoryController.controller("Incidentes", entityManager))::sugerenciaRevisionIncidente, TipoRol.MIEMBRO);


            /* ------------------------------------------------ CARGA MASIVA DE DATOS ------------------------------------------------ */

            // Guardado
            get("carga-prestadoras-organismos", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::showCargaPrestadorasYOrganismos, TipoRol.ADMINISTRADOR);

            // Guardado de las entidades en la base de datos
            post("carga-prestadoras-organismos", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::saveCargaPrestadorasYOrganismos, TipoRol.ADMINISTRADOR);


            /* ------------------------------------------------ ORGANISMOS DE CONTROL Y ENTIDADES ------------------------------------------------ */

            // Listado / edicion / borrado de organismos de control y prestadoras

            get("organismos", ((EmpresasController) FactoryController.controller("Empresas", entityManager))::index, TipoRol.ADMINISTRADOR);

            post("organismos/eliminar", ((EmpresasController) FactoryController.controller("Empresas", entityManager))::delete, TipoRol.ADMINISTRADOR);











            /* ------------------------------------------------ ENTIDADES ------------------------------------------------ */

            get("entidades/crear", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::create, TipoRol.ADMINISTRADOR);

            post("entidades", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::save, TipoRol.ADMINISTRADOR);

            // Listado / edicion / borrado de entidades

            get("entidades", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::index, TipoRol.ADMINISTRADOR);

            get("entidades/editar/{id}", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::edit, TipoRol.ADMINISTRADOR);

            post("entidades/editar/{id}", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::update, TipoRol.ADMINISTRADOR);

            post("entidades/eliminar", ((EntidadesController) FactoryController.controller("Entidades", entityManager))::delete, TipoRol.ADMINISTRADOR);


            /* ------------------------------------------------ ESTABLECIMIENTOS ------------------------------------------------ */

            get("establecimientos/crear", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::create, TipoRol.ADMINISTRADOR);

            post("establecimientos", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::save, TipoRol.ADMINISTRADOR);


            // Listado / edicion / borrado de establecimientos

            get("establecimientos", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::index, TipoRol.ADMINISTRADOR);

            get("establecimientos/editar/{id}", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::edit, TipoRol.ADMINISTRADOR);

            post("establecimientos/editar/{id}", ((EstablecimientosController) FactoryController.controller("Establecimientos", entityManager))::update, TipoRol.ADMINISTRADOR);





            /* ------------------------------------------------ SERVICIOS Y PRESTACIONES DE SERVICIOS ------------------------------------------------ */

            // ADMINISTRADOR: ABM

            // Creacion de servicios
            get("servicios/crear", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::create, TipoRol.ADMINISTRADOR);

            // Guardado de servicios en la base de datos luego de su registro
            post("servicios", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::save, TipoRol.ADMINISTRADOR);



            // Edicion y borrado de servicios (y su prestacion asociada)

            get("servicios/editar/{id}", ((ServiciosController) FactoryController.controller("Servicios", entityManager)):: edit, TipoRol.ADMINISTRADOR);

            post("servicios/editar/{id}", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::update, TipoRol.ADMINISTRADOR);

            post("servicios/eliminar", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::delete, TipoRol.ADMINISTRADOR);





            // USUARIO

            // Listado de servicios que NO son de interes
            get("servicios", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::listadoServicios, TipoRol.ADMINISTRADOR, TipoRol.MIEMBRO);

            // Listado de servicios de interes
            get("servicios/mis-servicios", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::serviciosDeInteres, TipoRol.MIEMBRO);


            // Marcar servicio como de interes
            post("servicios/follow", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::marcarInteres, TipoRol.MIEMBRO);


            // Marcar servicio como que ya no interesa mas
            post("servicios/unfollow", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::eliminarInteres, TipoRol.MIEMBRO);



            get("mostrarCamposAdicionales", ((ServiciosController) FactoryController.controller("Servicios", entityManager))::mostrarCamposAdicionales);



            /* ------------------------------------------------ RANKINGS ------------------------------------------------ */

            // Pantalla de rankings
            get("rankings", ((RankingsController) FactoryController.controller("Rankings", entityManager))::show, TipoRol.MIEMBRO);

            // Rankings segun el seleccionado
            get("rankings-incidentes", ((RankingsController) FactoryController.controller("Rankings", entityManager))::rankingSeleccionado, TipoRol.MIEMBRO);


            /* ------------------------------------------------ LOCALIZACION ------------------------------------------------ */

            get("obtener-municipios-provincia", ((LocalizacionController) FactoryController.controller("Localizacion", entityManager))::obtenerMunicipiosProvincia);

            get("obtener-departamentos-provincia", ((LocalizacionController) FactoryController.controller("Localizacion", entityManager))::obtenerDepartamentosProvincia);

            get("obtener-provincia-entidad", ((LocalizacionController) FactoryController.controller("Localizacion", entityManager))::obtenerProvinciaEntidad);

            get("obtener-localizacion", ((LocalizacionController) FactoryController.controller("Localizacion", entityManager))::obtenerLocalizacion);


            /* ------------------------------------------------ DESIGNACION DE MIEMBRO PRESTADORA / ORGANISMO  ------------------------------------------------ */

            get("miembro/designar", ((EmpresasController) FactoryController.controller("Empresas", entityManager))::show, TipoRol.EMPRESA);

            post("miembro/designar", ((EmpresasController) FactoryController.controller("Empresas", entityManager))::designar, TipoRol.EMPRESA);








        });
    }
}