package models.entities.crontask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class NotificadorCron {
    public void start() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Starteamos el scheduler
        scheduler.start();

        // Definimos el job y lo atamos al NotificadorJob (este ultimo tiene la logica de lo que hace el Cron)
        JobDetail job = JobBuilder.newJob(NotificadorJob.class)
                .withIdentity("notificadorJob", "group1")
                .build();

        // Define un trigger que lo va a ejecutar cada 1 minuto
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // Run every minute
                .build();

        // Scheduleamos el job con el trigger
        scheduler.scheduleJob(job, trigger);

        // You can uncomment the following line to stop the scheduler after a specific time
        // scheduler.shutdown();
    }
}
