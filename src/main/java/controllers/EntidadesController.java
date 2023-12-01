package controllers;

import com.opencsv.exceptions.CsvValidationException;
import io.javalin.http.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import io.javalin.http.HttpStatus;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.csv.LectorCSV;
import models.entities.localizacion.Localizacion;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Organismo;
import models.entities.ServicioPublico.Prestadora;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;
import models.repositories.*;

public class EntidadesController extends Controller {
  private RepositorioOrganismos repositorioOrganismos;
  private RepositorioEntidades repositorioEntidades;
  private RepositorioPrestadoras repositorioPrestadoras;
  private RepositorioProvincias repositorioProvincias;
  private RepositorioMunicipios repositorioMunicipios;
  private RepositorioDepartamentos repositorioDepartamentos;
  private RepositorioLocalizaciones repositorioLocalizaciones;
  private RepositorioRoles repositorioRoles;

  public EntidadesController(RepositorioEntidades repositorioEntidades, RepositorioOrganismos repositorioOrganismos, RepositorioPrestadoras repositorioPrestadoras, RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios, RepositorioDepartamentos repositorioDepartamentos, RepositorioLocalizaciones repositorioLocalizaciones, RepositorioRoles repositorioRoles) {
    this.repositorioEntidades = repositorioEntidades;
    this.repositorioOrganismos = repositorioOrganismos;
    this.repositorioPrestadoras = repositorioPrestadoras;
    this.repositorioProvincias = repositorioProvincias;
    this.repositorioMunicipios = repositorioMunicipios;
    this.repositorioDepartamentos = repositorioDepartamentos;
    this.repositorioLocalizaciones = repositorioLocalizaciones;
    this.repositorioRoles = repositorioRoles;
  }


  public void index(Context context) {

    // Listado de entidades
    List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

    Map<String, Object> model = new HashMap<>();

    model.put("entidades", entidades);
    cargarRolesAModel(context, model);
    context.render("listadoEntidades.hbs", model);
  }

  public void create(Context context) {
    // Pantalla de creacion de entidades

    // Cargamos las provincias
    List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

    // Mostrar las prestadoras
    List<Prestadora> prestadoras = this.repositorioPrestadoras.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("prestadoras", prestadoras);
    model.put("provincias", provincias);

    cargarRolesAModel(context, model);
    context.render("creacionEntidades.hbs", model);
  }

  public void save(Context context) {

    Entidad entidad = new Entidad();
    this.asignarParametros(entidad, context);

    this.repositorioEntidades.agregar(entidad);

    context.status(HttpStatus.CREATED);

    // Cargamos las provincias
    List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

    // Mostrar las prestadoras
    List<Prestadora> prestadoras = this.repositorioPrestadoras.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("creacion_entidad", "creacion_entidad");

    cargarRolesAModel(context, model);
    context.render("confirmacion.hbs", model);
  }

  public void edit(Context context) {

    // Buscamos la entidad en la DB
    long entidadID = Long.parseLong(context.pathParam("id"));

    Entidad entidad = this.repositorioEntidades.buscarPorId(entidadID);

    // Cargamos las provincias
    List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

    // Mostrar las prestadoras
    List<Prestadora> prestadoras = this.repositorioPrestadoras.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("prestadoras", prestadoras);
    model.put("provincias", provincias);
    model.put("entidad", entidad);

    cargarRolesAModel(context, model);
    context.render("creacionEntidades.hbs", model);
  }

  public void update(Context context) {

    long entidadId = Long.parseLong(context.pathParam("id"));
    Entidad entidad = this.repositorioEntidades.buscarPorId(entidadId);

    entidad.setNombre(context.formParam("nombre"));
    entidad.setDescripcion(context.formParam("descripcion"));

    long idProvincia = Long.parseLong(context.formParam("provincia"));
    long idDepartamento = Long.parseLong(context.formParam("departamento") == null ? "-1" : context.formParam("departamento"));
    long idMunicipio = Long.parseLong(context.formParam("municipio") == null ? "-1" : context.formParam("municipio"));


    // Buscamos en el repo de localizaciones si ya existe una localizacion que matchee la combinacion

    Provincia provincia = this.repositorioProvincias.buscarPorId(idProvincia);
    Departamento departamento = this.repositorioDepartamentos.buscarPorId(idDepartamento);
    Municipio municipio = this.repositorioMunicipios.buscarPorId(idMunicipio);

    Localizacion localizacion = this.repositorioLocalizaciones.buscarPorCombinacion(provincia, municipio, departamento);

    // En el caso de que no exista, la instanciamos
    if(localizacion == null) {
      localizacion = new Localizacion(provincia, departamento, municipio);
      this.repositorioLocalizaciones.agregar(localizacion);
    }

    //Localizacion localizacion = new Localizacion(provincia, departamento, municipio);

    // En cualquier caso le asignamos la localizacion a la entidad

    entidad.setLocalizacion(localizacion);

    Long idPrestadora = Long.parseLong(context.formParam("prestadora"));

    Prestadora prestadora = this.repositorioPrestadoras.buscarPorId(idPrestadora);

    entidad.setPrestadora(prestadora);


    this.repositorioEntidades.actualizar(entidad);

    //TODO: chequear esto

    // Cargamos el pop-up

    // Cargamos las provincias
    List<Provincia> provincias = this.repositorioProvincias.buscarTodos();

    // Mostrar las prestadoras
    List<Prestadora> prestadoras = this.repositorioPrestadoras.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("prestadoras", prestadoras);
    model.put("provincias", provincias);
    model.put("edit-success","edit-success");

    cargarRolesAModel(context, model);

    model.put("edicion_entidad", "edicion_entidad");
    context.render("confirmacion.hbs", model);
  }


