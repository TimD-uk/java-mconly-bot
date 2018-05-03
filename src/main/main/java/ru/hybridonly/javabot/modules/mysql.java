package main.java.ru.hybridonly.javabot.modules;

import main.java.ru.hybridonly.javabot.log;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class mysql
{
    private static mysql instance = new mysql();
    public static mysql get() {
        return instance;
    }

    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet results = null;

    public void init(Properties config)
    {
        try {
            con = DriverManager.getConnection(config.getProperty("DBHOST"), config.getProperty("DBUSER"), config.getProperty("DBPASS"));
            stmt = con.createStatement();

            log.info("Connected to mysql server as " + config.getProperty("DBUSER"));
        } catch (SQLException e) {
            e.printStackTrace();
            log.info("MySQL Error - Please, stop the bot");
        }
    }

    public ResultSet select(String what, String from, String where)
    {
        String query = "SELECT " + what + " FROM " + from + " WHERE " + where;
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ResultSet select(String what, String from)
    {
        String query = "SELECT " + what + " FROM " + from;
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet custom(String query) {
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <E> ResultSet insert(String where, ArrayList<E>... values)
    {
        String query = "INSERT INTO " + where + " VALUES " + values[0].toString().replace("[", "(").replace("]", ")");
        log.info(query);
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet update()
    {
        return null;
    }

}