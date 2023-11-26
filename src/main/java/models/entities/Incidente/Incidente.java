package models.entities.Incidente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import models.entities.Comunidad.Comunidad;
import models.entities.Servicio.PrestacionDeServicio;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import models.entities.Comunidad.Miembro;
import models.entities.Servicio2.LocalDateTimeAdapter;

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

    @Column(name = "fechaAlta", columnDefinition = "DATE")
    private LocalDateTime tiempoInicial;

    @OneToMany(mappedBy = "incidente")
    private List<EstadoPorComunidad> estados;

    @Column(name = "observaciones")
    private String observaciones;


    // Prestacion de servicio tiene una lista de incidentes pero realmente no la usamos para nada (no existen consultas de incidentes x prestacion)
    @ManyToOne
    @JoinColumn(name = "idPrestacion")
    private PrestacionDeServicio prestacionAfectada;

    @ManyToOne
    @JoinColumn(name = "idMiembro", referencedColumnName = "idMiembro")
    private Miembro miembroCreador;

    public Incidente(String observaciones, PrestacionDeServicio prestacion) {
        this.tiempoInicial = LocalDateTime.now();
        this.observaciones = observaciones;
        this.estados = new ArrayList<EstadoPorComunidad>();
        this.prestacionAfectada = prestacion;

    }


    public Incidente() {}

    public void cerrar(Miembro miembro) { //Esto estaria mejor hacer con una Exception.

        estados.forEach(unEstado -> {
            Comunidad unaComunidad = unEstado.getComunidad();
            if(miembro.getComunidadesDeLasQueFormaParte().contains(unaComunidad)) {
                unEstado.setEstaAierto(false);
                unEstado.setFechaDeCierre(LocalDateTime.now());
            }
        });
    }

    public EstadoPorComunidad estadoDeComunidad(Comunidad comunidad){
        EstadoPorComunidad estadoBuscado = null;

        for (EstadoPorComunidad estado : this.estados) {
            if (estado.getComunidad() == comunidad) {
                estadoBuscado = estado;
                break; // Terminar el bucle una vez que se encuentra el estado
            }
        }

        return estadoBuscado;
    }

    public void agregarEstado(EstadoPorComunidad estadoPorComunidad) {
        this.estados.add(estadoPorComunidad);
    }
}
