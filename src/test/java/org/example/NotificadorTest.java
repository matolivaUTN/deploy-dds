package org.example;

import models.entities.comunidad.Miembro;

import models.entities.notificaciones.AdapterAngusMail;
import models.entities.notificaciones.AdapterTwilio;
import models.entities.notificaciones.Notificacion;
import models.entities.notificaciones.NotificadorWhatsapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.mockito.Mockito.*;

public class NotificadorTest {
    @Test
    @DisplayName("Notificacion Email")
    public void testNotificacionEmail(){
        Miembro miembro = new Miembro();
        AdapterAngusMail adapterAngusMail = new AdapterAngusMail();
        Notificacion notificacion = new Notificacion();
        notificacion.setContenido("email de prueba");
        adapterAngusMail.enviarEmail(miembro,notificacion);
    }

    @Test
    @DisplayName("Notificacion Whatsapp")
    public void testNotificacionWhatsapp(){
        Miembro miembro = new Miembro();
        AdapterTwilio adapterTwilio = mock(AdapterTwilio.class);
        NotificadorWhatsapp notificadorWhatsapp = new NotificadorWhatsapp(adapterTwilio);
        miembro.setNotificador(notificadorWhatsapp);
        //when(adapterTwilio.enviarWhatsapp(miembro,"wsp de prueba")).thenReturn(true);
        //Assertions.assertTrue(notificadorWhatsapp.enviarNotificacion(miembro,"wsp de prueba"));
    }

}
