package server.handlers;

import io.javalin.Javalin;
import server.exceptions.AccessDeniedException;

public class AccessDeniedHandler implements IHandler {

    @Override
    public void setHandle(Javalin app) {
        app.exception(AccessDeniedException.class, (e, context) -> {
            // Si no tenemos acceso a una pagina, redirigimos a la pantalla de login
            context.redirect("/login");
        });
    }
}
