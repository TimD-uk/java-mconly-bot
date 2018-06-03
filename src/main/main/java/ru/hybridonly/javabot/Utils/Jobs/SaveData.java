package main.java.ru.hybridonly.javabot.Utils.Jobs;

import main.java.ru.hybridonly.javabot.Data.Manager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SaveData implements Job
{
    @Override
    public void execute(JobExecutionContext context) {
        Manager.get().save();
    }
}