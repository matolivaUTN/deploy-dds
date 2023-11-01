package models.entities.Servicio1.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSugerenciasFusion {
  private List<ComunidadServicio1> comunidadesAFusionar;
  private List<List<Long>> propuestasAExcluir;

  public RequestSugerenciasFusion(){
    this.comunidadesAFusionar = new ArrayList<>();
    this.propuestasAExcluir = new ArrayList<>();
  }
}
