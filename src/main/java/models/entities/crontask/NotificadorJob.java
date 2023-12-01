package models.entities.crontask;
import models.entities.comunidad.Miembro;
import models.entities.incidente.Incidente;
import models.entities.notificaciones.EnviadorDeNotificaciones;
import models.entities.notificaciones.NotificacionPorMiembro;
import models.repositories.RepositorioNotificacion;
import models.repositories.RepositorioNotificacionPorMiembro;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import server.App;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NotificadorJob implements Job {
    public NotificadorJob() {}

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // Buscamos todas las notificaciones que tengamos persistidas y que no fueron enviadas
        List<NotificacionPorMiembro> notificacionesMiembros = new RepositorioNotificacionPorMiembro(App.entityManager()).buscarSinEnviar();

        // De las notificaciones por miembro que no fueron enviadas, nos quedamos con aquellas en donde uno de los horarios preferidos coincidan con la hora actual
        List<NotificacionPorMiembro> notificacionesMiembrosQueDeberianMandarse = notificacionesMiembros.stream().filter(n ->
                n.getMiembro().getHorariosDePreferencia().stream().anyMatch(unHorario -> Duration.between(unHorario, LocalTime.now()).toMinutes() == 0)
        ).toList();

        // Chequeamos que para esas notifiaciones, hayan pasado menos de 24 horas desde la apertura de sus respectivos incidentes
        // Duration.between(unTiempoInicial, otroTiempoInicial).toHours() < 24;


        // Instanciamos a nuestro enviador de notificaciones
        EnviadorDeNotificaciones enviadorDeNotificaciones = new EnviadorDeNotificaciones();

        // Enviamos las notificaciones (con lista harcodeada de un elemento
        for(NotificacionPorMiembro n : notificacionesMiembrosQueDeberianMandarse) {
            List<Miembro> miembroAEnviar = new ArrayList<>();
            miembroAEnviar.add(n.getMiembro());

            enviadorDeNotificaciones.enviarNotificacion(n.getNotificacion(), miembroAEnviar);
            n.setFueEnviada(true);
        }
    }
}
