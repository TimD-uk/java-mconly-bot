package main.java.ru.hybridonly.javabot.command;

import main.java.ru.hybridonly.javabot.Data.Manager;
import main.java.ru.hybridonly.javabot.Data.UserData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.time.OffsetDateTime;

public class me {
    public static void run(Message msg)
    {
        UserData me = Manager.get().serversUsers.get(msg.getGuild().getId()).get(msg.getAuthor().getId());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("kron","http://discord.hybridonly.ru/", "http://www.clker.com/cliparts/s/z/e/c/V/V/white-letter-k-md.png");
        embed.setColor(Color.CYAN);
        embed.setTitle("User info of " + msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator());
        embed.setFooter("Powered by hybrid","https://i.imgur.com/RoJmU3z.png");
        embed.setTimestamp(OffsetDateTime.now());

        embed.addField("Выдача игр", String.valueOf(me.allow_games), true);
        embed.addField("Количество сообщений", String.valueOf(me.all_messages), true);

        msg.getChannel().sendMessage(embed.build()).queue();

    }
}