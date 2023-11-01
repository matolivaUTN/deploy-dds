package models.entities.Servicio;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EscaleraMecanica")
public class EscaleraMecanica extends Servicio {
  @Column(name = "tramoInicial")
  float tramoInicial;
  @Column(name = "tramoFinal")
  float tramoFinal;

  public void EscaleraMecanica(String nombre, float tramoInicial, float tramoFinal){
    this.nombre = nombre;
    this.tramoInicial = tramoInicial;
    this.tramoFinal = tramoFinal;
  }
}
