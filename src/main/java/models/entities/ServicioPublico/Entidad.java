package models.entities.ServicioPublico;

import javax.persistence.*;

import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;
import models.entities.localizacion.Localizacion;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "entidad")
@Setter
@Getter
public class Entidad {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idEntidad")
  private Long idEntidad;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "descripcion")
  private String descripcion;

  @OneToOne
  @JoinColumn(name = "idLocalizacion", referencedColumnName = "idLocalizacion")
  private Localizacion localizacion;

  @OneToMany(mappedBy = "entidad", cascade = {CascadeType.ALL})
  private List<Establecimiento> establecimientos = new ArrayList<>();

  @Transient
  private List<Miembro> miembrosInteresados = new ArrayList<>();

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "idPrestadora", referencedColumnName = "idPrestadora")
  private Prestadora prestadora;


  @Column(name = "deleted")
  private Boolean deleted;
  
  public Entidad(){}
  public Entidad(String nombre){
    this.nombre = nombre;
  }


  public float promedioDeTiempoDeCierreDeIncidentes() {
    float total = 0;
    float cantidad = 0;

    for(Establecimiento establecimiento : establecimientos) {
      // TODO: no se contempla que sea en la semana
      List<Incidente> incidentes = establecimiento.incidentes();

      cantidad += (float) incidentes.size();
      for(Incidente incidente : incidentes) {
        total += incidente.tiempoDeCierre();
        System.out.println("TOTAL POR AHORA: " + total);
      }
    }

    return total / (cantidad == 0 ? 1 : cantidad);
  }

  public float cantidadDeIncidentesReportadosEnLaSemana() {
    int cantidad = 0;

    for(Establecimiento establecimiento : establecimientos) {
      cantidad += establecimiento.cantidadDeIncidentesReportadosEnLaSemana();
    }

    return cantidad;
  }

  public void addEstablecimiento(Establecimiento establecimiento) {
    this.establecimientos.add(establecimiento);
  }
}
