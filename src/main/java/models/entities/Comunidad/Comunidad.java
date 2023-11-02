package models.entities.Comunidad;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.Servicio.Servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comunidad")
@Getter
@Setter
public class Comunidad {
  @Id
  @GeneratedValue
  private Long idComunidad;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "descripcion")
  private String descripcion;

  @Column(name = "puntaje")
  private Double puntaje;

  @Column(name = "estaHabilitada")
  private Integer estaHabilitada;

  //TODO
  //@OneToMany(mappedBy = "comunidad")
  @Transient
  private List<PrestacionDeServicio> prestaciones = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "miembroPorComunidad",
      joinColumns = @JoinColumn(name = "idComunidad"),
      inverseJoinColumns = @JoinColumn(name = "idMiembro")
  )
  private List<Miembro> miembros = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "adminPorComunidad",
      joinColumns = @JoinColumn(name = "idComunidad"),
      inverseJoinColumns = @JoinColumn(name = "idMiembro")
  )
  private List<Miembro> admins;

  //@OneToMany(mappedBy = "comunidad")
  //private List<Incidente> incidentes;

  public Comunidad() {


  }

  public Comunidad(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public void agregarMiembros(Miembro ... miembros) {
    Collections.addAll(this.miembros, miembros);
  }

  public void eliminarMiembros(Miembro ... miembros) {
    for(Miembro miembro : miembros) {
      this.miembros.remove(miembro);
      this.admins.remove(miembro);
    }
  }

  public Boolean esMiembro(Miembro miembro) {
    return this.miembros.contains(miembro);
  }

  public List<Miembro> filtrarMiembrosNoInteresados(Incidente incidente){
    return this.miembros.stream().filter(miembro -> !miembro.estaInteresadoEn(incidente.getPrestacionAfectada())).collect(Collectors.toList());
  }

  public Boolean esAdmin(Miembro miembro){
    return admins.contains(miembro);
  }

  public void agregarPrestacion(PrestacionDeServicio prestacion){
    this.prestaciones.add(prestacion);
  }


  public void agregarAdmin(Miembro miembro) {
    this.admins.add(miembro);
  }
}
