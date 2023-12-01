package server.init;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import models.entities.georef.ServicioGeoref;
import models.entities.georef.entities.*;
import models.entities.roles.Rol;
import models.entities.roles.TipoRol;
import models.repositories.RepositorioDepartamentos;
import models.repositories.RepositorioMunicipios;
import models.repositories.RepositorioProvincias;

import javax.persistence.EntityManager;
import java.io.IOException;

public class PersistidorGeoref implements WithSimplePersistenceUnit {

  private RepositorioProvincias repositorioProvincias;
  private RepositorioDepartamentos repositorioDepartamentos;
  private RepositorioMunicipios repositorioMunicipios;


  public void start(EntityManager entityManager) throws IOException {
    this.repositorioProvincias = new RepositorioProvincias(entityManager);
    this.repositorioDepartamentos = new RepositorioDepartamentos(entityManager);
    this.repositorioMunicipios = new RepositorioMunicipios(entityManager);
    persistirProvincias(entityManager);
    persistirMunicipios(entityManager);
    persistirDepartamentos(entityManager);
  }

  private void persistirProvincias(EntityManager entityManager) throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

    for(Provincia provincia : provinciasArgentinas.getProvincias()) {
      this.repositorioProvincias.agregar(provincia);
    }
  }


  private void persistirDepartamentos(EntityManager entityManager) throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

    for(Provincia provincia : provinciasArgentinas.getProvincias()) {
      ListadoDeDepartamentos departamentos = servicioGeoref.departamentosDeProvincia(provincia);

      for(Departamento departamento : departamentos.getDepartamentos()) {
        departamento.setProvincia(provincia);
        this.repositorioDepartamentos.agregar(departamento);
      }
    }
  }

  private void persistirMunicipios(EntityManager entityManager) throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

    for(Provincia provincia : provinciasArgentinas.getProvincias()) {
      ListadoDeMunicipios municipios = servicioGeoref.municipiosDeProvincia(provincia);

      for(Municipio municipio : municipios.getMunicipios()) {
        municipio.setProvincia(provincia);
        this.repositorioMunicipios.agregar(municipio);
      }
    }
  }

}


