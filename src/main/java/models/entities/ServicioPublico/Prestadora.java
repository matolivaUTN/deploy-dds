package models.entities.ServicioPublico;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import models.entities.Comunidad.Miembro;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestadora")
@Setter
@Getter
public class Prestadora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPrestadora")
    private Long idPrestadora;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "idMiembroDesignado", referencedColumnName = "idMiembro")
    private Miembro designado;

    @ManyToOne
    private Organismo organismo;

    @OneToMany(mappedBy = "prestadora")
    private List<Entidad> entidades;


    public Prestadora(){}

    public Prestadora(String nuevaPrestadora) {
        nombre = nuevaPrestadora;
        entidades = new ArrayList<>();
    }

    public void designar(Miembro miembro) {

    }
    public void enviarInformacion() {

    }
}


