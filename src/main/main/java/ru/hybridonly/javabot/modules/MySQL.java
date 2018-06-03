package main.java.ru.hybridonly.javabot.modules;

import main.java.ru.hybridonly.javabot.Utils.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class MySQL
{
    private static MySQL instance = new MySQL();
    public static MySQL get() {
        return instance;
    }

    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet results = null;

    public void init(Properties config)
    {
        try {
            con = DriverManager.getConnection(
                    config.getProperty("DBHOST"),
                    config.getProperty("DBUSER"),
                    config.getProperty("DBPASS")
            );
            Log.ainfo("Connected to mysql server as " + config.getProperty("DBUSER"));
        } catch (SQLException e) {
            e.printStackTrace();
            Log.ainfo("MySQL Error - Please, stop the bot");
        }
    }

    public void close()
    {
        try {
            con.close();
            Log.info("Disconnected from DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String what, String from, String where)
    {
        String query = "SELECT " + what + " FROM " + from + " WHERE " + where;
        try {
            stmt = con.createStatement();
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
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int custom(String query) {
        try {
            Statement stmt = con.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public <E> int insert(String table, ArrayList<E>... values)
    {
        String query = "INSERT INTO " + table + " VALUES " + values[0].toString().replace("[", "(").replace("]", ")");
        try {
            Statement stmt = con.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public <E> int update(String table, String where,  ArrayList<E>... values)
    {
        String query = "UPDATE " + table + " SET " + values[0].toString().replace("[", "").replace("]", "") + " WHERE " + where;
        try {
            Statement stmt = con.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}