  public void delete(Context context) {

    // Eliminar la entidad de la base de datos

    // Buscamos la entidad en la DB
    long entidadId = Long.parseLong(context.formParam("id"));
    Entidad entidad = this.repositorioEntidades.buscarPorId(entidadId);


    entidad.setDeleted(true);

    this.repositorioEntidades.actualizar(entidad);



    Map<String, Object> model = new HashMap<>();
    model.put("eliminacion_entidad", "eliminacion_entidad");
    model.put("entidad", entidad);

    cargarRolesAModel(context, model);
    context.render("confirmacion.hbs", model);
  }


  private void asignarParametros(Entidad entidad, Context context) {
    if(!Objects.equals(context.formParam("entidadesForm"), "")) {

      long idProvincia = Long.parseLong(context.formParam("provincia"));
      long idDepartamento = Long.parseLong(context.formParam("departamento") == null ? "-1" : context.formParam("departamento"));
      long idMunicipio = Long.parseLong(context.formParam("municipio") == null ? "-1" : context.formParam("municipio"));


      // Buscamos en el repo de localizaciones si ya existe una localizacion que matchee la combinacion

      Provincia provincia = this.repositorioProvincias.buscarPorId(idProvincia);
      Departamento departamento = this.repositorioDepartamentos.buscarPorId(idDepartamento);
      Municipio municipio = this.repositorioMunicipios.buscarPorId(idMunicipio);

      Localizacion localizacion = this.repositorioLocalizaciones.buscarPorCombinacion(provincia, municipio, departamento);

      // En el caso de que no exista, la instanciamos
      if(localizacion == null) {
        localizacion = new Localizacion(provincia, departamento, municipio);
        this.repositorioLocalizaciones.agregar(localizacion);
      }

      //Localizacion localizacion = new Localizacion(provincia, departamento, municipio);

      // En cualquier caso le asignamos la localizacion a la entidad


      entidad.setLocalizacion(localizacion);
      entidad.setNombre(context.formParam("nombre"));
      entidad.setDescripcion(context.formParam("descripcion"));

      Long idPrestadora = Long.parseLong(context.formParam("prestadora"));

      Prestadora prestadora = this.repositorioPrestadoras.buscarPorId(idPrestadora);

      entidad.setPrestadora(prestadora);

      entidad.setDeleted(false);
    }
  }

  public void showCargaPrestadorasYOrganismos(Context context) {
    // Muestra la pantalla de carga masiva

    Map<String, Object> model = new HashMap<>();
    model.put("cargaDePrestadorasYOrganismos", true);
    cargarRolesAModel(context, model);
    context.render("cargaMasiva.hbs", model);
  }

  public void showCargaEntidadesYEstablecimientos(Context context) {
    // Muestra la pantalla de carga masiva

    Map<String, Object> model = new HashMap<>();
    model.put("cargaDePrestadorasYOrganismos", false);

    cargarRolesAModel(context, model);
    context.render("cargaMasiva.hbs", model);
  }

  public void saveCargaPrestadorasYOrganismos(Context context) {
    // Obtiene el nombre del archivo seleccionado
    String fileName = context.uploadedFile("fileInput").filename();

    // Obtiene el contenido del archivo cargado
    InputStream fileContent = context.uploadedFile("fileInput").content();

    LectorCSV lector = new LectorCSV();
    List<Organismo> organismos;

    // Procesa el contenido del archivo
    try {
      organismos = lector.leerCSVPrestadorasYOrganismos(fileContent, repositorioRoles);
      fileContent.close();

      for (Organismo organismo : organismos) {
        this.repositorioOrganismos.agregar(organismo);
      }
    } catch (IOException | CsvValidationException e) {
      //e.printStackTrace();
      //throw new RuntimeException(e);
    }

    Map<String, Object> model = new HashMap<>();
    model.put("carga_prestadoras_organismos", "carga_prestadoras_organismos");

    cargarRolesAModel(context, model);
    context.render("confirmacion.hbs", model);
  }
}