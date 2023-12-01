package controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.contrasenias.*;
import models.entities.localizacion.Localizacion;
import models.entities.notificaciones.*;
import models.entities.georef.entities.Provincia;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.roles.Rol;
import models.repositories.*;

import org.mindrot.jbcrypt.BCrypt;
import server.App;
import server.utils.ICrudViewsHandler;
import java.time.LocalTime;
import java.util.*;

public class MiembrosController extends Controller implements ICrudViewsHandler {
    private RepositorioMiembros repositorioMiembros;
    private RepositorioProvincias repositorioProvincias;
    private RepositorioMunicipios repositorioMunicipios;
    private RepositorioDepartamentos repositorioDepartamentos;
    private RepositorioLocalizaciones repositorioLocalizaciones;
    private RepositorioRoles repositorioRoles;

    public MiembrosController(RepositorioMiembros repositorioMiembros, RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios, RepositorioDepartamentos repositorioDepartamentos, RepositorioLocalizaciones repositorioLocalizaciones, RepositorioRoles repositorioRoles) {
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioDepartamentos = repositorioDepartamentos;
        this.repositorioLocalizaciones = repositorioLocalizaciones;
        this.repositorioRoles = repositorioRoles;
    }

    @Override
    public void index(Context context) {
        Map<String, Object> model = new HashMap<>();

        cargarRolesAModel(context, model);


        context.render("home.hbs", model);
    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {
        // Pantalla de creacion de usuarios

        Map<String, Object> model = new HashMap<>();

        List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

        model.put("provincias", provincias);
        cargarRolesAModel(context, model);
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

            // Si existe el miembro le pedimos que ingrese otro usuario
            Map<String, Object> model = new HashMap<>();

            List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

            model.put("provincias", provincias);
            model.put("errorUsuario", "Ya existe una cuenta con ese email y/o nombre de usuario.");
            cargarRolesAModel(context, model);
            context.render("registro.hbs", model);
        }
        else { // Si no existe o está borrado (lógicamente)

            // Inicializamos los validadores de contraseña
            ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new Validador10kContraseniasMasUsadas(), new ValidadorCredencialPorDefecto(), new ValidadorLongitud(), new ValidadorComplejidad()));

            // Hacemos las validaciones correspondientes
            if(!validador.esValida(username, password)) {

                Map<String, Object> model = new HashMap<>();

                List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

                model.put("provincias", provincias);
                model.put("error", "La contraseña no cumple con los requerimientos.");
                cargarRolesAModel(context, model);
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

        cargarRolesAModel(context, model);
        context.render("edicionUsuario.hbs", model);
    }

    public void editTime(Context context) {

        Map<String, Object> model = new HashMap<>();

        // Buscamos el miembro en la DB
        long miembroId = Long.parseLong(context.pathParam("id"));

        Miembro miembro = this.repositorioMiembros.buscarPorId(miembroId);


        System.out.println( "KAJSLKDJKLJADSK: " + miembro.getEstrategiaDeAvisoAsString());


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


        if(!miembro.getHorariosDePreferencia().isEmpty()) {
            model.put("tieneHorarios", true);
        }

        // Cargamos al miembro y a las estrategias/medios
        model.put("miembro", miembro);
        model.put("estrategiasAviso", estrategiasAviso);
        model.put("mediosDeNotificacion", mediosDeNotificacion);

        cargarRolesAModel(context, model);
        context.render("edicionNotificaciones.hbs", model);
    }

    @Override
    public void update(Context context) {

        App.entityManager().clear();

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

        cargarRolesAModel(context, model);
        model.put("edicion_miembro", "edicion_miembro");
        context.render("confirmacion.hbs", model);
    }

    public void updateTime(Context context) {

        long miembroId = Long.parseLong(context.pathParam("id"));
        Miembro miembro = this.repositorioMiembros.buscarPorId(miembroId);


        // Convertimos de string al tipo de dato que se guarda en la base y se lo seteamos al miembro
        miembro.setEstrategiaDeAviso(this.convertirEstrategiaAviso(context.formParam("estrategia-aviso")));
        miembro.setNotificador(this.convertirMedioNotificacion(context.formParam("medio-notificacion")));


        // Ahora, si corresponde, actualizamos los horarios de notificacion


        if(Objects.equals(context.formParam("estrategia-aviso"), "Sin apuros")) {

            LocalTime horario1 = LocalTime.parse(context.formParam("horario-aviso-0"));
            LocalTime horario2 = LocalTime.parse(context.formParam("horario-aviso-1"));
            LocalTime horario3 = LocalTime.parse(context.formParam("horario-aviso-2"));


            miembro.getHorariosDePreferencia().clear();
            miembro.agregarHorarios(horario1, horario2, horario3);
        }

        this.repositorioMiembros.actualizar(miembro);

        Map<String, Object> model = new HashMap<>();
        cargarRolesAModel(context, model);
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

        for(Comunidad comunidad : comunidades) {
            comunidad.eliminarMiembros(miembro);
        }

        miembro.setDeleted(true);
        this.repositorioMiembros.actualizar(miembro);

        // Eliminamos el atributo de sesion y lo redirigimos a login
        context.removeCookie("id_miembro");

        context.redirect("/login");
    }

    public void deleteConfirmation(Context context) {

        HashMap<String, Object> model = new HashMap<>();
        cargarRolesAModel(context, model);
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
            miembro.setDeleted(false);


            // TODO: ver en que casos hay que setear un rol distinto (por ejemplo cuando se crea una comunidad y se vuelve admin)

            // Por default le seteamos el rol de usuario comun
            Rol rolMiembro = this.repositorioRoles.buscarRolPorNombre("Miembro");
            miembro.setRol(rolMiembro);


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