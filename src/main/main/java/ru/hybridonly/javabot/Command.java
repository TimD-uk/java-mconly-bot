package main.java.ru.hybridonly.javabot;

import com.google.common.collect.Lists;
import main.java.ru.hybridonly.javabot.command.link;
import main.java.ru.hybridonly.javabot.command.me;
import main.java.ru.hybridonly.javabot.modules.mysql;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Command extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(e.getMessage().getContentDisplay().startsWith("!"))
        {
            String[] args = e.getMessage().getContentDisplay().replaceFirst("!","").split(" ");

            switch(args[0])
            {
                case "link":
                    link.run(e.getMessage());
                    break;
                case "me":
                    me.run(e.getMessage());
                    break;
                case "test":
                    mysql.get().insert("test", Lists.newArrayList(Lists.newArrayList("test", "test1488"), Lists.newArrayList("huy", "huy1488")));
                    break;
            }
        }
    }
}