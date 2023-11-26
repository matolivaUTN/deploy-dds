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
import models.entities.ServicioPublico.Prestadora;
import models.entities.ServicioPublico.Entidad;

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


  // Un servicio pertenece a un establecimiento, ese establecimiento pertenece a una entidad y esa entidad a una prestadora
  @ManyToOne
  @JoinColumn(name = "idEstablecimiento")
  private Establecimiento establecimiento;


  // Podriamos guardarnos como columna la entidad y la prestadora del servicio pero mejor hacemos una consulta que haga JOIN


  public Servicio(String nombre) {
    this.nombre = nombre;
  }

  public Servicio() {

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
