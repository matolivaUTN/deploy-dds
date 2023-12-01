package models.entities.ServicioCalculoGradoDeConfianza.entities;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MiembroServicioCalculoGradoDeConfianza {
    protected Double puntaje;
    protected List<IncidenteServicioCalculoGradoDeConfianza> incidentes;
    protected int idMiembro;

    public MiembroServicioCalculoGradoDeConfianza() {
        // Constructor sin argumentos requerido para Retrofit
    }

    @Override
    public String toString() {
        // Utiliza la biblioteca Gson para convertir el objeto en formato JSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
