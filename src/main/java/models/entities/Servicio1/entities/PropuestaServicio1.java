package models.entities.Servicio1.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropuestaServicio1 {
  private List<ComunidadServicio1> comunidades;

  public PropuestaServicio1(){
    this.comunidades = new ArrayList<>();
  }
}
