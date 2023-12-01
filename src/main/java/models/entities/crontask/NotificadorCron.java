package models.entities.crontask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class NotificadorCron extends Cron {
    @Override
    public JobDetail job() {
        return JobBuilder.newJob(NotificadorJob.class)
                .withIdentity("notificadorJob", "group1")
                .build();
    }
}
