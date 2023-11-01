package models.entities.Incidente;

import models.entities.Comunidad.Comunidad;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.entities.Comunidad.Miembro;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "estadoPorComunidad")
@Setter
@Getter
public class EstadoPorComunidad {
    @Id
    @GeneratedValue
    private Long idEstadoPorComunidad;

    @ManyToOne
    @JoinColumn(name = "idComunidad")
    private Comunidad comunidad;

    @ManyToOne
    @JoinColumn(name = "idIncidente")
    private Incidente incidente;

    @Column(name = "estaAbierto")
    private Boolean estaAbierto;

    @Column(name = "fechaDeCierre")
    private LocalDateTime fechaDeCierre;

    @ManyToOne
    @JoinColumn(name = "idMiembroCerrador")
    private Miembro cerrador;




    public EstadoPorComunidad(){}

    public void setEstaAierto(boolean nuevoEstado) {
        this.estaAbierto = nuevoEstado;
    }
}

