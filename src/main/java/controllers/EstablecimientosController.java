package controllers;


import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.repositories.RepositorioEntidades;
import models.repositories.RepositorioEstablecimientos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EstablecimientosController {

  private RepositorioEntidades repositorioEntidades;
  private RepositorioEstablecimientos repositorioEstablecimientos;

  public EstablecimientosController(RepositorioEntidades repositorioEntidades, RepositorioEstablecimientos repositorioEstablecimientos) {
    this.repositorioEntidades = repositorioEntidades;
    this.repositorioEstablecimientos = repositorioEstablecimientos;

  }

  public void create(Context context) {
    // Pantalla de creacion de establecimientos

    // Mostrar las entidades posibles
    List<Entidad> entidades = this.repositorioEntidades.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("entidades", entidades);

    context.render("creacionEstablecimientos.hbs", model);
  }


  public void save(Context context) {

    Establecimiento establecimiento = new Establecimiento();
    this.asignarParametros(establecimiento, context);

    this.repositorioEstablecimientos.agregar(establecimiento);

    context.status(HttpStatus.CREATED);

    Map<String, Object> model = new HashMap<>();
    model.put("creacion_establecimiento", "creacion_establecimiento");
    context.render("confirmacion.hbs", model);
  }

  private void asignarParametros(Establecimiento establecimiento, Context context) {

    if (!Objects.equals(context.formParam("entidadesForm"), "")) {

      establecimiento.setNombre(context.formParam("nombre"));

      Long idEntidad = Long.parseLong(context.formParam("entidad"));

      Entidad entidad = this.repositorioEntidades.buscarPorId(idEntidad);


      establecimiento.setEntidad(entidad);
      entidad.addEstablecimiento(establecimiento);

    }
  }
}



  //@SuppressWarnings("unchecked")
/*  public void saveCargaEntidadesYEstablecimientos(Context context) {
    // Obtiene el nombre del archivo seleccionado
    String fileName = context.uploadedFile("fileInput").filename();

    // Obtiene el contenido del archivo cargado
    InputStream fileContent = context.uploadedFile("fileInput").content();

    LectorCSV lector = new LectorCSV();
    List<Entidad> entidad;

    // Procesa el contenido del archivo
    try {
      entidad = lector.leerCSVEntidadesYEstablecimientos(fileContent);
      fileContent.close();

      for (Entidad entidad : entidad) {
        this.repositorioOrganismos.agregar(entidad);
      }
    } catch (IOException | CsvValidationException e) {
      //e.printStackTrace();
      //throw new RuntimeException(e);
    }

    context.redirect("/home");
  }*/

