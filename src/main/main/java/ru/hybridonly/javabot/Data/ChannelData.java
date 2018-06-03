package main.java.ru.hybridonly.javabot.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelData {
    public String channel_id;
    public Boolean mat_allow;

    public ChannelData(String channel_id, ResultSet res)
    {
        try {
            while (res.next())
            {
                if (res.getString("channel_id").equals(channel_id))
                {
                    this.channel_id = channel_id;
                    mat_allow = res.getBoolean("mat_allow");
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}