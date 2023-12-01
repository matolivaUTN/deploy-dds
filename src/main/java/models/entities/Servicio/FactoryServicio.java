package models.entities.servicio;

import io.javalin.http.Context;
import models.entities.ServicioPublico.Establecimiento;


public class FactoryServicio {

    public static Servicio servicio(String nombre, Context context, Establecimiento establecimiento) {
        Servicio servicio = null;
        String nombreServicio = context.formParam("nombre");

        switch (nombre) {
            case "Banio": {

                // Instanciamos una clase de tipo Banio y la devolvemos
                String genero = context.formParam("genero");
                servicio = new Banio(nombreServicio, genero, establecimiento);
                break;
            }

            case "MedioDeElevacion": {

                // Instanciamos una clase de tipo MedioDeElevacion y la devolvemos
                Float tramoInicial = Float.valueOf(context.formParam("tramo-inicial"));
                Float tramoFinal = Float.valueOf(context.formParam("tramo-final"));

                servicio = new MedioDeElevacion(nombreServicio, tramoInicial, tramoFinal, establecimiento);

                break;
            }
        }
        return servicio;
    }
}