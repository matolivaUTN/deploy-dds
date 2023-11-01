package org.example;

import models.entities.Comunidad.Miembro;

import models.entities.Notificaciones.AdapterAngusMail;
import models.entities.Notificaciones.AdapterTwilio;
import models.entities.Notificaciones.NotificadorWhatsapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.mockito.Mockito.*;

public class NotificadorTest {
    @Test
    @DisplayName("Notificacion Email")
    public void testNotificacionEmail(){
        Miembro miembro = new Miembro();
        AdapterAngusMail adapterAngusMail = new AdapterAngusMail();
        adapterAngusMail.enviarEmail(miembro,"email de prueba");
    }

    @Test
    @DisplayName("Notificacion Whatsapp")
    public void testNotificacionWhatsapp(){
        Miembro miembro = new Miembro();
        AdapterTwilio adapterTwilio = mock(AdapterTwilio.class);
        NotificadorWhatsapp notificadorWhatsapp = new NotificadorWhatsapp(adapterTwilio);
        miembro.setNotificador(notificadorWhatsapp);
        when(adapterTwilio.enviarWhatsapp(miembro,"wsp de prueba")).thenReturn(true);
        Assertions.assertTrue(notificadorWhatsapp.enviarNotificacion(miembro,"wsp de prueba"));
    }

}
