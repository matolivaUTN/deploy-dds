package models.entities.Servicio;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Banio") // Valor discriminador para Banio
public class Banio extends Servicio{

  public void Banio(String nombre){
    this.nombre = nombre;
  }

  @Override
  public void darInformacion() {

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
