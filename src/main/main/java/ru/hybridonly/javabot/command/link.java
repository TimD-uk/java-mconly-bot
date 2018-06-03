package main.java.ru.hybridonly.javabot.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.time.OffsetDateTime;

public class link {
    public static void run(Message message)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("kron","http://discord.hybridonly.ru/", "http://www.clker.com/cliparts/s/z/e/c/V/V/white-letter-k-md.png");
        embed.setColor(Color.CYAN);
        embed.setTitle("Connect system");
        embed.setFooter("Powered by hybrid","https://i.imgur.com/RoJmU3z.png");
        embed.setTimestamp(OffsetDateTime.now());
        embed.addField("In work!", "", false);

        message.getChannel().sendMessage(embed.build()).queue();
    }
}