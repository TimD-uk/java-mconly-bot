package main.java.ru.hybridonly.javabot.Utils;

import main.java.ru.hybridonly.javabot.Utils.Jobs.BestForDay;
import main.java.ru.hybridonly.javabot.Utils.Jobs.SaveData;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobManager
{
    static class cron
    {
        public static final String everySecond = "0/1 0/1 * 1/1 * ? *";
        public static final String everyMinute = "0 0/1 * 1/1 * ? *";
        public static final String everyFiveMinute = "0 0/5 * 1/1 * ? *";
        public static final String everyHour = "0 0 0/1 1/1 * ? *";
        public static final String everyDay = "0 0 0 1/1 * ? *";
    }

    public static void load() throws SchedulerException
    {
        SchedulerFactory factoryData = new StdSchedulerFactory();
        Scheduler schedulerData = factoryData.getScheduler();
        schedulerData.start();
        JobDetail job = JobBuilder.newJob(SaveData.class).build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .withSchedule(
                        CronScheduleBuilder
                        .cronSchedule(cron.everyFiveMinute)
                )
                .build();
        schedulerData.scheduleJob(job, trigger);

        SchedulerFactory factoryEveryDay = new StdSchedulerFactory();
        Scheduler schedulerBest = factoryEveryDay.getScheduler();
        schedulerBest.start();
        JobDetail jobBest = JobBuilder
                .newJob(BestForDay.class)
                .build();
        Trigger triggerBest = TriggerBuilder
                .newTrigger()
                .startNow()
                .withSchedule(
                        CronScheduleBuilder
                        .cronSchedule(cron.everyDay) //TODO добавить сохранение в бд из этого модуля
                )
                .build();
        schedulerBest.scheduleJob(jobBest, triggerBest);
    }
}