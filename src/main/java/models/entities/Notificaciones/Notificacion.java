package models.entities.Notificaciones;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Setter;
import lombok.Getter;


@Entity
@Table(name = "notificacion")
@Setter
@Getter
public class Notificacion {
    @Id
    @GeneratedValue
    private Long idNotificacion;

    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "fueLeida")
    private Boolean fueLeida = false;

    @Column(name = "fueEnviada")
    private Boolean fueEnviada;

    @ManyToOne
    @JoinColumn(name = "idIncidente")
    private Incidente incidente;

    @ManyToOne
    @JoinColumn(name = "idMiembro")
    private Miembro miembro;

    public Notificacion(Incidente unIncidente, Miembro unMiembro) {
        this.incidente = unIncidente;
        this.miembro = unMiembro;
    }
    public Notificacion() {}
}