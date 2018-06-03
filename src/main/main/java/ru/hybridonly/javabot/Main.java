package main.java.ru.hybridonly.javabot;

import main.java.ru.hybridonly.javabot.Data.Manager;
import main.java.ru.hybridonly.javabot.Utils.JobManager;
import main.java.ru.hybridonly.javabot.Utils.Log;
import main.java.ru.hybridonly.javabot.Utils.TimeManager;
import main.java.ru.hybridonly.javabot.modules.GamesChecker;
import main.java.ru.hybridonly.javabot.modules.MySQL;
import main.java.ru.hybridonly.javabot.modules.Stats;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.quartz.SchedulerException;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;

public class Main extends ListenerAdapter
{
    public static Properties config = null;
    public static JDA api = null;
    public static void main(String[] args){
        ConfigManager.get().setup();
        config = ConfigManager.get().config();

        MySQL.get().init(config);

        JDABuilder jda = new JDABuilder(AccountType.BOT);
        jda.setToken(config.getProperty("TOKEN"));
        jda.setAudioEnabled(false);
        jda.addEventListener(new Main());
        jda.addEventListener(new Stats());
        jda.addEventListener(new CommandManager());
        jda.addEventListener(new GamesChecker());
        try {
            jda.buildAsync();
        } catch (Exception e) {
            System.out.println("Check files with name 'conf.properties'.");
            System.out.println("Bot exception " + e.getLocalizedMessage());
        }

        try {
            JobManager.load();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        Manager.get().load();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Manager.get().save();
            try {
                Thread.sleep(1000);
                Log.ainfo("Bot has been stopped");

                String now = TimeManager.year() + "-" + TimeManager.month() + "-" + TimeManager.date() + "_" + TimeManager.hours() + "-" + TimeManager.minutes() + "-" + TimeManager.seconds();
                File old = new File("log/latest.log");
                if (old.exists())
                {
                    File newFile = new File("log/" + now + ".log");

                    if (newFile.exists())
                        try {
                            throw new java.io.IOException("file exists");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    boolean success = old.renameTo(newFile);
                    if (!success)
                        Log.awarn("File wasn't successfully renamed");
                } else {
                    Log.awarn("File " + old.getName() + " doesn't exists");
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }));
    }

    public void onReady(ReadyEvent e)
    {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        api = e.getJDA();
        Log.ainfo("Bot has been started. It connected to " + api.getUsers().size() + " users and manipulating " + api.getGuilds().size() + " servers");
    }
}