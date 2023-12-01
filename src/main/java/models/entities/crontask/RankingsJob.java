package models.entities.crontask;
import models.repositories.RepositorioNotificacion;
import models.repositories.RepositorioNotificacionPorMiembro;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RankingsJob implements Job {

    private RepositorioNotificacion repositorioNotificacion;
    private RepositorioNotificacionPorMiembro repositorioNotificacionPorMiembro;

    public RankingsJob(RepositorioNotificacion repositorioNotificacion, RepositorioNotificacionPorMiembro repositorioNotificacionPorMiembro) {
        this.repositorioNotificacion = repositorioNotificacion;
        this.repositorioNotificacionPorMiembro = repositorioNotificacionPorMiembro;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //TODO

    }

}
