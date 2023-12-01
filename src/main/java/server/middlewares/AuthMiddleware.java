package server.middlewares;

import io.javalin.config.JavalinConfig;
import javax.persistence.EntityManager;

import models.entities.roles.TipoRol;
import server.exceptions.AccessDeniedException;
import io.javalin.http.Context;

import java.util.Arrays;

public class AuthMiddleware {

    // Este middleware por predeterminado engloba a todas las rutas de nuestro web server
    // Si queremos excluir algunas rutas de esto podemos hacer un array de strings con las rutas y chequear que la ruta no se encuentre en ninguno de ellos



    private static String[] rutasSiNoEstaLogueado = new String[] {
            "/login",
            "/sign-up",
            "/obtener-departamentos-provincia",
            "/obtener-municipios-provincia"
    };

    private static String[] ajaxRoutes = new String[] {
            "/obtener-departamentos-provincia",
            "/obtener-municipios-provincia"
    };

    public static void apply(JavalinConfig config) {
        // Acá es donde chequeamos tema de roles, permisos y sesiones
        config.accessManager(((handler, context, routeRoles) -> {

            if(! estaLogueado(context) && ! esAlgunaRuta(context.path(), rutasSiNoEstaLogueado)) { // Excluimos a la rutas definidas mas arriba
                // Ocurre la excepcion de acceso denegado
                throw new AccessDeniedException();
            }
            else if(estaLogueado(context) &&
                    esAlgunaRuta(context.path(), rutasSiNoEstaLogueado) &&
                    ! esAlgunaRuta(context.path(), ajaxRoutes)
                ) { // Si está logueado y es un path para los que no están logueados, redirije a home para no tener que loguearse de nuevo.

                context.redirect("/home");
            }
            else {
                TipoRol userRole = getUserRoleType(context);

                if(routeRoles.isEmpty() || routeRoles.contains(userRole)) {
                    handler.handle(context);
                }
                else {
                    throw new AccessDeniedException();
                }
            }
        }));
    }

    public static Boolean estaLogueado(Context context) {
        return (context.cookie("id_prestadora") != null ||
                context.cookie("id_organismo") != null ||
                context.cookie("id_miembro") != null);
    }

    public static Boolean esAlgunaRuta(String path, String[] rutas) {
        return Arrays.stream(rutas).anyMatch(ruta -> ruta.equals(path));
    }

    private static TipoRol getUserRoleType(Context context) {
        // En esta implementacion nos guardamos el rol del usuario en la sesion y luego lo levantamos y chequeamos
        return context.cookie("tipo_rol") != null ?
                TipoRol.valueOf(context.cookie("tipo_rol")) : null;
    }
}
