package controllers;

import io.javalin.http.Context;

import java.io.IOException;
import java.util.List;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;
import models.entities.ServicioCalculoGradoDeConfianza.entities.CalculadorDePuntajes;
import models.repositories.RepositorioComunidades;
import models.repositories.RepositorioIncidentes;
import models.repositories.RepositorioMiembros;

public class ServicioCalculoGradoDeConfianzaController {
  private RepositorioMiembros repositorioMiembros;
  private RepositorioComunidades repositorioComunidades;

  private RepositorioIncidentes repositorioIncidentes;

  CalculadorDePuntajes calculadorDePuntajes;
  public ServicioCalculoGradoDeConfianzaController(RepositorioMiembros repositorioMiembros, RepositorioComunidades repositorioComunidades, RepositorioIncidentes repositorioIncidentes){
    this.repositorioMiembros = repositorioMiembros;
    this.repositorioComunidades = repositorioComunidades;
    this.repositorioIncidentes = repositorioIncidentes;
  }

  public void actualizarPuntajeMiembros(Context context) throws IOException {
    List<Miembro> miembros = this.repositorioMiembros.buscarTodos();
    List<Incidente> incidentes = this.repositorioIncidentes.buscarTodos();

    for(Miembro miembro : miembros){
      Double puntaje = this.calculadorDePuntajes.calcularPuntajeMiembro(miembro, incidentes);
      miembro.setPuntaje(puntaje);
      this.repositorioMiembros.actualizar(miembro);
    }
  }

  public void actualizarPuntajeComunidad(Context context) throws IOException {
    List<Comunidad> comunidades = this.repositorioComunidades.buscarTodos();

    for(Comunidad comunidad : comunidades){
      Double puntaje = this.calculadorDePuntajes.calcularPuntajeComunidad(comunidad);
      comunidad.setPuntaje(puntaje);
      this.repositorioComunidades.actualizar(comunidad);
    }
  }
}
