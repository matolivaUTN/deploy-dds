package controllers;

import com.opencsv.exceptions.CsvValidationException;
import io.javalin.http.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.javalin.http.HttpStatus;
import models.entities.CSV.LectorCSV;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Organismo;
import models.entities.ServicioPublico.Prestadora;
import models.repositories.RepositorioEntidades;
import models.repositories.RepositorioOrganismos;
import models.repositories.RepositorioPrestadoras;
import server.utils.ICrudViewsHandler;

public class EntidadesController {
  private RepositorioOrganismos repositorioOrganismos;
  private RepositorioEntidades repositorioEntidades;
  private RepositorioPrestadoras repositorioPrestadoras;


  public EntidadesController(RepositorioEntidades repositorioEntidades, RepositorioOrganismos repositorioOrganismos, RepositorioPrestadoras repositorioPrestadoras) {
    this.repositorioEntidades = repositorioEntidades;
    this.repositorioOrganismos = repositorioOrganismos;
    this.repositorioPrestadoras = repositorioPrestadoras;
  }

  public void create(Context context) {
    // Pantalla de creacion de entidades

    // Mostrar las prestadoras
    List<Prestadora> prestadoras = this.repositorioPrestadoras.buscarTodos();

    Map<String, Object> model = new HashMap<>();
    model.put("prestadoras", prestadoras);

    context.render("creacionEntidades.hbs", model);
  }

  public void save(Context context) {

    Entidad entidad = new Entidad();
    this.asignarParametros(entidad, context);

    this.repositorioEntidades.agregar(entidad);

    context.status(HttpStatus.CREATED);

    context.redirect("/entidades/crear");
  }

  private void asignarParametros(Entidad entidad, Context context) {

    if(!Objects.equals(context.formParam("entidadesForm"), "")) {

      entidad.setNombre(context.formParam("nombre"));
      entidad.setDescripcion(context.formParam("descripcion"));

      Long idPrestadora = Long.parseLong(context.formParam("prestadora"));

      Prestadora prestadora = this.repositorioPrestadoras.buscarPorId(idPrestadora);

      entidad.setPrestadora(prestadora);


      //TODO: prestadora y localizacion

    }
  }









  public void showCargaPrestadorasYOrganismos(Context context) {
    // Muestra la pantalla de carga masiva

    Map<String, Object> model = new HashMap<>();
    model.put("cargaDePrestadorasYOrganismos", true);
    context.render("cargaMasiva.hbs", model);
  }

  public void showCargaEntidadesYEstablecimientos(Context context) {
    // Muestra la pantalla de carga masiva

    Map<String, Object> model = new HashMap<>();
    model.put("cargaDePrestadorasYOrganismos", false);
    context.render("cargaMasiva.hbs", model);
  }


  @SuppressWarnings("unchecked")
  public void saveCargaPrestadorasYOrganismos(Context context) {
    // Obtiene el nombre del archivo seleccionado
    String fileName = context.uploadedFile("fileInput").filename();

    // Obtiene el contenido del archivo cargado
    InputStream fileContent = context.uploadedFile("fileInput").content();

    LectorCSV lector = new LectorCSV();
    List<Organismo> organismos;

    // Procesa el contenido del archivo
    try {
      organismos = lector.leerCSVPrestadorasYOrganismos(fileContent);
      fileContent.close();

      for (Organismo organismo : organismos) {
        this.repositorioOrganismos.agregar(organismo);
      }
    } catch (IOException | CsvValidationException e) {
      //e.printStackTrace();
      //throw new RuntimeException(e);
    }

    context.redirect("/home");
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

