package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.servicio.Banio;
import models.entities.servicio.PrestacionDeServicio;
import models.entities.ServicioFusionComunidades.entities.ComunidadServicioFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.FusionadorDeComunidades;
import models.entities.ServicioFusionComunidades.entities.PropuestaServicioFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.RequestPropuestasComunidad;
import models.entities.ServicioFusionComunidades.entities.RespuestaFusionComunidades;
import models.entities.ServicioFusionComunidades.entities.RespuestaPropuestaFusion;
import models.entities.ServicioPublico.Establecimiento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServicioFusionComunidadesTest {
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

    for (PropuestaServicioFusionComunidades propuesta : rta.getPropuestas()) {
      System.out.println("Propuesta de Fusión:");
      for (ComunidadServicioFusionComunidades comunidadId : propuesta.getComunidades()) {
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
    
    for(ComunidadServicioFusionComunidades comunidad:respuesta.getFusiones()){
      System.out.println("Nueva Comunidad");
      for(Integer miembroId:comunidad.getMiembros()){
        System.out.println("Miembro: "+ miembroId);
      }
    }


  }

}
