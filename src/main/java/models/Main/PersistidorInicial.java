package models.Main;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.*;

import models.entities.georef.ServicioGeoref;
import models.entities.georef.entities.*;
import models.repositories.RepositorioDepartamentos;
import models.repositories.RepositorioMunicipios;
import models.repositories.RepositorioProvincias;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class PersistidorInicial implements WithSimplePersistenceUnit {

  private RepositorioProvincias repositorioProvincias;
  private RepositorioDepartamentos repositorioDepartamentos;
  private RepositorioMunicipios repositorioMunicipios;


  public static void main(String[] args) throws IOException {
    //new PersistidorInicial().start();
  }

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
      try{
        this.repositorioProvincias.agregar(provincia);

      }catch (Exception e){

      }

    }
  }


  private void persistirDepartamentos(EntityManager entityManager) throws IOException {
    try{
      ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
      ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

      for(Provincia provincia : provinciasArgentinas.getProvincias()) {
        ListadoDeDepartamentos departamentos = servicioGeoref.departamentosDeProvincia(provincia);

        for(Departamento departamento : departamentos.getDepartamentos()) {
          departamento.setProvincia(provincia);
          this.repositorioDepartamentos.agregar(departamento);
        }
      }
    }catch (Exception e){

    }

  }

  private void persistirMunicipios(EntityManager entityManager) throws IOException {
    try{
      ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
      ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

      for(Provincia provincia : provinciasArgentinas.getProvincias()) {
        ListadoDeMunicipios municipios = servicioGeoref.municipiosDeProvincia(provincia);

        for(Municipio municipio : municipios.getMunicipios()) {
          municipio.setProvincia(provincia);
          this.repositorioMunicipios.agregar(municipio);
        }
      }

    }catch (Exception e){

    }


  }

}


