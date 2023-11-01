package models.entities.Servicio2.entities;

import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.Servicio2.Servicio2Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculadorDePuntajes {
    public Double calcularPuntajeMiembro(Miembro miembro, List<Incidente> incidentes) throws IOException {
        List<IncidenteServicio2> incidentes2 = new ArrayList<>();
        for(Incidente incidente : incidentes){
            IncidenteServicio2 incidente2 = new IncidenteServicio2();
            incidente2.setFechaApertura(incidente.getTiempoInicial());
            incidente2.setIdCreador(Math.toIntExact(incidente.getMiembroCreador().getIdMiembro()));
            incidente2.setIdCerrador(null);
            incidente2.setFechaCierre(null);
            incidente2.setIdPrestacion(Math.toIntExact(incidente.getPrestacionAfectada().getIdPrestacion()));
            for(EstadoPorComunidad estado : incidente.getEstados()){ //si el miembro analizado no es quien lo cerro entonces no me importa la fecha de cierre
                if(estado.getCerrador() == miembro){
                    incidente2.setIdCerrador(Math.toIntExact(miembro.getIdMiembro()));
                    incidente2.setFechaCierre(estado.getFechaDeCierre());
                }
            }
            incidentes2.add(incidente2);
        }
        MiembroServicio2 miembro2 = new MiembroServicio2();
        miembro2.setIdMiembro(Math.toIntExact(miembro.getIdMiembro()));
        miembro2.setIncidentes(incidentes2);
        miembro2.setPuntaje(miembro.getPuntaje());
        Servicio2Retrofit servicio2 = Servicio2Retrofit.instancia();
        return servicio2.calcularPuntajeMiembro(miembro2);
    }
    public Double calcularPuntajeComunidad(Comunidad comunidad) throws IOException {
        ComunidadServicio2 comunidad2 = new ComunidadServicio2();
        for (Miembro miembro : comunidad.getMiembros()) {
            comunidad2.agregarPuntajeDeMiembro(miembro.getPuntaje());
        }
        Servicio2Retrofit servicio2 = Servicio2Retrofit.instancia();
        return servicio2.calcularPuntajeComunidad(comunidad2);
    }
}
