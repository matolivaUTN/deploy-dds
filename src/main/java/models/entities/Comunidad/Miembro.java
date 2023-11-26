package models.entities.Comunidad;

import models.entities.Localizacion.Localizacion;
import models.entities.Notificaciones.*;
import models.entities.Servicio.PrestacionDeServicio;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.entities.Converter.MedioDeNotificacionConverter;
import models.entities.Converter.EstrategiaDeAvisoConverter;

import java.util.ArrayList;
import java.util.List;
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

  @Column(name = "telefono")
  private Long telefono;

  @Convert(converter = MedioDeNotificacionConverter.class)
  @Column(name = "medioNotificacion")
  private MedioDeNotificacion notificador;

  @Convert(converter = EstrategiaDeAvisoConverter.class)
  @Column(name = "estrategia_aviso")
  private EstrategiaDeAviso estrategiaDeAviso;

  @ManyToOne
  @JoinColumn(name = "idLocalizacion")
  private Localizacion localizacion;

  @ManyToMany(mappedBy = "miembros", cascade = {CascadeType.ALL})
  private List<Comunidad> comunidadesDeLasQueFormaParte;



  //Dudoso
  @Transient
  private List<Rol> rolesPorPrestacion;

  public Miembro() {
    this.rolesPorPrestacion = new ArrayList<>();
    this.comunidadesDeLasQueFormaParte = new ArrayList<>();
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


  public void noEstaInteresadoEn(PrestacionDeServicio prestacion) {

    // Por como planteamos el disenio, cuando un miembro deja de estar interesado necesariamente pasa a ocupar el rol de observador
    // Esta interesado <=> es afectado


    // Pero con esta estrategia un miembro cuando entra a una comunidad no tiene ningun rol
    // Recien lo va a tener cuando se muestre interesado en algo
    // Entonces capaz deberiamos hacer que cuando un miembro entra a una comunidad, hacer que este sea observador para todas las prestaciones de esa comunidad
    prestacion.eliminarMiembroInteresado(this);
  }






  public void enviarNotificacion(String mensaje) {
      this.notificador.enviarNotificacion(this, mensaje);
  }

  public void setLocalizacion(Localizacion localizacion){
    this.localizacion = localizacion;
    //TODO CHEQUEAR SI HAY QUE MANDAR SUGERENCIA. TODAVIA NO SABEMOS BIEN COMO HACERLO
  }

  public String getNotificadorAsString() {
    MedioDeNotificacion medio = this.getNotificador();
    String medioAsString = "";

    if(medio instanceof NotificadorWhatsapp) {
      medioAsString = "WhatsApp";
    }
    else if(medio instanceof NotificadorEmail) {
      medioAsString = "Email";
    }
    return medioAsString;
  }

  public String getEstrategiaDeAvisoAsString() {
    EstrategiaDeAviso estrategia = this.getEstrategiaDeAviso();
    String estrategiaAsString = "";

    if(estrategia instanceof CuandoSuceden) {
      estrategiaAsString = "Cuando suceden";
    }
    else if(estrategia instanceof SinApuros) {
      estrategiaAsString = "Sin apuros";
    }
    return estrategiaAsString;
  }
}
