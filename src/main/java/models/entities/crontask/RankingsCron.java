package models.entities.crontask;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;

public class RankingsCron extends Cron {
    public RankingsCron() {
        this.periodicity = "0 */5 * * * ?";
    }

    @Override
    public JobDetail job() {
        return JobBuilder.newJob(RankingsJob.class)
                .withIdentity("rankingsJob", "group1")
                .build();
    }
}
