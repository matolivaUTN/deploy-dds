package models.entities.localizacion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;

@Entity
@Table(name = "localizacion")
@Setter
@Getter
public class Localizacion {
  public Localizacion(Provincia provincia, Departamento departamento, Municipio municipio) {
      this.provincia = provincia;
      this.departamento = departamento;
      this.municipio = municipio;
  }

  public Localizacion() {

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idLocalizacion")
  private Long idLocalizacion;

  @ManyToOne
  @JoinColumn(name = "idDepartamento")
  private Departamento departamento;

  @ManyToOne
  @JoinColumn(name = "idProvincia")
  private Provincia provincia;

  @ManyToOne
  @JoinColumn(name = "idMunicipio")
  private Municipio municipio;
}
