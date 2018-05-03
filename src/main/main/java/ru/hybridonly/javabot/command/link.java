package main.java.ru.hybridonly.javabot.command;

import net.dv8tion.jda.core.entities.Message;

import java.util.Timer;
import java.util.TimerTask;

public class link {
    public static void run(Message msg)
    {
        msg.getChannel().sendTyping().queue(m -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    msg.getChannel().sendMessage("Test").queue();
                    msg.delete().queue();
                }
            }, 1000);
        });
    }
}