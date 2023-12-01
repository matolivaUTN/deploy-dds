package models.entities.notificaciones;


import lombok.Getter;
import lombok.Setter;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;

import javax.persistence.*;

@Entity
@Table(name = "notificacionPorMiembro")
@Setter
@Getter
public class NotificacionPorMiembro {

    @Id
    @GeneratedValue
    private Long idNotificacionPorMiembro;

    @ManyToOne
    @JoinColumn(name = "idNotificacion")
    private Notificacion notificacion;

    @ManyToOne
    @JoinColumn(name = "idMiembro")
    private Miembro miembro;


    @ManyToOne
    @JoinColumn(name = "idIncidente")
    private Incidente incidente;


    // Es importante llevar registro de si esa notificacion ya fue enviada para ese miembro
    @Column(name = "fueEnviada")
    private Boolean fueEnviada;

    @Column(name = "deleted")
    private Boolean deleted;

}
