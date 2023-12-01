package models.entities.crontask;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

abstract public class Cron {
    public String periodicity;

    public Cron() {
        this.periodicity = "0 * * * * ?";
    }

    public void start() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Starteamos el scheduler
        scheduler.start();

        // Define un trigger que lo va a ejecutar cada 1 minuto
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(periodicity)) // Run every minute
                .build();

        // Scheduleamos el job con el trigger
        scheduler.scheduleJob(job(), trigger);

        // You can uncomment the following line to stop the scheduler after a specific time
        // scheduler.shutdown();
    }

    // Definimos el job y lo atamos al Job específico (este último tiene la lógica de lo que hace el Cron)
    abstract public JobDetail job();
}