package models.entities.ServicioFusionComunidades.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComunidadServicioFusionComunidades {
  private Double gradoConfianza;
  private List<Integer> miembros;
  private List<PrestacionDeServicioServicioFusionComunidades> prestacionesDeServicio;
  private Integer id;

  public ComunidadServicioFusionComunidades(){
    this.miembros = new ArrayList<>();
    this.prestacionesDeServicio = new ArrayList<>();
  }

}
