package models.entities.ServicioPublico;

import javax.persistence.*;

import models.entities.localizacion.Localizacion;
import models.entities.incidente.Incidente;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import models.entities.servicio.PrestacionDeServicio;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "establecimiento")
@Setter
@Getter
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstablecimiento")
    private Long idEstablecimiento;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "establecimiento", cascade = {CascadeType.ALL})
    private List<PrestacionDeServicio> prestaciones;

    @ManyToOne(cascade = {CascadeType.ALL}) // Agregar esta anotación
    @JoinColumn(name = "idEntidad", referencedColumnName = "idEntidad")
    private Entidad entidad;

    @OneToOne
    @JoinColumn(name = "idLocalizacion", referencedColumnName = "idLocalizacion")
    private Localizacion localizacion;

    @Column(name = "deleted")
    private Boolean deleted;

    public Establecimiento(){}

    public void agregarPrestacion(PrestacionDeServicio prestacion) {
        prestaciones.add(prestacion);
    }

    public List<Incidente> incidentes() {
        List<Incidente> incidentes = new ArrayList<>();

        for(PrestacionDeServicio prestacionDeServicio : prestaciones) {
            incidentes.addAll(prestacionDeServicio.getIncidentes());
        }

        return incidentes;
    }

    public int cantidadDeIncidentesReportadosEnLaSemana() {
        int cantidad = 0;

        for(PrestacionDeServicio prestacionDeServicio : prestaciones) {
            cantidad += prestacionDeServicio.cantidadDeIncidentesReportadosEnLaSemana();
        }

        return cantidad;
    }


}