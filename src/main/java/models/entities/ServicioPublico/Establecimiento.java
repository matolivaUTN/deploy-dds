package models.entities.ServicioPublico;

import javax.persistence.*;

import models.entities.Localizacion.Localizacion;
import models.entities.Incidente.Incidente;
import java.util.List;
import java.util.ArrayList;
import models.entities.Servicio.PrestacionDeServicio;
import lombok.Setter;
import lombok.Getter;

//Fijarse si la estación debe conocer a las líneas a las que pertenece


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

    @OneToMany(mappedBy = "establecimiento")
    private List<PrestacionDeServicio> prestaciones;

    @ManyToOne // Agregar esta anotación
    @JoinColumn(name = "idEntidad", referencedColumnName = "idEntidad")
    private Entidad entidad;

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