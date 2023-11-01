package models.entities.Comunidad;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.entities.Localizacion.Localizacion;
import models.entities.Notificaciones.EstrategiaDeAviso;
import models.entities.Notificaciones.Notificador;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.Incidente.Incidente;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.entities.Converter.MedioDeNotificacionConverter;
import models.entities.Converter.EstrategiaDeAvisoConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.entities.Incidente.EstadoPorComunidad;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "miembro")
public class Miembro {
  @Id
  @GeneratedValue
  private Long idMiembro;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "email")
  private String email;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "puntaje")
  private Double puntaje;

  @Convert(converter = MedioDeNotificacionConverter.class)
  @Column(name = "medioNotificacion")
  private Notificador notificador;

  @Convert(converter = EstrategiaDeAvisoConverter.class) // Aplica el convertidor aquí
  @Column(name = "estrategia_aviso")
  private EstrategiaDeAviso estrategiaDeAviso;

  @ManyToOne
  @JoinColumn(name = "idLocalizacion")
  private Localizacion localizacion;

  @ManyToMany(mappedBy = "miembros")
  @JsonBackReference
  private List<Comunidad> comunidadesDeLasQueFormaParte;

  //Dudoso
  @Transient
  private List<Rol> rolesPorPrestacion;

  public Miembro() {
    this.rolesPorPrestacion = new ArrayList<>();
    this.comunidadesDeLasQueFormaParte = new ArrayList<>();
  }

  public void darDeBajaComunidad(Comunidad comunidad){
    if(comunidad.esAdmin(this)) {
      //Eliminar comunidad
    }
  }

  public void setUbicacion(Float lat, Float lon){
    //this.latitud = lat;
    //this.longitud = lon;
  }


  public void agregarComunidad(Comunidad comunidad) {
    this.comunidadesDeLasQueFormaParte.add(comunidad);
  }

  public void eliminarComunidad(Comunidad comunidad) {
    this.comunidadesDeLasQueFormaParte.remove(comunidad);
  }

  //Vamos a considerar que si un miembro esta interesado, necesariamente es afectado
  public Boolean estaInteresadoEn(PrestacionDeServicio prestacion){
    prestacion.agregarMiembroInteresado(this);
    this.rolesPorPrestacion.add(new Rol(prestacion, true));

    return true;
  }

  public void asignarRol(PrestacionDeServicio prestacion, Boolean estado){

  }

  public void noEstaInteresadoEn(PrestacionDeServicio prestacion) {

    // Por como planteamos el disenio, cuando un miembro deja de estar interesado necesariamente pasa a ocupar el rol de observador
    // Esta interesado <=> es afectado


    // Pero con esta estrategia un miembro cuando entra a una comunidad no tiene ningun rol
    // Recien lo va a tener cuando se muestre interesado en algo
    // Entonces capaz deberiamos hacer que cuando un miembro entra a una comunidad, hacer que este sea observador para todas las prestaciones de esa comunidad
    prestacion.eliminarMiembroInteresado(this);
  }

  public void solicitarAltaDeComunidad(Miembro miembro, String nombreComunidad, String descripcionComunidad) {

  }

  public void cerrarIncidente(Incidente incidente) {
    /*
    for comunidad in comunidades
      estado = find indicente.comunidad == comunidad
      estado.cerrar

     */
    List<EstadoPorComunidad> estados = incidente.getEstados();
    for(Comunidad comunidad: this.comunidadesDeLasQueFormaParte) {
      for (EstadoPorComunidad estado : estados) {
        if (estado.getComunidad() == comunidad) {
          estado.setEstaAierto(false);
        }
      }
    }
  }
/*
  public List<Incidente> consultarIncidentesReportados(Comunidad comunidad, Boolean estado) {

    List<Incidente> incidentesFiltrados = new ArrayList<>();

    if(comunidad.esMiembro(this)) {
      incidentesFiltrados = comunidad.mostrarIncidentesSegunEstado(estado);
    }

    // Retornamos la lista filtrada de incidentes para los tests
    return incidentesFiltrados;
  }
*/
  public void enviarNotificacion(String mensaje) {
      this.notificador.enviarNotificacion(this, mensaje);
  }

  public void setLocalizacion(Localizacion localizacion){
    this.localizacion = localizacion;
    //TODO CHEQUEAR SI HAY QUE MANDAR SUGERENCIA. TODAVIA NO SABEMOS BIEN COMO HACERLO
  }
}
