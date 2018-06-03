package main.java.ru.hybridonly.javabot.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerData {
    public String server_id;
    public String cmd_prefix;
    public Boolean server_mat_allow;

    public ServerData(String server_id, ResultSet res)
    {
        try {
            while (res.next())
            {
                if (res.getString("server_id").equals(server_id))
                {
                    this.server_id = server_id;
                    cmd_prefix = res.getString("server_prefix");
                    server_mat_allow = res.getBoolean("server_mat_allow");
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}