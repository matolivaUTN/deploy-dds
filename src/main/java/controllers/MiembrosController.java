package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Contrasenias.*;
import models.entities.Localizacion.Localizacion;
import models.entities.Notificaciones.*;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Prestadora;
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

            List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

            model.put("provincias", provincias);
            model.put("errorUsuario", "Ya existe una cuenta con ese email y/o nombre de usuario.");
            context.render("registro.hbs", model);
        }
        else {

            // Inicializamos los validadores de contraseña
            ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new Validador10kContraseniasMasUsadas(), new ValidadorCredencialPorDefecto(), new ValidadorLongitud(), new ValidadorComplejidad()));

            // Hacemos las validaciones correspondientes
            if(!validador.esValida(username, password)) {

                Map<String, Object> model = new HashMap<>();

                List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

                model.put("provincias", provincias);
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

        Map<String, Object> model = new HashMap<>();

        // Buscamos el miembro en la DB
        long miembroId = Long.parseLong(context.pathParam("id"));

        Miembro miembro = this.repositorioMiembros.buscarPorId(miembroId);


        System.out.println( "KAJSLKDJKLJADSK: " + miembro.getEstrategiaDeAvisoAsString());

        Localizacion localizacionMiembro = miembro.getLocalizacion();
        Provincia provinciaMiembro = localizacionMiembro.getProvincia();


        // Cargamos las localizaciones
        ArrayList<Provincia> provincias = new ArrayList<>(this.repositorioProvincias.buscarTodos());
        ArrayList<Municipio> municipios = new ArrayList<>(this.repositorioMunicipios.buscarMunicipiosDeProvincia(provinciaMiembro));
        ArrayList<Departamento> departamentos = new ArrayList<>(this.repositorioDepartamentos.buscarDepartamentosDeProvincia(provinciaMiembro));



        provincias.removeIf(provincia -> Objects.equals(provincia.getNombre(), provinciaMiembro.getNombre()));

        Municipio municipioMiembro = localizacionMiembro.getMunicipio();
        if(municipioMiembro != null) {
            municipios.removeIf(municipio -> Objects.equals(municipio.getNombre(), municipioMiembro.getNombre()));
        }

        Departamento departamentoMiembro  = localizacionMiembro.getDepartamento();
        if(departamentoMiembro != null) {
            departamentos.removeIf(departamento -> Objects.equals(departamento.getNombre(), departamentoMiembro.getNombre()));
        }


        model.put("provincias", provincias);
        model.put("municipios", municipios);
        model.put("departamentos", departamentos);


        // Esto es medio falopa pero lo hago para que en el front no muestre el medio/estrategia de notificacion ya seleccionado
        ArrayList<String> mediosDeNotificacion = new ArrayList<>();
        mediosDeNotificacion.add("WhatsApp");
        mediosDeNotificacion.add("Email");

        ArrayList<String> estrategiasAviso = new ArrayList<>();
        estrategiasAviso.add("Cuando suceden");
        estrategiasAviso.add("Sin apuros");



        // Hacemos esto porque la instancia que obtenemos del miembro no es la misma que esta en la lista
        estrategiasAviso.removeIf(estrategia -> Objects.equals(miembro.getEstrategiaDeAvisoAsString(), estrategia));
        mediosDeNotificacion.removeIf(medio -> Objects.equals(miembro.getNotificadorAsString(), medio));





        // Cargamos al miembro y a las estrategias/medios
        model.put("miembro", miembro);
        model.put("estrategiasAviso", estrategiasAviso);
        model.put("mediosDeNotificacion", mediosDeNotificacion);

        context.render("edicionUsuario.hbs", model);
    }

    @Override
    public void update(Context context) {

        long miembroId = Long.parseLong(context.pathParam("id"));
        Miembro miembro = this.repositorioMiembros.buscarPorId(miembroId);


        miembro.setEmail(context.formParam("email"));
        miembro.setTelefono(Long.valueOf(context.formParam("telefono")));

        // Convertimos de string al tipo de dato que se guarda en la base y se lo seteamos al miembro
        miembro.setEstrategiaDeAviso(this.convertirEstrategiaAviso(context.formParam("estrategia-aviso")));
        miembro.setNotificador(this.convertirMedioNotificacion(context.formParam("medio-notificacion")));



        long idProvincia = Long.parseLong(context.formParam("provincia"));
        long idDepartamento = Long.parseLong(context.formParam("departamento") == null ? "-1" : context.formParam("departamento"));
        long idMunicipio = Long.parseLong(context.formParam("municipio") == null ? "-1" : context.formParam("municipio"));


        // Buscamos en el repo de localizaciones si ya existe una localizacion que matchee la combinacion
        Provincia provinciaMiembro = this.repositorioProvincias.buscarPorId(idProvincia);
        Departamento departamentoMiembro = this.repositorioDepartamentos.buscarPorId(idDepartamento);
        Municipio municipioMiembro = this.repositorioMunicipios.buscarPorId(idMunicipio);

        // TODO: ojo que hay un error cuando en la edicion se mantiene la misma ubicacion
        Localizacion localizacion = this.repositorioLocalizaciones.buscarPorCombinacion(provinciaMiembro, municipioMiembro, departamentoMiembro);

        // En el caso de que no exista, la instanciamos
        if(localizacion == null) {
            localizacion = new Localizacion(provinciaMiembro, departamentoMiembro, municipioMiembro);
            this.repositorioLocalizaciones.agregar(localizacion);
        }


        // En cualquier caso le asignamos la localizacion al miembro

        miembro.setLocalizacion(localizacion);

        this.repositorioMiembros.actualizar(miembro);


        Map<String, Object> model = new HashMap<>();

        model.put("edicion_miembro", "edicion_miembro");
        context.render("confirmacion.hbs", model);

    }

    @Override
    public void delete(Context context) {
        // Eliminar miembro de la base de datos

        // Buscamos el miembro en la DB
        long miembroId = Long.parseLong(context.cookie("id_miembro"));
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
        context.consumeSessionAttribute("id_miembro");

        context.redirect("/logout");
    }

    public void deleteConfirmation(Context context) {
        context.render("deleteConfirmation.hbs");
    }


    private void asignarParametros(Miembro miembro, Context context) {
        // Con un formParam levantamos los parámetros de un formulario (tenemos que especificar el nombre del form)
        if(!Objects.equals(context.formParam("signUpForm"), "")) {


            long idProvincia = Long.parseLong(context.formParam("provincia"));
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
            miembro.setTelefono(Long.parseLong(context.formParam("telefono")));


            // Convertimos de string al tipo de dato que se guarda en la base y se lo seteamos al miembro
            miembro.setEstrategiaDeAviso(this.convertirEstrategiaAviso(context.formParam("estrategia-aviso")));
            miembro.setNotificador(this.convertirMedioNotificacion(context.formParam("medio-notificacion")));



            // Guardamos la localizacion
            miembro.setLocalizacion(localizacion);


            String contraseniaHasheada = BCrypt.hashpw(context.formParam("contrasena"), BCrypt.gensalt());


            miembro.setPassword(contraseniaHasheada);
            miembro.setPuntaje(5.0);

        }
    }

    private EstrategiaDeAviso convertirEstrategiaAviso(String estrategiaAviso) {

        if ("Sin apuros".equals(estrategiaAviso)) {
            return new SinApuros();
        }
        else if ("Cuando suceden".equals(estrategiaAviso)) {
            return new CuandoSuceden();
        } else {
            // Manejar otros casos si es necesario
            return null;
        }
    }

    private MedioDeNotificacion convertirMedioNotificacion(String medioNotificacion) {

        if ("WhatsApp".equals(medioNotificacion)) {
            AdapterTwilio adapter = new AdapterTwilio();
            return new NotificadorWhatsapp(adapter);
        }
        else if ("Email".equals(medioNotificacion)) {
            AdapterAngusMail adapter = new AdapterAngusMail();
            return new NotificadorEmail(adapter);
        } else {

            return null;
        }
    }


}