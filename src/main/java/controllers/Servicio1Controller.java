package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.Servicio1.entities.ComunidadServicio1;
import models.entities.Servicio1.entities.FusionadorDeComunidades;
import models.entities.Servicio1.entities.PrestacionDeServicioServicio1;
import models.entities.Servicio1.entities.PropuestaServicio1;
import models.entities.Servicio1.entities.RequestPropuestasComunidad;
import models.entities.Servicio1.entities.RespuestaFusionComunidades;
import models.entities.Servicio1.entities.RespuestaPropuestaFusion;
import models.repositories.RepositorioComunidades;
import io.javalin.http.Context;
import models.repositories.RepositorioMiembros;
import models.repositories.RepositorioPrestacionesDeServicios;

public class Servicio1Controller {
  private RepositorioComunidades repositorioComunidades;
  private RepositorioMiembros repositorioMiembros;

  private RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios;

  FusionadorDeComunidades fusionadorDeComunidades;

  public Servicio1Controller(RepositorioComunidades repositorioComunidades, RepositorioMiembros repositorioMiembros, RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios){
    this.repositorioComunidades = repositorioComunidades;
    this.repositorioMiembros = repositorioMiembros;
    this.repositorioPrestacionesDeServicios = repositorioPrestacionesDeServicios;
  }

  public void crearPropuestas(Context context) throws IOException {
    List<Comunidad> comunidades = this.repositorioComunidades.buscarTodos();
    List<List<Long>> listaDeListas = new ArrayList<>();
    List<Long> listaInterna = new ArrayList<>();
    listaInterna.add(99L);
    listaDeListas.add(listaInterna);
    RespuestaPropuestaFusion rta = this.fusionadorDeComunidades.crearPropuestas(comunidades, listaDeListas);
  }

  public void fusionarComunidades(Context context, PropuestaServicio1 propuesta) throws IOException {


    List<PropuestaServicio1> propuestas = new ArrayList<>();
    propuestas.add(propuesta);
    RequestPropuestasComunidad requestPropuestasComunidad = new RequestPropuestasComunidad();
    requestPropuestasComunidad.setPropuestas(propuestas);
    RespuestaFusionComunidades respuesta = fusionadorDeComunidades.fusionarComunidades(requestPropuestasComunidad);

    for(ComunidadServicio1 comunidad : propuesta.getComunidades()){
      this.repositorioComunidades.deshabilitarComunidadPorId(comunidad.getId());
    }

    ComunidadServicio1 comunidad1 = respuesta.getFusiones().get(0);

    Comunidad comunidadFusionada = new Comunidad();
    comunidadFusionada.setEstaHabilitada(1);
    comunidadFusionada.setPuntaje(comunidad1.getGradoConfianza());
    List<Miembro> miembrosAFusionar = new ArrayList<>();
    for(Integer id : comunidad1.getMiembros()){
      miembrosAFusionar.add(this.repositorioMiembros.buscarPorId(id));
    }
    List<PrestacionDeServicio> prestacionesAFusionar = new ArrayList<>();
    comunidadFusionada.setMiembros(miembrosAFusionar);
    for(PrestacionDeServicioServicio1 prestacionDeServicioServicio1 : comunidad1.getPrestacionesDeServicio()){
      prestacionesAFusionar.add(this.repositorioPrestacionesDeServicios.buscarPorId(prestacionDeServicioServicio1.getId()));
    }
    comunidadFusionada.setPrestaciones(prestacionesAFusionar);

    this.repositorioComunidades.agregar(comunidadFusionada);

  }

}
