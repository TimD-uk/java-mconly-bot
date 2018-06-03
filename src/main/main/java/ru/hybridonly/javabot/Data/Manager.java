package main.java.ru.hybridonly.javabot.Data;

import com.google.common.collect.Lists;
import main.java.ru.hybridonly.javabot.Main;
import main.java.ru.hybridonly.javabot.Utils.Log;
import main.java.ru.hybridonly.javabot.modules.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager
{
    public HashMap<String, ServerData> ServerData = new HashMap<>();
    public HashMap<String, ChannelData> channelData = new HashMap<>();
    public HashMap<String, HashMap<String, UserData>> serversUsers = new HashMap<>();

    private static Manager instance = new Manager();
    private ResultSet res = null;

    public static Manager get()
    {
        return instance;
    }

    /*
    Summon when bot is loading
     */
    //TODO проверять наличие таблиц в бд. Если нет - создавать таблицы с нужной структурой
    /*

    CREATE TABLE `hybrid_McOnlyBot_db`.`channelData123` (
  `channel_id` varchar(18) NOT NULL,
  `mat_allow` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    CREATE TABLE `hybrid_McOnlyBot_db`.`serverData123` (
  `server_id` bigint(18) NOT NULL,
  `server_prefix` varchar(10) DEFAULT '!',
  `server_mat_allow` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    CREATE TABLE `hybrid_McOnlyBot_db`.`userData123` (
  `user_id` varchar(18) NOT NULL,
  `user_server_id` varchar(18) NOT NULL,
  `allow_games` tinyint(1) NOT NULL DEFAULT '1',
  `is_bestforday` tinyint(1) NOT NULL DEFAULT '0',
  `all_messages` int(11) NOT NULL DEFAULT '0',
  `day_messages` int(11) NOT NULL DEFAULT '0',
  `last_message` int(11) NOT NULL DEFAULT '0' COMMENT 'timestamp',
  `day_count_messages` int(11) NOT NULL DEFAULT '0',
  `all_voice` int(11) NOT NULL DEFAULT '0',
  `day_voice` int(11) NOT NULL DEFAULT '0',
  `last_voice_connect` int(11) NOT NULL DEFAULT '0' COMMENT 'timestamp',
  `all_reactions` int(11) NOT NULL DEFAULT '0',
  `day_reactions` int(11) NOT NULL DEFAULT '0',
  `all_emojis` int(11) NOT NULL DEFAULT '0',
  `day_emojis` int(11) NOT NULL DEFAULT '0',
  `leader_faction` varchar(18) NOT NULL,
  `member_factions` varchar(535) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    CREATE TABLE `hybrid_McOnlyBot_db`.`userTop123` (
  `user_first` varchar(18) NOT NULL,
  `user_second` varchar(18) NOT NULL,
  `user_third` varchar(18) NOT NULL,
  `data` int(11) NOT NULL COMMENT 'timestamp'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

     */
    public void load() {
        try {
            /*#####
            LOAD DATA FOR CHANNELS
             #####*/
            res = MySQL.get().select("*", "channelData");
            while (res.next())
            {
                String channel_id = res.getString("channel_id");
                channelData.put(channel_id, new ChannelData(channel_id, res));
            }

            /*#####
            LOAD DATA FOR SERVERS
             #####*/
            res = MySQL.get().select("*", "serverData");
            while (res.next())
            {
                String server_id = res.getString("server_id");
                ServerData.put(server_id, new ServerData(server_id, res));
            }

            /*#####
            LOAD DATA FOR USERS
             #####*/
            res = MySQL.get().select("*", "userData");
            while (res.next())
            {
                String user_id = res.getString("user_id");
                String server_id = res.getString("user_server_id");
                if (serversUsers == null || serversUsers.isEmpty() || !serversUsers.containsKey(server_id))
                {
                    HashMap<String, UserData> inner = new HashMap<>();
                    inner.put(user_id, new UserData(user_id, server_id));
                    serversUsers.put(server_id, inner);
                } else {
                    HashMap<String, UserData> users = serversUsers.get(server_id);
                    users.put(user_id, new UserData(user_id, server_id));
                    serversUsers.put(server_id, users);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySQL.get().close();
    }

    /*
    Summon on everyday resetting, off bot and every five minutes for save data
     */
    public void save()
    {
        MySQL.get().init(Main.config);
        ResultSet res = MySQL.get().select("user_id", "userData");
        List<String> userList = new ArrayList<>();
        try {
            while (res.next())
            {
                userList.add(res.getString("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String key : serversUsers.keySet())
        {
            for (String keyUser : serversUsers.get(key).keySet())
            {
                UserData user = serversUsers.get(key).get(keyUser);
                if (userList.contains(user.user_id))
                {
                    MySQL.get().update("userData",
                            "`user_id`=" + user.user_id + " and `user_server_id`=" + user.user_server_id,
                            Lists.newArrayList(
                            "`user_id`=" + user.user_id,
                            "`user_server_id`=" + user.user_server_id,
                            "`allow_games`=" + user.allow_games,
                            "`is_bestforday`=" + user.is_bestforday,
                            "`all_messages`=" + user.all_messages,
                            "`day_messages`=" + user.day_messages,
                            "`last_message`=" + user.last_message,
                            "`day_count_messages`=" + user.day_count_messages,
                            "`all_voice`=" + user.all_voice,
                            "`day_voice`=" + user.day_voice,
                            "`last_voice_connect`=" + user.last_voice_connect,
                            "`all_reactions`=" + user.all_reactions,
                            "`day_reactions`=" + user.day_reactions,
                            "`all_emojis`=" + user.all_emojis,
                            "`day_emojis`=" + user.day_emojis,
                            "`leader_faction`=" + '"' + user.leader_faction + '"',
                            "`member_factions`=" + '"' + user.member_factions + '"'
                    ));
                } else {
                    MySQL.get().insert("userData", Lists.newArrayList(
                            user.user_id,
                            user.user_server_id,
                            user.allow_games,
                            user.is_bestforday,
                            user.all_messages,
                            user.day_messages,
                            user.last_message,
                            user.day_count_messages,
                            user.all_voice,
                            user.day_voice,
                            user.last_voice_connect,
                            user.all_reactions,
                            user.day_reactions,
                            user.all_emojis,
                            user.day_emojis,
                            '"' + user.leader_faction + '"',
                            '"' + user.member_factions + '"'
                    ));
                }
            }
        }
        Log.finfo("Users data saved");

        res = MySQL.get().select("server_id", "serverData");
        List<String> serverList = new ArrayList<>();
        try {
            while (res.next())
            {
                serverList.add(res.getString("server_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String key : ServerData.keySet())
        {
            ServerData server = ServerData.get(key);
            if (serverList.contains(key))
            {
                MySQL.get().update("serverData",
                        "`server_id`=" + server.server_id,
                        Lists.newArrayList(
                                "`server_id`=" + server.server_id,
                                "`server_prefix`=" +server.cmd_prefix,
                                "`server_mat_allow`=" + server.server_mat_allow
                        ));
            } else {
                MySQL.get().insert("serverData",
                        Lists.newArrayList(
                                server.server_id,
                                server.cmd_prefix,
                                server.server_mat_allow
                        ));
            }
        }
        Log.finfo("Servers data saved");

        res = MySQL.get().select("channel_id", "channelData");
        List<String> channelList = new ArrayList<>();
        try {
            while (res.next())
            {
                channelList.add(res.getString("channel_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String key : channelData.keySet())
        {
            ChannelData channel = channelData.get(key);
            if (channelList.contains(key))
            {
                MySQL.get().update("channelData",
                        "`channel_id`=" + channel.channel_id,
                        Lists.newArrayList(
                                "`channel_id`=" + channel.channel_id,
                                "`mat_allow`=" +channel.mat_allow
                        ));
            } else {
                MySQL.get().insert("channelData",
                        Lists.newArrayList(
                                channel.channel_id,
                                channel.mat_allow
                        ));
            }
        }
        Log.finfo("Channels data saved");

        MySQL.get().close();
        Log.ainfo("All data saved");
    }

    /*
    Everyday resetting day's data for achievement "Best of day"
    Summon everyday at 12 a.m. Moscow time
     */
    public void everyDayReset()
    {
        for (String keyServer : serversUsers.keySet())
        {
            for (String keyUser : serversUsers.get(keyServer).keySet())
            {

                HashMap<String, UserData> users = serversUsers.get(keyServer);
                UserData user = serversUsers.get(keyServer).get(keyUser);
                user.day_emojis = 0;
                user.day_reactions = 0;
                user.day_count_messages = 0;
                user.day_messages = 0;
                user.day_voice = 0;
                users.put(keyUser, user);
                serversUsers.put(keyServer, users);
            }
        }
        Log.info("Successful reset day's data in serversUsers");
        get().save();
        Log.info("Day's data was successful reset in DB");
    }
}
/*
TODO #2 добавить проверку всех серверов после загрузки их и добавление их здесь
 */