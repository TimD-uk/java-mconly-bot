package main.java.ru.hybridonly.javabot.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.time.OffsetDateTime;

public class me {

    public static void run(Message msg)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("hybrid","https://hybridonly.ru/",msg.getAuthor().getAvatarUrl());
        embed.setColor(Color.white);
        embed.setTitle("Test embed");
        embed.setFooter("Minecraftonly.ru","https://minecraftonly.ru/templates/new/img/logo.png");

        embed.addField("Welcome", "Welcome man", true);
        embed.addField("Test", "Test2", true);

        embed.setTimestamp(OffsetDateTime.now());
        embed.setThumbnail(msg.getAuthor().getAvatarUrl());
        embed.addField("Test", "Test2", false);

        msg.getChannel().sendMessage(embed.build()).queue();

    }
}