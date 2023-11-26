package models.entities.Servicio;

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
@DiscriminatorValue("Banio") // Valor discriminador para Banio
public class Banio extends Servicio {

  @Column(name = "genero")
  String genero;

  public Banio(String nombre, String genero, Establecimiento establecimiento) {
    this.nombre = nombre;
    this.genero = genero;
    this.setEstablecimiento(establecimiento);
  }

  public Banio() {

  }

  @Transient
  public String getDiscriminatorValue() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  @Override
  public float cantidadIncidentes() {
    return 0;
  }

  @Override
  public float totalIncidentes() {
    return 0;
  }
}
