package controllers;

import io.javalin.http.Context;
import models.entities.ServicioPublico.Organismo;
import models.entities.ServicioPublico.Prestadora;
import models.entities.comunidad.Miembro;
import models.repositories.RepositorioMiembros;
import models.repositories.RepositorioOrganismos;
import models.repositories.RepositorioPrestadoras;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LogInController extends Controller {
    private RepositorioMiembros repositorioMiembros;
    private RepositorioPrestadoras repoPrestadoras;
    private RepositorioOrganismos repositorioOrganismos;

    public LogInController(RepositorioMiembros repositorioMiembros, RepositorioPrestadoras repoPrestadoras, RepositorioOrganismos repositorioOrganismos) {
        this.repositorioMiembros = repositorioMiembros;
        this.repoPrestadoras = repoPrestadoras;
        this.repositorioOrganismos = repositorioOrganismos;
    }

    public void show(Context context) {
        // Mostramos la pantalla de login

        HashMap<String, Object> model = new HashMap<>();
        context.header("Cache-Control", "no-store");

        cargarRolesAModel(context, model);
        context.render("inicioSesion.hbs", model);
    }

    // Validaciones del log in
    public void validate(Context context) throws IOException {
        String usuario = "";
        String contrasenia = "";

        if (!Objects.equals(context.formParam("loginForm"), "")) {
            usuario = context.formParam("usuario");
            contrasenia = context.formParam("contrasena");
        }

        // Buscamos el miembro que tenga ese usuario y contraseña en la base de datos
        Miembro miembro = this.repositorioMiembros.buscarPorUsuarioYContrasenia(usuario, contrasenia);
        Prestadora prestadora = this.repoPrestadoras.buscarPorNombreYContrasenia(usuario, contrasenia);
        Organismo organismo = this.repositorioOrganismos.buscarPorNombreYContrasenia(usuario, contrasenia);

        if (miembro != null) {
            // Guardamos el id en la cookie
            context.cookie("id_miembro", miembro.getIdMiembro().toString());

            // Guardamos el rol del usuario en la cookie
            context.cookie("tipo_rol", miembro.getRol().getTipo().toString());

            // Redirigimos a home una vez que se completó el log in del usuario
            context.redirect("/home");
        }
        else if(prestadora != null) {
            // Guardamos el id en la cookie
            context.cookie("id_prestadora", prestadora.getIdPrestadora().toString());

            // Guardamos el rol del usuario en la cookie
            context.cookie("tipo_rol", prestadora.getRol().getTipo().toString());

            // Redirigimos a home una vez que se completó el log in del usuario
            context.redirect("/home");
        }
        else if(organismo != null) {
            // Guardamos el id en la cookie
            context.cookie("id_organismo", organismo.getIdOrganismo().toString());

            // Guardamos el rol del usuario en la cookie
            context.cookie("tipo_rol", organismo.getRol().getTipo().toString());

            // Redirigimos a home una vez que se completó el log in del usuario
            context.redirect("/home");
        }
        else {
            // Si el usuario y/o la contraseña no son correctos entonces le mostramos un mensaje de error
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Usuario y/o contraseña incorrectos");

            cargarRolesAModel(context, model);
            context.render("inicioSesion.hbs", model);
        }
    }

    public void logout(Context context) {
        // Eliminamos la cookie y lo redirigimos a la pestaña de login
        context.removeCookie("id_miembro");
        context.removeCookie("id_prestadora");
        context.removeCookie("id_organismo");

        context.header("Cache-Control", "no-store");
        context.redirect("/login");
    }
}