package models.entities.servicio;

import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import models.entities.ServicioPublico.Establecimiento;

@Entity
@Table(name = "prestacionDeServicio")
@Setter
@Getter
public class PrestacionDeServicio {

    @Id
    @GeneratedValue
    private Long idPrestacion;

    @ManyToOne
    @JoinColumn(name = "idServicio")
    private Servicio servicio;

    @ManyToMany
    @JoinTable(
        name = "miembroInteresadoPorPrestacion",
        joinColumns = @JoinColumn(name = "idPrestacion"),
        inverseJoinColumns = @JoinColumn(name = "idMiembro")
    )
    private List<Miembro> miembrosInteresados;


    @OneToMany(mappedBy = "prestacionAfectada")
    private List<Incidente> incidentes;

    @ManyToOne
    @JoinColumn(name = "idEstablecimiento", referencedColumnName = "idEstablecimiento")
    private Establecimiento establecimiento;

    @Column(name = "esta_disponible")
    private Boolean estaDisponible;

    @Column(name = "deleted")
    private Boolean deleted;



    public PrestacionDeServicio() {}
    public PrestacionDeServicio(Servicio servicio, Boolean estaDisponible) {
        this.servicio = servicio;
        this.miembrosInteresados = new ArrayList<>();
        this.incidentes = new ArrayList<>();
    }

    public int cantidadDeIncidentesReportadosEnLaSemana() {
        // TODO: no contempla que sean en la semana
        // TODO: no contempla el siguiente requerimiento:
        /* Una vez que un incidente sobre una prestación es reportado por algún usuario, independientemente de la comunidad de la que forma parte, no se consideran, para el presente ranking, ningún incidente que se genere sobre dicha prestación en un plazo de 24 horas siempre y cuando el mismo continúe abierto.  */
        // Esto quiere decir que si se abrieron varios incidentes, solo deben contarse si no fueron hechos en el mismo día, si es que sigue abierto.
        return incidentes.size();
    }

    private boolean yaSeContemplaAlIncidente(Incidente incidenteRemovido, List<Incidente> incidentesAbiertos) {
        LocalDateTime t0 = incidenteRemovido.getFechaDeApertura();
        return incidentesAbiertos.stream().anyMatch(entidad -> this.pasaronMenosDe24h(t0, entidad.getFechaDeApertura()));
    }

    private boolean pasaronMenosDe24h(LocalDateTime unTiempoInicial, LocalDateTime otroTiempoInicial) {
        return Duration.between(unTiempoInicial, otroTiempoInicial).toHours() < 24;
    }

    public void agregarMiembroInteresado(Miembro miembro) {
        this.miembrosInteresados.add(miembro);
    }

    public void eliminarMiembroInteresado(Miembro miembro) {
        this.miembrosInteresados.remove(miembro);
    }
}
