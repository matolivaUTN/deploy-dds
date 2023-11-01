package models.entities.georef.entities;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departamento")
@Getter
@Setter
public class Departamento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idDepartamento;

  @Column(name = "nombre")
  public String nombre;

  @ManyToOne
  @JoinColumn(name = "provincia", referencedColumnName = "id")
  private Provincia provincia;
}
