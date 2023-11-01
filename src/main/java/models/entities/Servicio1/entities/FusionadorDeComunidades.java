package models.entities.Servicio1.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.Servicio1.Servicio1Retrofit;
import models.entities.Servicio2.Servicio2Retrofit;
import models.entities.Servicio2.entities.MiembroServicio2;

public class FusionadorDeComunidades {
  public RespuestaPropuestaFusion crearPropuestas(List<Comunidad> comunidades, List<List<Long>> propuestasAExcluir) throws IOException {
    List<ComunidadServicio1> comunidades1 = new ArrayList<>();
    for(Comunidad comunidad : comunidades){
      ComunidadServicio1 comunidad1 = new ComunidadServicio1();
      comunidad1.setId(Math.toIntExact(comunidad.getIdComunidad()));
      comunidad1.setGradoConfianza(Double.valueOf(comunidad.getPuntaje()));
      List<Integer> ids = new ArrayList<>();
      for(Miembro miembro : comunidad.getMiembros()){
        ids.add(Math.toIntExact(miembro.getIdMiembro()));
      }
      comunidad1.setMiembros(ids);
      List<PrestacionDeServicioServicio1> prestaciones1 = new ArrayList<>();
      for(PrestacionDeServicio prestacion : comunidad.getPrestaciones()){
        PrestacionDeServicioServicio1 prestacion1 = new PrestacionDeServicioServicio1();
        prestacion1.setId(Math.toIntExact(prestacion.getIdPrestacion()));
        prestacion1.setServicio(Math.toIntExact(prestacion.getServicio().getIdServicio()));
        prestacion1.setEstablecimiento(Math.toIntExact(prestacion.getServicio().getEstablecimiento().getIdEstablecimiento()));
        prestaciones1.add(prestacion1);
      }
      comunidad1.setPrestacionesDeServicio(prestaciones1);

      comunidades1.add(comunidad1);
    }
    RequestSugerenciasFusion requestSugerenciasFusion = new RequestSugerenciasFusion();
    requestSugerenciasFusion.setComunidadesAFusionar(comunidades1);
    requestSugerenciasFusion.setPropuestasAExcluir(propuestasAExcluir);

    Servicio1Retrofit servicio1 = Servicio1Retrofit.instancia();
    return servicio1.calcularFusiones(requestSugerenciasFusion);

  }

  public RespuestaFusionComunidades fusionarComunidades(RequestPropuestasComunidad requestPropuestasComunidad) throws IOException {
    Servicio1Retrofit servicio1 = Servicio1Retrofit.instancia();
    return servicio1.realizarFusiones(requestPropuestasComunidad);
  }
}
