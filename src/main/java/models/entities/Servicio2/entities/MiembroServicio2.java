package models.entities.Servicio2.entities;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@Getter
@Setter
public class MiembroServicio2 {
    protected Double puntaje;
    protected List<IncidenteServicio2> incidentes;
    protected int idMiembro;

    public MiembroServicio2() {
        // Constructor sin argumentos requerido para Retrofit
    }

    @Override
    public String toString() {
        // Utiliza la biblioteca Gson para convertir el objeto en formato JSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
