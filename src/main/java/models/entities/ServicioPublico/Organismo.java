package models.entities.ServicioPublico;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "organismo")
@Setter
@Getter
public class Organismo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idOrganismo")
  private Long idOrganismo;

  @Column(name = "nombre")
  private String nombre;

  @OneToMany(mappedBy = "organismo", cascade = CascadeType.ALL)
  private List<Prestadora> prestadoras;
  public Organismo(){}
  public Organismo(String nuevoOrganismo) {
    nombre = nuevoOrganismo;
    prestadoras = new ArrayList<>();      //inicializamos la lista en vacio
  }

  public void addPrestadora(Prestadora prestadora) {
    this.prestadoras.add(prestadora);
    prestadora.setOrganismo(this);
  }

}
