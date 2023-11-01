package models.entities.Servicio1.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.ServicioPublico.Establecimiento;

@Getter
@Setter
public class ComunidadServicio1 {
  private Double gradoConfianza;
  private List<Integer> miembros;
  private List<PrestacionDeServicioServicio1> prestacionesDeServicio;
  private Integer id;

  public ComunidadServicio1(){
    this.miembros = new ArrayList<>();
    this.prestacionesDeServicio = new ArrayList<>();
  }

}
