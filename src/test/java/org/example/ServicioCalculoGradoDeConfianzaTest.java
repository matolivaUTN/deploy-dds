package org.example;
import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;
import models.entities.servicio.PrestacionDeServicio;
import models.entities.ServicioCalculoGradoDeConfianza.entities.CalculadorDePuntajes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.time.Month;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServicioCalculoGradoDeConfianzaTest {
    @Test
    @DisplayName("Miembros - Integracion con el servicio 2 Test")
    public void testServicio2Miembro() throws IOException {
        Miembro miembro = new Miembro();
        miembro.setIdMiembro(1L);
        miembro.setPuntaje(3.0);

        PrestacionDeServicio prestacion = new PrestacionDeServicio();
        prestacion.setIdPrestacion(10L);

        Incidente incidente = new Incidente();
        incidente.setFechaDeCierre(LocalDateTime.of(2023,Month.OCTOBER,6,1,1));
        incidente.setIdIncidente(1L);
        incidente.setFechaDeApertura(LocalDateTime.of(2023, Month.OCTOBER,5,1,1));
        incidente.setPrestacionAfectada(prestacion);
        incidente.setMiembroCreador(miembro);
        incidente.setCerrador(miembro);

        List<Incidente> incidentes = new ArrayList<>();
        incidentes.add(incidente);

        CalculadorDePuntajes calculadorDePuntajes = new CalculadorDePuntajes();
        Double resultado = calculadorDePuntajes.calcularPuntajeMiembro(miembro, incidentes);
        System.out.println("Resultado miembro = " + resultado);

    }

    @Test
    @DisplayName("Comunidades - Integracion con el servicio 2 Test")
    public void testServicio2Comunidad() throws IOException {
        Miembro miembro1 = new Miembro();
        miembro1.setIdMiembro(1L);
        miembro1.setPuntaje(3.0);
        Miembro miembro2 = new Miembro();
        miembro2.setIdMiembro(2L);
        miembro2.setPuntaje(1.0);
        Miembro miembro3 = new Miembro();
        miembro3.setIdMiembro(3L);
        miembro3.setPuntaje(2.0);
        Comunidad comunidad = new Comunidad("comunidad prueba", "comunidad de prueba");
        comunidad.agregarMiembros(miembro1,miembro2,miembro3);
        CalculadorDePuntajes calculadorDePuntajes = new CalculadorDePuntajes();
        Double resultado = calculadorDePuntajes.calcularPuntajeComunidad(comunidad);
        System.out.println("Resultado comunidad = " + resultado);
    }
}
