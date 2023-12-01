package models.entities.ServicioFusionComunidades.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.servicio.PrestacionDeServicio;
import models.entities.ServicioFusionComunidades.ServicioFusionComunidadesRetrofit;

public class FusionadorDeComunidades {
  public RespuestaPropuestaFusion crearPropuestas(List<Comunidad> comunidades, List<List<Long>> propuestasAExcluir) throws IOException {
    List<ComunidadServicioFusionComunidades> comunidades1 = new ArrayList<>();
    for(Comunidad comunidad : comunidades){
      ComunidadServicioFusionComunidades comunidad1 = new ComunidadServicioFusionComunidades();
      comunidad1.setId(Math.toIntExact(comunidad.getIdComunidad()));
      comunidad1.setGradoConfianza(Double.valueOf(comunidad.getPuntaje()));
      List<Integer> ids = new ArrayList<>();
      for(Miembro miembro : comunidad.getMiembros()){
        ids.add(Math.toIntExact(miembro.getIdMiembro()));
      }
      comunidad1.setMiembros(ids);
      List<PrestacionDeServicioServicioFusionComunidades> prestaciones1 = new ArrayList<>();
      for(PrestacionDeServicio prestacion : comunidad.getPrestaciones()){
        PrestacionDeServicioServicioFusionComunidades prestacion1 = new PrestacionDeServicioServicioFusionComunidades();
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

    ServicioFusionComunidadesRetrofit servicio1 = ServicioFusionComunidadesRetrofit.instancia();
    return servicio1.calcularFusiones(requestSugerenciasFusion);

  }

  public RespuestaFusionComunidades fusionarComunidades(RequestPropuestasComunidad requestPropuestasComunidad) throws IOException {
    ServicioFusionComunidadesRetrofit servicio1 = ServicioFusionComunidadesRetrofit.instancia();
    return servicio1.realizarFusiones(requestPropuestasComunidad);
  }
}
