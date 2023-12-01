package models.entities.incidente;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Setter;
import models.entities.servicio.PrestacionDeServicio;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import models.entities.comunidad.Miembro;

@Entity
@Table(name = "incidente")
@Getter
@Setter
public class Incidente {
    @Id
    @GeneratedValue
    private Long idIncidente;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fechaAlta", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime fechaDeApertura;

    @Column(name = "fechaDeCierre", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime fechaDeCierre;



    @Column(name = "observaciones")
    private String observaciones;

    // Prestacion de servicio tiene una lista de incidentes pero realmente no la usamos para nada (no existen consultas de incidentes x prestacion)
    @ManyToOne
    @JoinColumn(name = "idPrestacion")
    private PrestacionDeServicio prestacionAfectada;

    @ManyToOne
    @JoinColumn(name = "idMiembro", referencedColumnName = "idMiembro")
    private Miembro miembroCreador;

    @ManyToOne
    @JoinColumn(name = "idMiembroCerrador")
    private Miembro cerrador;

    @Column(name = "estaAbierto")
    private Boolean estaAbierto;

    @Column(name = "deleted")
    private Boolean deleted;


    public Incidente() {}

    public Incidente(String unasObservaciones, PrestacionDeServicio prestacion) {
        observaciones = unasObservaciones;
        prestacionAfectada = prestacion;

    }

    public void cerrar(Miembro miembroCerrador) {
        cerrador = miembroCerrador;
        estaAbierto = false;
        fechaDeCierre = LocalDateTime.now();
    }

    public float tiempoDeCierre() {
        LocalDateTime fechaCierrePosta = fechaDeCierre == null ? LocalDateTime.now() : fechaDeCierre;
        return Duration.between(fechaDeApertura, fechaCierrePosta).toMinutes();
    }
}
