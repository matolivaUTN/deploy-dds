package models.entities;

import models.entities.Localizacion.Localizacion;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.ListadoDeDepartamentos;
import models.entities.georef.entities.ListadoDeMunicipios;
import models.entities.georef.entities.ListadoDeProvincias;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;
import models.entities.georef.ServicioGeoref;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

/**
 * El sistema deberá permitir mostrar todas los municipios de una provincia seleccionada (de Argentina)
 */
public class EjemploDeUso {

  public static void main(String[] args) throws IOException {
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    System.out.println("Seleccione una de las siguientes provincias, ingresando su id:");

    ListadoDeProvincias listadoDeProvinciasArgentinas = servicioGeoref.listadoDeProvincias();

    listadoDeProvinciasArgentinas.provincias.sort((p1, p2) -> p1.getIdProvincia() >= p2.getIdProvincia()? 1 : -1);

    for(Provincia unaProvincia:listadoDeProvinciasArgentinas.provincias){
      //System.out.println(unaProvincia.id + ") " + unaProvincia.nombre);
      Localizacion nuevaProvincia = new Localizacion();
      nuevaProvincia.setProvincia(unaProvincia);
      System.out.println("id: " + nuevaProvincia.getProvincia().getIdProvincia() + " " + nuevaProvincia.getProvincia().getNombre());
    }

    Scanner entradaEscaner = new Scanner(System.in);
    int idProvinciaElegida = Integer.parseInt(entradaEscaner.nextLine());

    Optional<Provincia> posibleProvincia = listadoDeProvinciasArgentinas.provinciaDeId(idProvinciaElegida);

    if(posibleProvincia.isPresent()){
      Provincia provinciaSeleccionada = posibleProvincia.get();
      ListadoDeMunicipios municipiosDeLaProvincia = servicioGeoref.listadoDeMunicipiosDeProvincia(provinciaSeleccionada);
      System.out.println("Los municipios de la provincia "+ provinciaSeleccionada.nombre +" son:");
      for(Municipio unMunicipio: municipiosDeLaProvincia.municipios){
        //System.out.println(unMunicipio.nombre);
        //Localizacion nuevoMunicipio = new Localizacion(unMunicipio.nombre, TipoLocalizacion.MUNICIPIO);
        Localizacion nuevoMunicipio = new Localizacion();
        nuevoMunicipio.setMunicipio(unMunicipio);

        System.out.println(nuevoMunicipio.getMunicipio().getNombre());
      }

      System.out.println("Los departamentos de la provincia "+ provinciaSeleccionada.nombre +" son:");
      ListadoDeDepartamentos listadoDeDepartamentos = servicioGeoref.listadoDeDepartamentos(provinciaSeleccionada);
      for(Departamento departamento:listadoDeDepartamentos.departamentos){
        //System.out.println(departamento.nombre);
        Localizacion nuevoDepartamento = new Localizacion();
        nuevoDepartamento.setDepartamento(departamento);

        System.out.println(nuevoDepartamento.getDepartamento().getNombre());
      }

    }
    else{
      System.out.println("No existe la provincia seleccionada");
    }
  }
}