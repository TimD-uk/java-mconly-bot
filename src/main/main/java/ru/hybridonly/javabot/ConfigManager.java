package main.java.ru.hybridonly.javabot;


import main.java.ru.hybridonly.javabot.Utils.Log;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class ConfigManager
{
    private static ConfigManager instance = new ConfigManager();

    public static ConfigManager get()
    {
        return instance;
    }

    private Properties prop = new Properties();
    private OutputStream os = null;
    private InputStream is = null;

    private File configFile = new File("conf.properties");
    private File gamesJson = new File("games.json");

    public void setup()
    {
        if (!configFile.exists()) {
            try {
                os = new FileOutputStream(configFile);//TODO добавить комменты к каждому из блоков
                prop.setProperty("TOKEN", "PUT YOUR TOKEN HERE");
                prop.setProperty("PREFIX", "$");
                prop.setProperty("DBHOST", "PUT HOST NAME HERE");
                prop.setProperty("DBUSER", "PUT USER NAME HERE");
                prop.setProperty("DBPASS", "PUT PASSWORD HERE");

                prop.store(os, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.load();

        if (!gamesJson.exists())
        {
            String path = "main/resources/games.json";
            String path2 = "games.json";
            try
            {
                URL inputUrl = getClass().getClassLoader().getResource(path);
                OutputStream out = new BufferedOutputStream(new FileOutputStream(gamesJson));
                if(inputUrl == null) {
                    inputUrl = getClass().getClassLoader().getResource(path2);
                    if (inputUrl == null)
                        throw new IOException("Cannot get resource \"games.json\" from Jar file.");
                }
                FileUtils.copyURLToFile(inputUrl, gamesJson);
                Log.ainfo("Game's file was created");
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }
//TODO добавление в конфиг включения и отключения модулей (глобально)

    public void load()
    {
        try {
            is = new FileInputStream(configFile);
            prop.load(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties config()
    {
        return prop;
    }
}