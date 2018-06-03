package main.java.ru.hybridonly.javabot.Data;

import main.java.ru.hybridonly.javabot.Main;
import main.java.ru.hybridonly.javabot.modules.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserData {
    public String user_id = null;
    public String user_server_id;
    public Boolean allow_games;
    public Boolean is_bestforday;
    public int all_messages;
    public int day_messages;
    public int last_message;
    public int day_count_messages;
    public int all_voice;
    public int day_voice;
    public int last_voice_connect;
    public int all_reactions;
    public int day_reactions;
    public int all_emojis;
    public int day_emojis;
    public String leader_faction;
    public String member_factions;

    public UserData(String user_id, String server_id)
    {

        MySQL.get().init(Main.config);
        ResultSet res = MySQL.get().select("*", "userData");

        try {
            while (res.next())
            {
                if (res.getString("user_id").equals(user_id) && res.getString("user_server_id").equals(server_id))
                {
                    this.user_id = res.getString("user_id");
                    user_server_id = res.getString("user_server_id");
                    allow_games = res.getBoolean("allow_games");
                    is_bestforday = res.getBoolean("is_bestforday");
                    all_messages = res.getInt("all_messages");
                    day_messages = res.getInt("day_messages");
                    last_message = res.getInt("last_message"); //TimeStamp
                    day_count_messages = res.getInt("day_count_messages");
                    all_voice = res.getInt("all_voice");
                    day_voice = res.getInt("day_voice");
                    last_voice_connect = res.getInt("last_voice_connect"); //TimeStamp
                    all_reactions = res.getInt("all_reactions");
                    day_reactions = res.getInt("day_reactions");
                    all_emojis = res.getInt("all_emojis");
                    day_emojis = res.getInt("day_emojis");
                    leader_faction = res.getString("leader_faction");
                    member_factions = res.getString("member_factions");
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQL.get().close();
        }
        if (this.user_id == null)
        {
            createUserData(user_id, server_id);
        }
    }

    private void createUserData(String user_id, String server_id)
    {
        this.user_id = user_id;
        user_server_id = server_id;
        allow_games = false;
        is_bestforday = false;
        all_messages = 0;
        day_messages = 0;
        last_message = 0;
        day_count_messages = 0;
        all_voice = 0;
        day_voice = 0;
        last_voice_connect = 0;
        all_reactions = 0;
        day_reactions = 0;
        all_emojis = 0;
        day_emojis = 0;
        leader_faction = "";
        member_factions = "";
    }

}