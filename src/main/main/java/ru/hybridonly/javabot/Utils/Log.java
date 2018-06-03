package main.java.ru.hybridonly.javabot.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Log
{
    private static File logFolder = new File("log");
    private static File logFile = new File("log/latest.log");
    private static String getTime()
    {
        return "[" + TimeManager.hours() + ":" + TimeManager.minutes() + ":" + TimeManager.seconds() + "] ";
    }
    private static void checkFolder()
    {
        if (!logFolder.exists())
        {
            logFolder.mkdir();
        }
    }

    private final class pref
    {
        public static final String ColINFO = Colors.BLUE + " [INFO] " + Colors.RESET;
        public static final String ColWARN = Colors.RED + " [WARN] " + Colors.RESET;
        public static final String ColDEBUG = Colors.GREEN + Colors.WHITE_BACKGROUND + " [DEBUG] " + Colors.RESET;
        public static final String INFO = " [INFO] ";
        public static final String WARN = " [WARN] ";
        public static final String DEBUG = " [DEBUG] ";
    }
    private static String PREFIX()
    {
        return getTime() + "[Log]";
    }
    public static void info(Object o)
    {
        System.out.println(PREFIX() + pref.ColINFO + String.valueOf(o));
    }

    public static void warn(Object o)
    {
        System.out.println(PREFIX() + pref.ColWARN + String.valueOf(o));
    }

    public static void debug(Object o)
    {
        System.out.println(PREFIX() + pref.ColDEBUG + String.valueOf(o));
    }

    public static void finfo(Object o)
    {
        checkFolder();
        try
        {
            String text = PREFIX() + pref.INFO + String.valueOf(o) + "\n";
            FileOutputStream os = new FileOutputStream(logFile, true);
            byte[] bytes = text.getBytes();
            os.write(bytes);
            os.close();
        } catch (IOException e) {
            warn(e.getMessage());
        }
    }

    public static void fdebug(Object o)
    {
        checkFolder();
        String str = String.valueOf(o);
        try
        {
            FileWriter writer = new FileWriter(logFile, true);
            String text = PREFIX() + pref.DEBUG + str + "\n";
            writer.write(text);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            warn(e.getMessage());
        }
    }

    public static void fwarn(Object o)
    {
        checkFolder();
        String str = String.valueOf(o);
        try
        {
            FileWriter writer = new FileWriter(logFile, true);
            String text = PREFIX() + pref.WARN + str + "\n";
            writer.write(text);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            warn(e.getMessage());
        }
    }

    public static void ainfo(Object o)
    {
        finfo(o);
        info(o);
    }

    public static void adebug(Object o)
    {
        fdebug(o);
        debug(o);
    }

    public static void awarn(Object o)
    {
        fwarn(o);
        warn(o);
    }
}