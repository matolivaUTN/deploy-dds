package controllers;


import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.localizacion.Localizacion;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;
import models.repositories.*;

import java.util.*;

public class EstablecimientosController extends Controller {

  private RepositorioEntidades repositorioEntidades;
  private RepositorioEstablecimientos repositorioEstablecimientos;
  private RepositorioLocalizaciones repositorioLocalizaciones;
  private RepositorioProvincias repoProvincias;
  private RepositorioDepartamentos repoDeptos;
  private RepositorioMunicipios repoMunis;

  public EstablecimientosController(RepositorioEntidades repositorioEntidades, RepositorioEstablecimientos repositorioEstablecimientos, RepositorioLocalizaciones repositorioLocalizaciones, RepositorioProvincias repoProvincias, RepositorioDepartamentos repoDeptos, RepositorioMunicipios repoMunis) {
    this.repositorioEntidades = repositorioEntidades;
    this.repositorioEstablecimientos = repositorioEstablecimientos;
    this.repositorioLocalizaciones = repositorioLocalizaciones;
    this.repoProvincias = repoProvincias;
    this.repoDeptos = repoDeptos;
    this.repoMunis = repoMunis;
  }

  public void create(Context context) {
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
        muni = repoMunis.buscarPorId(Long.parseLong(context.formParam("municipio")));
      }
      else if(context.formParam("departamento") != null) {
        depto = repoDeptos.buscarPorId(Long.parseLong(context.formParam("departamento")));
      }

      Localizacion localizacionEstablecimiento = repositorioLocalizaciones.buscarPorCombinacion(provincia, muni, depto);


      establecimiento.setEntidad(entidad);
      entidad.addEstablecimiento(establecimiento);
      establecimiento.setLocalizacion(localizacionEstablecimiento);
      establecimiento.setDeleted(false);
    }
  }
}