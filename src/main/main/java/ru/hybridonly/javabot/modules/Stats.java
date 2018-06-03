package main.java.ru.hybridonly.javabot.modules;

import main.java.ru.hybridonly.javabot.Data.Manager;
import main.java.ru.hybridonly.javabot.Data.UserData;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class Stats extends ListenerAdapter
{
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e)
    {
        User senderUser = e.getMember().getUser();
        Member senderMem = e.getMember();
        if (senderUser.isBot())
        {
            return;
        }

        UserData data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());

        if (data == null)
        {
            createUser(senderUser.getId(), senderMem.getGuild().getId());
            data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        data.last_voice_connect = (int) timestamp.getTime() / 1000;
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e)
    {
        User senderUser = e.getMember().getUser();
        Member senderMem = e.getMember();
        if (senderUser.isBot())
        {
            return;
        }

        UserData data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());

        if (data == null)
        {
            createUser(senderUser.getId(), senderMem.getGuild().getId());
            data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int lastVoiceTime = (int) timestamp.getTime() / 1000 - data.last_voice_connect;

        data.all_voice += lastVoiceTime;
        data.day_voice += lastVoiceTime;
        data.last_voice_connect = 0;

        HashMap<String, UserData> inner = new HashMap<>();
        inner.put(senderUser.getId(), data);
        Manager.get().serversUsers.put(senderMem.getGuild().getId(), inner);
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e)
    {
        User senderUser = e.getUser();
        Member senderMem = e.getMember();
        if (senderUser.isBot())
        {
            return;
        }

        UserData data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());

        if (data == null)
        {
            createUser(senderUser.getId(), senderMem.getGuild().getId());
            data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());
        }

        data.all_reactions++;
        data.day_reactions++;

        HashMap<String, UserData> inner = new HashMap<>();
        inner.put(senderUser.getId(), data);
        Manager.get().serversUsers.put(senderMem.getGuild().getId(), inner);
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        User senderUser = e.getAuthor();
        Member senderMem = e.getMember();

        if (senderUser.isBot())
        {
            return;
        }
        UserData data = null;
        if (Manager.get().serversUsers.get(senderMem.getGuild().getId()) != null)
        {
            data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());
        }

        if (data == null)
        {
            createUser(senderUser.getId(), senderMem.getGuild().getId());
            data = Manager.get().serversUsers.get(senderMem.getGuild().getId()).get(senderUser.getId());
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long time = timestamp.getTime() / 1000;
        if (data.last_message + 15 < time)
        {
            data.last_message = Math.toIntExact(time);
            data.day_count_messages++;
        } else if (data.last_message == 0) {
            data.last_message = (int) timestamp.getTime() / 1000;
        }
        List<Emote> emotes = e.getMessage().getEmotes();
        if (!emotes.isEmpty())
        {
            data.all_emojis += emotes.size();
            data.day_emojis += emotes.size();
        }
        data.all_messages++;
        data.day_messages++;

        HashMap<String, UserData> inner = Manager.get().serversUsers.get(senderMem.getGuild().getId());
        inner.put(senderUser.getId(), data);
        Manager.get().serversUsers.put(senderMem.getGuild().getId(), inner);
    }

    private void createUser(String user_id, String server_id)
    {
        HashMap<String, UserData> inner = Manager.get().serversUsers.get(server_id);
        if (inner == null)
            inner = new HashMap<>();
        inner.put(user_id, new UserData(user_id, server_id));
        Manager.get().serversUsers.put(server_id, inner);
    }
}
/*
DATABASE : userData_<GUILDid>

| user_id | user_server_id | allow_games | is_bestforday | all_messages | day_messages | last_message | day_count_messages | all_voice | day_voice | last_voice_connect | all_reactions | day_reactions | all_emojis | day_emojis | leader_faction | member_factions |
 */