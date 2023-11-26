package models.entities.Servicio;

import models.entities.Comunidad.Miembro;
import models.entities.Incidente.Incidente;

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




    public PrestacionDeServicio(Servicio servicio, Boolean estaDisponible) {
        this.servicio = servicio;
        this.miembrosInteresados = new ArrayList<>();
        this.incidentes = new ArrayList<>();
    }

    public PrestacionDeServicio() {}

    public int cantidadDeIncidentesReportadosEnLaSemana() {
        int cantidad = 0;
        List<Incidente> incidentesAbiertos = new ArrayList<>();

        //for(Incidente incidente : incidentes) {
        //    if(incidente.getTiempoFinal() != null) {
        //        cantidad++;
        //    }
        //    else {
        //        incidentesAbiertos.add(incidente);
        //    }
        //}

        Incidente incidenteRemovido;
        while(!incidentesAbiertos.isEmpty()) {
            incidenteRemovido = incidentesAbiertos.remove(0);

            if(!this.yaSeContemplaAlIncidente(incidenteRemovido, incidentesAbiertos)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private boolean yaSeContemplaAlIncidente(Incidente incidenteRemovido, List<Incidente> incidentesAbiertos) {
        LocalDateTime t0 = incidenteRemovido.getTiempoInicial();
        return incidentesAbiertos.stream().anyMatch(entidad -> this.pasaronMenosDe24h(t0, entidad.getTiempoInicial()));
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
