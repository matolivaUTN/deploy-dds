package models.entities.ServicioFusionComunidades.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropuestaServicioFusionComunidades {
  private List<ComunidadServicioFusionComunidades> comunidades;

  public PropuestaServicioFusionComunidades(){
    this.comunidades = new ArrayList<>();
  }
}
