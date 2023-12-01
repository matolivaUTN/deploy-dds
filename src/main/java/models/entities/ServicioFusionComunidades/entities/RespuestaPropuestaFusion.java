package models.entities.ServicioFusionComunidades.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaPropuestaFusion {
  private String mensaje;
  List<PropuestaServicioFusionComunidades> propuestas;

  public RespuestaPropuestaFusion(){
    this.propuestas = new ArrayList<>();
  }
}
