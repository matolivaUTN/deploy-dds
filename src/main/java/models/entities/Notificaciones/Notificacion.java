package models.entities.notificaciones;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

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

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "asunto")
    private String asunto;

    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDateTime fecha = LocalDateTime.now();


    @OneToMany(mappedBy = "notificacion")
    private List<NotificacionPorMiembro> notificacionesPorMiembros;

    @Column(name = "deleted")
    private Boolean deleted;


    public Notificacion() {}

    public void agregarNotificacionPorMiembro(NotificacionPorMiembro noti) {
        this.notificacionesPorMiembros.add(noti);
    }
}