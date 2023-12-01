package models.entities.ServicioPublico;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import models.entities.comunidad.Miembro;
import models.entities.roles.Rol;

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

  @Column(name = "password")
  private String password;

  @OneToMany(mappedBy = "organismo", cascade = CascadeType.ALL)
  private List<Prestadora> prestadoras = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "idMiembroDesignado", referencedColumnName = "idMiembro")
  private Miembro designado;

  @Column(name = "deleted")
  private Boolean deleted;

  @ManyToOne
  @JoinColumn(name = "rol_id", referencedColumnName = "id")
  private Rol rol;

  public Organismo(){}
  public Organismo(String nuevoOrganismo, String newPassword) {
    nombre = nuevoOrganismo;
    password = newPassword;
    prestadoras = new ArrayList<>(); //inicializamos la lista en vacio
    deleted = false;
  }

  public void addPrestadora(Prestadora prestadora) {
    this.prestadoras.add(prestadora);
    prestadora.setOrganismo(this);
  }
}