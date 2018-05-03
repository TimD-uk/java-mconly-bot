package main.java.ru.hybridonly.javabot.modules;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.Timestamp;

public class stats extends ListenerAdapter
{
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e)
    {
        User senderUser = e.getMember().getUser();
        Member senderMem = e.getMember();
        if (senderUser.isBot())
        {
            return;
        }
        UserData data = new UserData(senderUser.getId(), senderMem.getGuild().getId());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        data.last_voice_connect = (int) timestamp.getTime();
        data.save();
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e)
    {
        User senderUser = e.getMember().getUser();
        Member senderMem = e.getMember();
        if (senderUser.isBot())
        {
            return;
        }
        UserData data = new UserData(senderUser.getId(), senderMem.getGuild().getId());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int lastVoiceTime = (int) timestamp.getTime() - data.last_voice_connect;
        data.all_voice = data.all_voice + lastVoiceTime;
        data.day_voice = data.day_voice + lastVoiceTime;
        data.last_voice_connect = 0;
        data.save();
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e)
    {

    }

/*
(http://home.dv8tion.net:8080/job/JDA/javadoc/net/dv8tion/jda/core/entities/Message.html#getEmotes--)
So use a messageReceivedEvent and use event.getMessage() and then use that method on the message ^
 */

    public void onMessageReceived(MessageReceivedEvent e)
    {
        User senderUser = e.getAuthor();
        Member senderMem = e.getMember();

        if (senderUser.isBot())
        {
            return;
        }
        UserData data = new UserData(senderUser.getId(), senderMem.getGuild().getId());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (data.last_message + 15 >= (int) timestamp.getTime())
        {
            data.last_message = (int) timestamp.getTime();
            data.day_count_messages++;
        }
        data.all_messages++;
        data.day_messages++;
        data.save();
    }

    public void onShutdown(ShutdownEvent e)
    {
        /*
        Сохранение всех параметров всех пользователей серверов
         */
    }
}
/* DATABASE : userData_<GUILDid>

| user_id | allow_games | is_bestforday | all_messages | day_messages | last_message | day_count_messages | all_voice | day_voice | last_voice_connect | all_reactions | all_emojis | leader_faction | member_factions |
 */