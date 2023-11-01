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
            // Buscamos si existe una sesión donde se haya guardado el id del usuario previamente
            long session = getSessionByID(miembro.getIdMiembro());

            if(session != -1) {
                // Encontro una sesion en el sistema

                // Guardamos el id como un atributo de la sesión actual
                context.sessionAttribute("id_usuario", session);

                System.out.println("Encontre una sesion con el id " + context.sessionAttribute("id_usuario"));

            }
            else {

                // Si no encuentra una sesión, la creamos
                System.out.println("Voy a crear la sesion");

                createSessionFile(miembro, context);
                
            }

            // En cualquier caso, redirigimos a home una vez que se completó el log in del usuario
            context.redirect("/home");

        } else {
            // Si el usuario y/o la contraseña no son correctos entonces le mostramos un mensaje de error
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Usuario y/o contraseña incorrectos");
            context.render("inicioSesion.hbs", model);
        }
    }


    public static long getSessionByID(long numeroABuscar) {

        // Recorre el directorio de sesiones y si encuentra una sesion donde este guardado el id del usuario que hizo login, lo devuelve
        File carpeta = new File(System.getProperty("user.dir") + "\\src\\main\\java\\server\\session");

        if (carpeta.exists() && carpeta.isDirectory()) {

            File[] archivos = carpeta.listFiles();

            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    try {
                        String contenido = new String(Files.readAllBytes(archivo.toPath()));
                        if (contenido.contains("id_usuario=" + numeroABuscar)) {
                            return numeroABuscar;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return -1;
    }


    public void logout(Context context) {
        // Eliminamos el atributo de sesion y lo redirigimos a la pestaña de login
        context.consumeSessionAttribute("id_usuario");

        context.header("Cache-Control", "no-store");
        context.redirect("/login");
    }



    private void createSessionFile(Miembro miembro, Context context) throws IOException {

        // Guardamos el id del usuario como atributo de la sesión actual
        context.sessionAttribute("id_usuario", miembro.getIdMiembro());


        // Crea el archivo de sesión con el nombre único (puedes usar el formato que prefieras)
        String sessionFileName = System.getProperty("user.dir") + "\\src\\main\\java\\server\\session\\session" + miembro.getIdMiembro().toString() + ".session";
        FileWriter writer = new FileWriter(sessionFileName);
        writer.write("id_usuario=" + miembro.getIdMiembro());
        writer.close();
    }


}





