package org.example;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.Servicio.PrestacionDeServicio;
import models.entities.Servicio2.entities.CalculadorDePuntajes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Servicio2Test {
    @Test
    @DisplayName("Miembros - Integracion con el servicio 2 Test")
    public void testServicio2Miembro() throws IOException {
        Miembro miembro1 = new Miembro();
        miembro1.setIdMiembro(1L);
        miembro1.setPuntaje(3.0);

        PrestacionDeServicio prestacion1 = new PrestacionDeServicio();
        prestacion1.setIdPrestacion(10L);

        EstadoPorComunidad estado1 = new EstadoPorComunidad();
        estado1.setFechaDeCierre(LocalDateTime.of(2023,Month.OCTOBER,6,1,1));
        estado1.setCerrador(miembro1);
        List<EstadoPorComunidad> estados = new ArrayList<>();
        estados.add(estado1);

        Incidente incidente1 = new Incidente();
        incidente1.setIdIncidente(1L);
        incidente1.setTiempoInicial(LocalDateTime.of(2023, Month.OCTOBER,5,1,1));
        incidente1.setPrestacionAfectada(prestacion1);
        incidente1.setMiembroCreador(miembro1);
        incidente1.setEstados(estados);
        List<Incidente> incidentes = new ArrayList<>();
        incidentes.add(incidente1);

        CalculadorDePuntajes calculadorDePuntajes = new CalculadorDePuntajes();
        Double resultado = calculadorDePuntajes.calcularPuntajeMiembro(miembro1, incidentes);
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
