package controllers;

import io.javalin.http.Context;
import models.entities.Comunidad.Miembro;
import models.repositories.RepositorioMiembros;
import org.eclipse.jetty.server.session.Session;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LogInController {
    private RepositorioMiembros repositorioMiembros;

    public LogInController(RepositorioMiembros repositorioMiembros) {
        this.repositorioMiembros = repositorioMiembros;
    }

    public void show(Context context) {
        // Mostramos la pantalla de login
        context.header("Cache-Control", "no-store");
        context.render("inicioSesion.hbs");
    }

    public void validate(Context context) throws IOException {
        String usuario = "";
        String contrasenia = "";


        if (!Objects.equals(context.formParam("loginForm"), "")) {
            usuario = context.formParam("usuario");
            contrasenia = context.formParam("contrasena");

        }

        // Buscamos el miembro que tenga ese usuario y contraseña en la base de datos
        Miembro miembro = this.repositorioMiembros.buscarPorUsuarioYContrasenia(usuario, contrasenia);

        if (miembro != null) {
            // Guardamos el id como un atributo de la sesión actual

            context.cookie("id_miembro", miembro.getIdMiembro().toString());

            System.out.println("Encontre una COOKIE con el id " +  context.cookie("id_miembro"));


            // En cualquier caso, redirigimos a home una vez que se completó el log in del usuario
            context.redirect("/home");

        } else {
            // Si el usuario y/o la contraseña no son correctos entonces le mostramos un mensaje de error
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Usuario y/o contraseña incorrectos");
            context.render("inicioSesion.hbs", model);
        }
    }


    public void logout(Context context) {
        // Eliminamos la cookie y lo redirigimos a la pestaña de login
        context.removeCookie("id_miembro");

        context.header("Cache-Control", "no-store");
        context.redirect("/login");
    }

}





