package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.servicio.PrestacionDeServicio;
import models.entities.ServicioFusionComunidades.entities.ComunidadServicioFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.FusionadorDeComunidades;
import models.entities.ServicioFusionComunidades.entities.PrestacionDeServicioServicioFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.PropuestaServicioFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.RequestPropuestasComunidad;
import models.entities.ServicioFusionComunidades.entities.RespuestaFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.RespuestaPropuestaFusion;
import models.repositories.RepositorioComunidades;
import io.javalin.http.Context;
import models.repositories.RepositorioMiembros;
import models.repositories.RepositorioPrestacionesDeServicios;

public class ServicioFusionComunidadesController {
  private RepositorioComunidades repositorioComunidades;
  private RepositorioMiembros repositorioMiembros;

  private RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios;

  FusionadorDeComunidades fusionadorDeComunidades;

  public ServicioFusionComunidadesController(RepositorioComunidades repositorioComunidades, RepositorioMiembros repositorioMiembros, RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios){
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

  public void fusionarComunidades(Context context, PropuestaServicioFusionComunidades propuesta) throws IOException {
    List<PropuestaServicioFusionComunidades> propuestas = new ArrayList<>();
    propuestas.add(propuesta);
    RequestPropuestasComunidad requestPropuestasComunidad = new RequestPropuestasComunidad();
    requestPropuestasComunidad.setPropuestas(propuestas);
    RespuestaFusionComunidades respuesta = fusionadorDeComunidades.fusionarComunidades(requestPropuestasComunidad);

    for(ComunidadServicioFusionComunidades comunidad : propuesta.getComunidades()){
      this.repositorioComunidades.deshabilitarComunidadPorId(comunidad.getId());
    }

    ComunidadServicioFusionComunidades comunidad1 = respuesta.getFusiones().get(0);

    Comunidad comunidadFusionada = new Comunidad();
    comunidadFusionada.setEstaHabilitada(1);
    comunidadFusionada.setPuntaje(comunidad1.getGradoConfianza());
    List<Miembro> miembrosAFusionar = new ArrayList<>();
    for(Integer id : comunidad1.getMiembros()){
      miembrosAFusionar.add(this.repositorioMiembros.buscarPorId(id));
    }
    List<PrestacionDeServicio> prestacionesAFusionar = new ArrayList<>();
    comunidadFusionada.setMiembros(miembrosAFusionar);
    for(PrestacionDeServicioServicioFusionComunidades prestacionDeServicioServicioFusionComunidades : comunidad1.getPrestacionesDeServicio()){
      prestacionesAFusionar.add(this.repositorioPrestacionesDeServicios.buscarPorId(prestacionDeServicioServicioFusionComunidades.getId()));
    }
    comunidadFusionada.setPrestaciones(prestacionesAFusionar);

    this.repositorioComunidades.agregar(comunidadFusionada);
  }
}
