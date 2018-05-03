package main.java.ru.hybridonly.javabot;

import main.java.ru.hybridonly.javabot.modules.gamesChecker;
import main.java.ru.hybridonly.javabot.modules.mysql;
import main.java.ru.hybridonly.javabot.modules.stats;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Properties;

public class Main extends ListenerAdapter {

    private static Properties config = null;

    public static void main(String[] args){
        configManager.get().setup();
        config = configManager.get().config();

        mysql.get().init(config);

        JDABuilder jda = new JDABuilder(AccountType.BOT);
        jda.setToken(config.getProperty("TOKEN"));
        jda.setAudioEnabled(false);
        jda.addEventListener(new Main());
        jda.addEventListener(new Command());
        jda.addEventListener(new events());
        jda.addEventListener(new gamesChecker());
        jda.addEventListener(new stats());
        try {
            jda.buildAsync();
        } catch (Exception e) {
            System.out.println("Check files with name 'conf.properties'.");
            System.out.println("Bot exception " + e.getLocalizedMessage());
        }
    }

    public void onReady(ReadyEvent e)
    {
        log.info("I'm ready");
    }



}