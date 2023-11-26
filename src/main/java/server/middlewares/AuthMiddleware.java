package server.middlewares;

import io.javalin.config.JavalinConfig;
import server.exceptions.AccessDeniedException;
import io.javalin.http.Context;

import java.util.Arrays;

//MIDDLEWARE DEL ORTOOO
public class AuthMiddleware {

    // Este middleware por predeterminado engloba a todas las rutas de nuestro web server
    // Si queremos excluir algunas rutas de esto podemos hacer un array de strings con las rutas y chequear que la ruta no se encuentre en ninguno de ellos



    private String[] noLogInAllowedRoutes = new String[] {
            "/login",
            "/sign-up",
            "/obtener-departamentos-provincia",
            "/obtener-municipios-provincia"
    };

    private String[] ajaxRoutes = new String[] {
            "/obtener-departamentos-provincia",
            "/obtener-municipios-provincia"
            //"/obtenerIncidentesComunidad"
            //"/obtenerEstablecimientosIncidente"
    };

    public static void apply(JavalinConfig config) {
        // Acá es donde chequeamos tema de roles, permisos y sesiones
        config.accessManager(((handler, context, routeRoles) -> {

            if(context.cookie("id_miembro") == null && Arrays.stream(new AuthMiddleware().noLogInAllowedRoutes).noneMatch(ruta -> ruta.equals(context.path()))) {   // Excluimos a la rutas definidas mas arriba
                // Ocurre la excepcion de acceso denegado
                throw new AccessDeniedException();
            }
            else {

                if(context.cookie("id_miembro") != null &&
                        !Arrays.stream(new AuthMiddleware().noLogInAllowedRoutes).noneMatch(ruta -> ruta.equals(context.path())) &&
                        !Arrays.stream(new AuthMiddleware().ajaxRoutes).anyMatch(ruta -> ruta.equals(context.path()))
                ) {

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
