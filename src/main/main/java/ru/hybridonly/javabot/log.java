package main.java.ru.hybridonly.javabot;

import java.time.LocalTime;

public class log
{
    private static class time
    {
        private static String checkTime(int number)
        {
            if (number < 10 && number >= 0)
            {
                return "0" + String.valueOf(number);
            }
            return String.valueOf(number);
        }
        public static String getTime()
        {
            LocalTime now = LocalTime.now();
            return "[" + checkTime(now.getHour()) + ":" + checkTime(now.getMinute()) + ":" + checkTime(now.getSecond()) + "] ";
        }

    }

    private final class pref
    {
        public static final String INFO = " [INFO] ";
        public static final String WARN = " [WARN] ";
        public static final String DEBUG = " [DEBUG] ";
    }
    private static String PREFIX()
    {
        return time.getTime() + "[Log]";
    }

    public static void info(String str)
    {
        System.out.println(PREFIX() + pref.INFO + str);
    }

    public static void warn(String str)
    {
        System.out.println(PREFIX() + pref.WARN + str);
    }

    public static void debug(String str)
    {
        System.out.println(PREFIX() + pref.DEBUG + str);
    }

}