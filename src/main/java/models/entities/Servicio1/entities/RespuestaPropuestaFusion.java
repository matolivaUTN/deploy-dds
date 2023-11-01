package models.entities.Servicio1.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaPropuestaFusion {
  private String mensaje;
  List<PropuestaServicio1> propuestas;

  public RespuestaPropuestaFusion(){
    this.propuestas = new ArrayList<>();
  }
}
