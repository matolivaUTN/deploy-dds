package models.entities.ServicioFusionComunidades.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSugerenciasFusion {
  private List<ComunidadServicioFusionComunidades> comunidadesAFusionar;
  private List<List<Long>> propuestasAExcluir;

  public RequestSugerenciasFusion(){
    this.comunidadesAFusionar = new ArrayList<>();
    this.propuestasAExcluir = new ArrayList<>();
  }
}
