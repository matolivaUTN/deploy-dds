package models.entities.Servicio;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.entities.ServicioPublico.Establecimiento;

@Getter
@Setter
@Entity
@Table(name = "servicio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminador")
public abstract class Servicio {

  @Id
  @GeneratedValue
  private Long idServicio;
  @Column(name = "nombre")
  String nombre;

  @ManyToOne
  @JoinColumn(name = "idEstablecimiento")
  private Establecimiento establecimiento;

  // Resto de tus atributos y métodos...

  public void darInformacion() {
    // Implementación
  }

  public float cantidadIncidentes() {
    // Implementación
    return 0;
  }

  public float totalIncidentes() {
    // Implementación
    return 0;
  }
}
