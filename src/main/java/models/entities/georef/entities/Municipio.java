package models.entities.georef.entities;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "municipio")
@Getter
@Setter
public class Municipio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idMunicipio;

  @Column(name = "nombre")
  public String nombre;

  @ManyToOne
  @JoinColumn(name = "provincia", referencedColumnName = "id")
  private Provincia provincia;
}
