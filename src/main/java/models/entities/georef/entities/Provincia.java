package models.entities.georef.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "provincia")
@Getter
@Setter
public class Provincia {
  @Id
  private Long id;

  @Column(name = "nombre")
  public String nombre;
}
