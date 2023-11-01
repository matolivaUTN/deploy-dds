package server.middlewares;

import io.javalin.config.JavalinConfig;
import server.exceptions.AccessDeniedException;
import io.javalin.http.Context;

import java.util.Arrays;

//MIDDLEWARE DEL ORTO
public class AuthMiddleware {

    // Este middleware por predeterminado engloba a todas las rutas de nuestro web server
    // Si queremos excluir algunas rutas de esto podemos hacer un array de strings con las rutas y chequear que la ruta no se encuentre en ninguno de ellos



    private String[] noLogInAllowedRoutes = new String[] {
            "/login",
            "/sign-up",
            "/obtener-departamentos-provincia",
            "/obtener-municipios-provincia"
    };

    public static void apply(JavalinConfig config) {
        // Acá es donde chequeamos tema de roles, permisos y sesiones
        config.accessManager(((handler, context, routeRoles) -> {

            if(context.sessionAttribute("id_usuario") == null && Arrays.stream(new AuthMiddleware().noLogInAllowedRoutes).noneMatch(ruta -> ruta.equals(context.path()))) {   // Excluimos a la rutas definidas mas arriba
                // Ocurre la excepcion de acceso denegado
                throw new AccessDeniedException();
            }
            else {

                if(context.sessionAttribute("id_usuario") != null && ! Arrays.stream(new AuthMiddleware().noLogInAllowedRoutes).noneMatch(ruta -> ruta.equals(context.path()))) {

                    // Quizas aca deberiamos hacer otra exception que te mande a home pero por ahora lo dejo asi
                    context.redirect("/home");
                }
                else {
                    handler.handle(context);
                }
            }
        }));
    }
}
