package controllers;


import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.ServicioPublico.Prestadora;
import models.entities.localizacion.Localizacion;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;
import models.repositories.*;
import server.App;

import java.util.*;

public class EstablecimientosController extends Controller {

  private RepositorioEntidades repositorioEntidades;
  private RepositorioEstablecimientos repositorioEstablecimientos;
  private RepositorioLocalizaciones repositorioLocalizaciones;
  private RepositorioProvincias repositorioProvincias;
  private RepositorioDepartamentos repositorioDepartamentos;
  private RepositorioMunicipios repositorioMunicipios;
  private RepositorioPrestadoras repositorioPrestadoras;

  public EstablecimientosController(RepositorioEntidades repositorioEntidades, RepositorioEstablecimientos repositorioEstablecimientos, RepositorioLocalizaciones repositorioLocalizaciones, RepositorioProvincias repositorioProvincias, RepositorioDepartamentos repositorioDepartamentos, RepositorioMunicipios repositorioMunicipios,RepositorioPrestadoras repositorioPrestadoras) {
    this.repositorioEntidades = repositorioEntidades;
    this.repositorioEstablecimientos = repositorioEstablecimientos;
    this.repositorioLocalizaciones = repositorioLocalizaciones;
    this.repositorioProvincias = repositorioProvincias;
    this.repositorioDepartamentos = repositorioDepartamentos;
    this.repositorioMunicipios = repositorioMunicipios;
    this.repositorioPrestadoras = repositorioPrestadoras;
  }

  public void index(Context context) {

    // Listado de establecimientos
    List<Establecimiento> establecimientos = this.repositorioEstablecimientos.buscarTodos();

    Map<String, Object> model = new HashMap<>();

    model.put("establecimientos", establecimientos);
    cargarRolesAModel(context, model);
    context.render("listadoEstablecimientos.hbs", model);
  }

  public void create(Context context) {

    App.entityManager().clear();
    // Pantalla de creacion de establecimientos

    // Mostrar las entidades posibles
    List<Entidad> entidades = this.repositorioEntidades.buscarTodos();


    Map<String, Object> model = new HashMap<>();
    model.put("entidades", entidades);

    cargarRolesAModel(context, model);
    context.render("creacionEstablecimientos.hbs", model);
  }

  public void save(Context context) {

    Establecimiento establecimiento = new Establecimiento();
    this.asignarParametros(establecimiento, context);

    this.repositorioEstablecimientos.agregar(establecimiento);

    context.status(HttpStatus.CREATED);

    Map<String, Object> model = new HashMap<>();
    model.put("creacion_establecimiento", "creacion_establecimiento");

    cargarRolesAModel(context, model);
    context.render("confirmacion.hbs", model);
  }



  public void edit(Context context) {

    // Buscamos el establecimiento en la DB
    long establecimientoID = Long.parseLong(context.pathParam("id"));

    Establecimiento establecimiento = this.repositorioEstablecimientos.buscarPorId(establecimientoID);

    // Cargamos los municipios
    List<Municipio> municipios = this.repositorioMunicipios.buscarTodos();

    // Cargamos los departamentos
    List<Departamento> departamentos = this.repositorioDepartamentos.buscarTodos();

    // Mostrar las entidades
    List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("entidades", entidades);
    model.put("departamentos", departamentos);
    model.put("municipios", municipios);
    model.put("establecimiento", establecimiento);

    cargarRolesAModel(context, model);
    context.render("creacionEstablecimientos.hbs", model);
  }

  public void update(Context context) {

    long establecimientoId = Long.parseLong(context.pathParam("id"));
    Establecimiento establecimiento = this.repositorioEstablecimientos.buscarPorId(establecimientoId);

    //entidad.setNombre(context.formParam("nombre"));
    //entidad.setDescripcion(context.formParam("descripcion"));

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

    establecimiento.setLocalizacion(localizacion);

    Long idPrestadora = Long.parseLong(context.formParam("prestadora"));

    Prestadora prestadora = this.repositorioPrestadoras.buscarPorId(idPrestadora);

    //entidad.setPrestadora(prestadora);


    //this.repositorioEntidades.actualizar(entidad);

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





  private void asignarParametros(Establecimiento establecimiento, Context context) {

    if (!Objects.equals(context.formParam("entidadesForm"), "")) {

      establecimiento.setNombre(context.formParam("nombre"));

      establecimiento.setPrestaciones(new ArrayList<>());

      Long idEntidad = Long.parseLong(context.formParam("entidad"));
      Entidad entidad = this.repositorioEntidades.buscarPorId(idEntidad);
      Localizacion localizacion = entidad.getLocalizacion();
      Provincia provincia = localizacion.getProvincia();

      Municipio muni = null;
      Departamento depto = null;
      if(localizacion.getMunicipio() != null) {
        muni = localizacion.getMunicipio();
      }
      else if(localizacion.getDepartamento() != null) {
        depto = localizacion.getDepartamento();
      }
      else if(context.formParam("municipio") != null) {
        muni = repositorioMunicipios.buscarPorId(Long.parseLong(context.formParam("municipio")));
      }
      else if(context.formParam("departamento") != null) {
        depto = repositorioDepartamentos.buscarPorId(Long.parseLong(context.formParam("departamento")));
      }

      Localizacion localizacionEstablecimiento = repositorioLocalizaciones.buscarPorCombinacion(provincia, muni, depto);


      establecimiento.setEntidad(entidad);
      entidad.addEstablecimiento(establecimiento);
      establecimiento.setLocalizacion(localizacionEstablecimiento);
      establecimiento.setDeleted(false);
    }
  }
}