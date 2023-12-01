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
import models.entities.comunidad.Miembro;
import lombok.Getter;
import lombok.Setter;
import models.entities.roles.Rol;

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

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "idMiembroDesignado", referencedColumnName = "idMiembro")
    private Miembro designado;

    @ManyToOne
    @JoinColumn(name = "organismo", referencedColumnName = "idOrganismo")
    private Organismo organismo;

    @OneToMany(mappedBy = "prestadora")
    private List<Entidad> entidades = new ArrayList<>();

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Rol rol;

    public Prestadora(){}
    public Prestadora(String nuevaPrestadora, String newPassword) {
        nombre = nuevaPrestadora;
        password = newPassword;
        entidades = new ArrayList<>();
        deleted = false;
    }
}