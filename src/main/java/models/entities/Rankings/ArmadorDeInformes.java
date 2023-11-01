package models.entities.Rankings;

import models.entities.ServicioPublico.Entidad;

import java.util.ArrayList;
import java.util.List;

public class ArmadorDeInformes {
    private AdapterCSVWriter csvWriter;

    public void armarInforme(Ranking ranking, List<Entidad> entidades){

        List<Entidad> entidadesOrdenadas = ranking.obtener(entidades);
        List<String> nombresEntidades = new ArrayList<>();
        for(Entidad entidad: entidadesOrdenadas){
            nombresEntidades.add(entidad.getNombre());
        }
        csvWriter.armarInformeDeRanking(nombresEntidades);
    }
}
