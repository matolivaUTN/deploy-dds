package models.entities.ServicioCalculoGradoDeConfianza.entities;

import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;
import models.entities.ServicioCalculoGradoDeConfianza.ServicioCalculoGradoDeConfianzaRetrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalculadorDePuntajes {
    public Double calcularPuntajeMiembro(Miembro miembro, List<Incidente> incidentes) throws IOException {
        List<IncidenteServicioCalculoGradoDeConfianza> incidentes2 = new ArrayList<>();
        for(Incidente incidente : incidentes){
            IncidenteServicioCalculoGradoDeConfianza incidente2 = new IncidenteServicioCalculoGradoDeConfianza();
            incidente2.setFechaApertura(incidente.getFechaDeApertura());
            incidente2.setIdCreador(Math.toIntExact(incidente.getMiembroCreador().getIdMiembro()));
            incidente2.setIdPrestacion(Math.toIntExact(incidente.getPrestacionAfectada().getIdPrestacion()));
            /*
            incidente2.setIdCerrador(null);
            incidente2.setFechaCierre(null);
            for(EstadoPorComunidad estado : incidente.getEstados()) { //si el miembro analizado no es quien lo cerro entonces no me importa la fecha de cierre
                if(estado.getCerrador() == miembro){
                    incidente2.setIdCerrador(Math.toIntExact(miembro.getIdMiembro()));
                    incidente2.setFechaCierre(estado.getFechaDeCierre());
                }
            }*/
            incidente2.setIdCerrador(Math.toIntExact(miembro.getIdMiembro()));
            incidente2.setFechaCierre(incidente.getFechaDeCierre());
            incidentes2.add(incidente2);
        }

        MiembroServicioCalculoGradoDeConfianza miembro2 = new MiembroServicioCalculoGradoDeConfianza();
        miembro2.setIdMiembro(Math.toIntExact(miembro.getIdMiembro()));
        miembro2.setIncidentes(incidentes2);
        miembro2.setPuntaje(miembro.getPuntaje());
        ServicioCalculoGradoDeConfianzaRetrofit servicio2 = ServicioCalculoGradoDeConfianzaRetrofit.instancia();
        return servicio2.calcularPuntajeMiembro(miembro2);
    }

    public Double calcularPuntajeComunidad(Comunidad comunidad) throws IOException {
        ComunidadServicioCalculoGradoDeConfianza comunidad2 = new ComunidadServicioCalculoGradoDeConfianza();
        for (Miembro miembro : comunidad.getMiembros()) {
            comunidad2.agregarPuntajeDeMiembro(miembro.getPuntaje());
        }
        ServicioCalculoGradoDeConfianzaRetrofit servicio2 = ServicioCalculoGradoDeConfianzaRetrofit.instancia();
        return servicio2.calcularPuntajeComunidad(comunidad2);
    }
}
