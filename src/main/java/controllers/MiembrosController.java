package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Contrasenias.*;
import models.entities.Localizacion.Localizacion;
import models.entities.georef.entities.Provincia;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.repositories.*;

import org.mindrot.jbcrypt.BCrypt;
import server.utils.ICrudViewsHandler;



import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.awt.desktop.SystemEventListener;
import java.util.*;

public class MiembrosController implements ICrudViewsHandler, WithSimplePersistenceUnit {
    private RepositorioMiembros repositorioMiembros;
    private RepositorioProvincias repositorioProvincias;
    private RepositorioMunicipios repositorioMunicipios;
    private RepositorioDepartamentos repositorioDepartamentos;
    private RepositorioLocalizaciones repositorioLocalizaciones;

    public MiembrosController(RepositorioMiembros repositorioMiembros, RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios, RepositorioDepartamentos repositorioDepartamentos, RepositorioLocalizaciones repositorioLocalizaciones) {
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioDepartamentos = repositorioDepartamentos;
        this.repositorioLocalizaciones = repositorioLocalizaciones;
    }

    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();
        context.render("home.hbs", model);
    }


    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {
        // Creacion de un miembro -> registro en la plataforma

        Map<String, Object> model = new HashMap<>();

        List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

        model.put("provincias", provincias);

        context.render("registro.hbs", model);
    }

    @Override
    public void save(Context context) {

        String username = "";
        String password = "";
        String email = "";

        if(!Objects.equals(context.formParam("signUpForm"), "")) {
            username = context.formParam("usuario");
            password = context.formParam("contrasena");
            email = context.formParam("email");
        }

        // Validamos que el usuario no este creado
        Miembro miembroUsuario = this.repositorioMiembros.buscarPorUsuarioOMail(username, email);

        if(miembroUsuario != null) {
            // Si encuentra a un miembro con ese usuario, entonces le pedimos que ingrese otro usuario
            Map<String, Object> model = new HashMap<>();
            model.put("errorUsuario", "Ya existe una cuenta con ese email y/o nombre de usuario.");
            context.render("registro.hbs", model);
        }
        else {

            // Inicializamos los validadores de contraseña
            ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new Validador10kContraseniasMasUsadas(), new ValidadorCredencialPorDefecto(), new ValidadorLongitud(), new ValidadorComplejidad()));

            // Hacemos las validaciones correspondientes
            if(!validador.esValida(username, password)) {

                Map<String, Object> model = new HashMap<>();
                model.put("error", "La contraseña no cumple con los requerimientos.");
                context.render("registro.hbs", model);
            }
            else {
                // Si es valida, proseguimos con el guardado del miembro nuevo en la base de datos y redirigimos a la pantalla de login

                Miembro miembro = new Miembro();
                this.asignarParametros(miembro, context);
                this.repositorioMiembros.agregar(miembro);
                context.status(HttpStatus.CREATED);
                context.redirect("/login");
            }
        }
    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {
        // Eliminar miembro de la base de datos

        // Buscamos el miembro en la DB
        long miembroId = context.sessionAttribute("id_usuario");
        Miembro miembro = this.repositorioMiembros.buscarPorId(miembroId);

        // Se copia la lista porque sino hay errores de concurrencia (ya que se va eliminando mientras se itera sobre la misma lista)
        List<Comunidad> comunidades = new ArrayList<>(miembro.getComunidadesDeLasQueFormaParte());

        EntityTransaction tx = entityManager().getTransaction();
        tx.begin();
        for(Comunidad comunidad : comunidades) {
            miembro.eliminarComunidad(comunidad);
            comunidad.eliminarMiembros(miembro);
        }
        tx.commit();

        this.repositorioMiembros.eliminar(miembro);

        // Eliminamos el atributo de sesion y lo redirigimos a login
        context.consumeSessionAttribute("id_usuario");

        context.redirect("/logout");
    }

    public void deleteConfirmation(Context context) {
        context.render("deleteConfirmation.hbs");
    }


    private void asignarParametros(Miembro miembro, Context context) {
        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("signUpForm"), "")) {

            long idProvincia;
            if(context.formParam("provincia") == null){
                idProvincia = 10;
            }else{
                idProvincia = Long.parseLong(context.formParam("provincia"));
            }
            long idDepartamento = Long.parseLong(context.formParam("departamento") == null ? "-1" : context.formParam("departamento"));
            long idMunicipio = Long.parseLong(context.formParam("municipio") == null ? "-1" : context.formParam("municipio"));

            Provincia provincia = this.repositorioProvincias.buscarPorId(idProvincia);
            Departamento departamento = this.repositorioDepartamentos.buscarPorId(idDepartamento);
            Municipio municipio = this.repositorioMunicipios.buscarPorId(idMunicipio);

            Localizacion localizacion = new Localizacion(provincia, departamento, municipio);

            this.repositorioLocalizaciones.agregar(localizacion);

            





            miembro.setNombre(context.formParam("nombre"));
            miembro.setApellido(context.formParam("apellido"));
            miembro.setEmail(context.formParam("email"));
            miembro.setUsername(context.formParam("usuario"));
            miembro.setLocalizacion(localizacion);


            String contraseniaHasheada = BCrypt.hashpw(context.formParam("contrasena"), BCrypt.gensalt());


            miembro.setPassword(contraseniaHasheada);
            miembro.setPuntaje(5.0);

            //TODO -> Falta todo el tema de las notificaciones (deberiamos hacer una pantalla para que el usuario pueda editar todo lo de las notificaciones)
            //TODO -> Si el usuario eligio sin apuros, en esa ventana deberiamos darle la posibilidad de elegir horarios en los que les llegan las notis
            //miembro.setNotificador();
            //miembro.setEstrategiaDeAviso();

            //TODO -> Tenemos que ver bien el tema de la ubicacion, si el usuario la elige en el momento del registro
            //miembro.setLocalizacion();
        }
    }

}