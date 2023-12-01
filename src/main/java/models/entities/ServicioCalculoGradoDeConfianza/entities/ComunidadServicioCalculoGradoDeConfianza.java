package models.entities.ServicioCalculoGradoDeConfianza.entities;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComunidadServicioCalculoGradoDeConfianza {
    private List<Double> puntajesDeMiembros;

    public ComunidadServicioCalculoGradoDeConfianza() {
        this.puntajesDeMiembros = new ArrayList<>();
        // Constructor sin argumentos requerido para Retrofit
    }

    public void agregarPuntajeDeMiembro(Double puntos){
        this.puntajesDeMiembros.add(puntos);
    }

    @Override
    public String toString() {
        // Utiliza la biblioteca Gson para convertir el objeto en formato JSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
