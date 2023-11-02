package models.entities.ServicioPublico;

import javax.persistence.*;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;
import models.entities.Localizacion.Localizacion;

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
  private Localizacion ubicacion;


  @OneToMany(mappedBy = "entidad", cascade = {CascadeType.ALL})
  private List<Establecimiento> establecimientos = new ArrayList<>();


  @Transient
  private List<Miembro> miembrosInteresados = new ArrayList<>();

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "idPrestadora", referencedColumnName = "idPrestadora")
  private Prestadora prestadora;
  
  public Entidad(){}
  public Entidad(String nombre){
    this.nombre = nombre;
  }

  public void darDeBaja() {

  }

  public float promedioDeTiempoDeCierreDeIncidentes() {
    float total = 0;
    float cantidad = 0;

    for(Establecimiento establecimiento : establecimientos) {
      List<Incidente> incidentes = establecimiento.incidentes();

      //cantidad += (float) incidentes.size();
      //for(Incidente incidente : incidentes) {
      //  total += incidente.tiempoDeCierre();
      //}
    }

    //return total / cantidad;
    return 1;
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
