package io.xstefank.scheduled;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import io.xstefank.model.yaml.Projects;
import org.jboss.logging.Logger;

public class BuildTriggerWatch {

    private Logger logger = Logger.getLogger(BuildTriggerWatch.class);

    private Projects projects = readProjectList();

//    @Scheduled(every = "1m")
    public void checkBuildTriggers(ScheduledExecution scheduledExecution) {
        logger.infof("Checking build triggers in %s at %s", projects, scheduledExecution.getFireTime());


    }

    private Projects readProjectList() {
        return null;
    }

}
