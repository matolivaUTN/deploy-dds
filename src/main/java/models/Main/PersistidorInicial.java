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

  static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();
  static EntityManager entityManager = entityManagerFactory.createEntityManager();

  static RepositorioProvincias repositorioProvincias = new RepositorioProvincias(entityManager);
  static RepositorioDepartamentos repositorioDepartamentos = new RepositorioDepartamentos(entityManager);
  static RepositorioMunicipios repositorioMunicipios = new RepositorioMunicipios(entityManager);

  public static void main(String[] args) throws IOException {
    new PersistidorInicial().start();
  }

  public static void start() throws IOException {
    persistirProvincias();
    persistirMunicipios();
    persistirDepartamentos();
  }

  private static void persistirProvincias() throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

    for(Provincia provincia : provinciasArgentinas.getProvincias()) {
      repositorioProvincias.agregar(provincia);
    }
  }


  private static void persistirDepartamentos() throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

    for(Provincia provincia : provinciasArgentinas.getProvincias()) {
      ListadoDeDepartamentos departamentos = servicioGeoref.departamentosDeProvincia(provincia);

      for(Departamento departamento : departamentos.getDepartamentos()) {
        departamento.setProvincia(provincia);
        repositorioDepartamentos.agregar(departamento);
      }
    }
  }

  private static void persistirMunicipios() throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    ListadoDeProvincias provinciasArgentinas = servicioGeoref.provincias();

    for(Provincia provincia : provinciasArgentinas.getProvincias()) {
      ListadoDeMunicipios municipios = servicioGeoref.municipiosDeProvincia(provincia);

      for(Municipio municipio : municipios.getMunicipios()) {
        municipio.setProvincia(provincia);
        repositorioMunicipios.agregar(municipio);
      }
    }
  }

  public static EntityManagerFactory createEntityManagerFactory() {
    // https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
    Map<String, String> env = System.getenv();
    Map<String, Object> configOverrides = new HashMap<String, Object>();

    String[] keys = new String[] { "javax.persistence.jdbc.url", "javax.persistence.jdbc.user",
            "javax.persistence.jdbc.password", "javax.persistence.jdbc.driver", "hibernate.hbm2ddl.auto",
            "hibernate.connection.pool_size", "hibernate.show_sql" };

    for (String key : keys) {
      if (env.containsKey(key)) {

        String value = env.get(key);
        configOverrides.put(key, value);

      }
    }

    return Persistence.createEntityManagerFactory("db", configOverrides);

  }


}


