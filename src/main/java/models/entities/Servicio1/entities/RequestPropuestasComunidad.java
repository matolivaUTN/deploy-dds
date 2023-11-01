package models.entities.Servicio1.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPropuestasComunidad {
  private List<PropuestaServicio1> propuestas;
  public RequestPropuestasComunidad(){
    this.propuestas = new ArrayList<>();
  }
}
