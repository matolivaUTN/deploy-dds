package models.entities.servicio;

import lombok.Getter;
import lombok.Setter;
import models.entities.ServicioPublico.Establecimiento;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Setter
@Getter
@Entity
@DiscriminatorValue("MedioDeElevacion")
public class MedioDeElevacion extends Servicio {

  @Column(name = "tramoInicial")
  float tramoInicial;             // Tramo en mts desde el acceso en la calle hasta el acceso al transporte
  @Column(name = "tramoFinal")
  float tramoFinal;               // Tramo desde el acceso hasta el anden


  public MedioDeElevacion(String nombre, float tramoInicial, float tramoFinal, Establecimiento establecimiento) {
    this.nombre = nombre;
    this.tramoInicial = tramoInicial;
    this.tramoFinal = tramoFinal;
    this.setEstablecimiento(establecimiento);
  }

  public MedioDeElevacion() {

  }

  @Transient
  public String getDiscriminatorValue() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();

  }

}
