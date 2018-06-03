package main.java.ru.hybridonly.javabot.Utils;

import java.sql.Timestamp;

public class TimeManager {
    private static String check(int dateTime)
    {
        if (dateTime < 10)
        {
            return "0" + String.valueOf(dateTime);
        } else {
            return String.valueOf(dateTime);
        }
    }
    private static Timestamp getTime()
    {
        return new Timestamp(System.currentTimeMillis());
    }
    public static String year()
    {
        return String.valueOf(getTime().getYear() - 100);
    }
    public static String month()
    {
        int local = getTime().getMonth() + 1;
        return check(local);
    }
    public static String date()
    {
        return check(getTime().getDate());
    }
    public static String hours()
    {
        return check(getTime().getHours());
    }
    public static String minutes()
    {
        return check(getTime().getMinutes());
    }
    public static String seconds()
    {
        return check(getTime().getSeconds());
    }
}