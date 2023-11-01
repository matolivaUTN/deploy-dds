package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Servicio.Banio;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.Servicio1.entities.ComunidadServicio1;
import models.entities.Servicio1.entities.FusionadorDeComunidades;
import models.entities.Servicio1.entities.PropuestaServicio1;
import models.entities.Servicio1.entities.RequestPropuestasComunidad;
import models.entities.Servicio1.entities.RequestSugerenciasFusion;
import models.entities.Servicio1.entities.RespuestaFusionComunidades;
import models.entities.Servicio1.entities.RespuestaPropuestaFusion;
import models.entities.Servicio2.entities.CalculadorDePuntajes;
import models.entities.ServicioPublico.Establecimiento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Servicio1Test {
  @Test
  @DisplayName("Sugerencias - Integracion con el servicio 1 Test")
  public void testServicio1Sugerencia() throws IOException {
    Miembro miembro1 = new Miembro();
    miembro1.setIdMiembro(1L);
    miembro1.setPuntaje(3.0);
    Miembro miembro2 = new Miembro();
    miembro2.setIdMiembro(2L);
    miembro2.setPuntaje(1.0);
    Miembro miembro3 = new Miembro();
    miembro3.setIdMiembro(3L);
    miembro3.setPuntaje(2.5);

    PrestacionDeServicio prestacionDeServicio = new PrestacionDeServicio();
    prestacionDeServicio.setIdPrestacion(1L);

    Banio banio = new Banio();
    banio.setIdServicio(1L);

    prestacionDeServicio.setServicio(banio);

    Establecimiento establecimiento = new Establecimiento();
    establecimiento.setIdEstablecimiento(1L);

    banio.setEstablecimiento(establecimiento);

    Comunidad comunidad1 = new Comunidad("comunidad prueba1", "comunidad de prueba1");
    comunidad1.setPuntaje(3.0);
    comunidad1.setIdComunidad(1L);
    comunidad1.agregarMiembros(miembro1,miembro2);
    comunidad1.agregarPrestacion(prestacionDeServicio);

    Comunidad comunidad2 = new Comunidad("comunidad prueba2", "comunidad de prueba2");
    comunidad2.setPuntaje(3.0);
    comunidad2.setIdComunidad(2L);
    comunidad2.agregarMiembros(miembro1,miembro3);
    comunidad2.agregarPrestacion(prestacionDeServicio);

    List<Comunidad> comunidades = new ArrayList<>();
    comunidades.add(comunidad1);
    comunidades.add(comunidad2);

    List<List<Long>> propuestasAExcluir = new ArrayList<>();
    propuestasAExcluir.add(Collections.singletonList(99L));

    FusionadorDeComunidades fusionadorDeComunidades = new FusionadorDeComunidades();

    RespuestaPropuestaFusion rta = fusionadorDeComunidades.crearPropuestas(comunidades, propuestasAExcluir);

    for (PropuestaServicio1 propuesta : rta.getPropuestas()) {
      System.out.println("Propuesta de Fusión:");
      for (ComunidadServicio1 comunidadId : propuesta.getComunidades()) {
        System.out.println("  Comunidad ID: " + comunidadId.getId());
      }
    }

  }
  @Test
  @DisplayName("Fusiones - Integracion con el servicio 1 Test")
  public void testServicio1Fusion() throws IOException{
    Miembro miembro1 = new Miembro();
    miembro1.setIdMiembro(1L);
    miembro1.setPuntaje(3.0);
    Miembro miembro2 = new Miembro();
    miembro2.setIdMiembro(2L);
    miembro2.setPuntaje(1.0);
    Miembro miembro3 = new Miembro();
    miembro3.setIdMiembro(3L);
    miembro3.setPuntaje(2.5);

    PrestacionDeServicio prestacionDeServicio = new PrestacionDeServicio();
    prestacionDeServicio.setIdPrestacion(1L);

    Banio banio = new Banio();
    banio.setIdServicio(1L);

    prestacionDeServicio.setServicio(banio);

    Establecimiento establecimiento = new Establecimiento();
    establecimiento.setIdEstablecimiento(1L);

    banio.setEstablecimiento(establecimiento);

    Comunidad comunidad1 = new Comunidad("comunidad prueba1", "comunidad de prueba1");
    comunidad1.setPuntaje(3.0);
    comunidad1.setIdComunidad(1L);
    comunidad1.agregarMiembros(miembro1,miembro2);
    comunidad1.agregarPrestacion(prestacionDeServicio);

    Comunidad comunidad2 = new Comunidad("comunidad prueba2", "comunidad de prueba2");
    comunidad2.setPuntaje(3.0);
    comunidad2.setIdComunidad(2L);
    comunidad2.agregarMiembros(miembro1,miembro3);
    comunidad2.agregarPrestacion(prestacionDeServicio);

    List<Comunidad> comunidades = new ArrayList<>();
    comunidades.add(comunidad1);
    comunidades.add(comunidad2);

    List<List<Long>> propuestasAExcluir = new ArrayList<>();
    propuestasAExcluir.add(Collections.singletonList(99L));

    FusionadorDeComunidades fusionadorDeComunidades = new FusionadorDeComunidades();

    RespuestaPropuestaFusion rta = fusionadorDeComunidades.crearPropuestas(comunidades, propuestasAExcluir);

    RequestPropuestasComunidad requestPropuestasComunidad = new RequestPropuestasComunidad();
    requestPropuestasComunidad.setPropuestas(rta.getPropuestas());

    RespuestaFusionComunidades respuesta = fusionadorDeComunidades.fusionarComunidades(requestPropuestasComunidad);
    
    for(ComunidadServicio1 comunidad:respuesta.getFusiones()){
      System.out.println("Nueva Comunidad");
      for(Integer miembroId:comunidad.getMiembros()){
        System.out.println("Miembro: "+ miembroId);
      }
    }


  }

}
