package models.entities.ServicioFusionComunidades.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPropuestasComunidad {
  private List<PropuestaServicioFusionComunidades> propuestas;
  public RequestPropuestasComunidad(){
    this.propuestas = new ArrayList<>();
  }
}
