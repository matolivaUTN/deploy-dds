package models.entities.comunidad;

import models.entities.converter.LocalTimeAttributeConverter;
import models.entities.localizacion.Localizacion;
import models.entities.notificaciones.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.entities.converter.MedioDeNotificacionConverter;
import models.entities.converter.EstrategiaDeAvisoConverter;
import models.entities.roles.Rol;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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

  @ElementCollection
  @Convert(converter = LocalTimeAttributeConverter.class)
  @Column(name = "horario", columnDefinition = "TIME")
  private List<LocalTime> horariosDePreferencia = new ArrayList<LocalTime>() ;

  @Column(name = "deleted")
  private Boolean deleted;

  @ManyToOne
  @JoinColumn(name = "rol_id", referencedColumnName = "id")
  private Rol rol;

  public Miembro() {
    this.comunidadesDeLasQueFormaParte = new ArrayList<>();
  }

  public void agregarComunidad(Comunidad comunidad) {
    this.comunidadesDeLasQueFormaParte.add(comunidad);
  }

  public void eliminarComunidad(Comunidad comunidad) {
    this.comunidadesDeLasQueFormaParte.remove(comunidad);
  }

  public void agregarHorarios(LocalTime ... horarios) {
    Collections.addAll(horariosDePreferencia, horarios);
  }

  public void agregarHorario(LocalTime horario) {
    this.horariosDePreferencia.add(horario);
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
