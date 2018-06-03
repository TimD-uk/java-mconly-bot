package main.java.ru.hybridonly.javabot;

import main.java.ru.hybridonly.javabot.Data.Manager;
import main.java.ru.hybridonly.javabot.command.clan;
import main.java.ru.hybridonly.javabot.command.link;
import main.java.ru.hybridonly.javabot.command.me;
import main.java.ru.hybridonly.javabot.command.top;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandManager extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(e.getMessage().getContentDisplay().startsWith("!"))
        {
            String[] args = e.getMessage().getContentDisplay().replaceFirst("!","").split(" ");

            switch(args[0])
            {
                case "link": //TODO verification task
                    link.run(e.getMessage());
                    break;
                case "clan": //TODO clan system
                    clan.run(e.getMessage());
                    break;
                case "me": //TODO all user info
                    me.run(e.getMessage());
                    break;
                case "top": //TODO top of all users
                    top.run(e.getMessage());
                    break;
                case "save":
                    Manager.get().save();
                    break;
            }
        }
    }
}