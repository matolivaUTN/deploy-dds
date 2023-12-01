package org.example;

import models.entities.comunidad.Comunidad;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;


import models.entities.servicio.PrestacionDeServicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private Miembro franco;
    private Miembro tomy;
    private Miembro joaco;

    private Comunidad usuariosSubteB;
    private Comunidad usuariosSubteA;
    //private MedioDeElevacion ascensorSubteB;
    private PrestacionDeServicio prestacionAscensor;
    private PrestacionDeServicio prestacionEscalera;

    private Incidente incidenteAscensor;
    private Incidente incidenteEscalera;

    @BeforeEach
    public void setUpTestIncidentes() {
        franco = new Miembro();
        tomy = new Miembro();
        joaco = new Miembro();

        //usuariosSubteB = new Comunidad("Usuarios Subte B", "Comunidad que defiende los intereses de los usuarios del subte B");
        //usuariosSubteA = new Comunidad("Usuarios Subte A", "Comunidad que defiende los intereses de los usuarios del subte A");

        //usuariosSubteB.agregarMiembros(franco, tomy);
        //usuariosSubteA.agregarMiembros(joaco, tomy);

        //ascensorSubteB = new MedioDeElevacion();
        //prestacionAscensor = new PrestacionDeServicio(ascensorSubteB, true);
        //prestacionEscalera = new PrestacionDeServicio(ascensorSubteB, true);

        // Creamos un par de incidentes
        //incidenteAscensor = franco.abrirIncidente("No funciona el ascensor de la estacion Medrano", prestacionAscensor,
         //       usuariosSubteB);

        //incidenteEscalera = tomy.abrirIncidente("No funciona la escalera de la estacion Medrano", prestacionEscalera,
          //      usuariosSubteB);
    }



    @BeforeEach
    public void setUpTestRolMiembros() {
        // Franco y Tomy tienen movilidad reducida. Joaco esta completamente sano.
        // Mientras que Franco se esguinzo el tobillo derecho, Tomy quedo paralitico

        franco = new Miembro();
        tomy = new Miembro();


        // Tengo dudas sobre el seteo inicial de la lista de roles en cada usuario
        // Capaz lo tiene que hacer la comunidad cada vez que se instancia una prestacion????
        // No se si esta bien planteada la estrategia de que cada usuario tenga una lista de roles por prestacion





        // Entonces estos dos usuarios se ven afectados para la prestacion de la escalera mecanica de la estacion Medrano del subte B



    }

    @Test
    @DisplayName("Roles de miembros respecto a prestaciones de servicios")
    public void testRolMiembros() {








    }

    @Test
    @DisplayName("Cambio de rol de miembros respecto a prestaciones de servicios")
    public void testCambioRolMiembros() {

        // Franco se recupero de su lesion, por lo que ya no se ve afectado para la prestacion de la escalera mecanica

        // Y como sigue estando en la comunidad, ahora pasara a ser observador





    }
}