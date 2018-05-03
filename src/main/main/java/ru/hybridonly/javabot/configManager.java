package main.java.ru.hybridonly.javabot;


import java.io.*;
import java.util.Properties;

public class configManager {
    private static configManager instance = new configManager();

    public static configManager get()
    {
        return instance;
    }

    private Properties prop = new Properties();
    private OutputStream os = null;
    private InputStream is = null;

    private File configFile = new File("conf.properties");
    private File gamesJson = new File("games.json");

    public void setup() {
        if (!configFile.exists()) {
            try {
                os = new FileOutputStream(configFile);
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
            String path = "resources/games.json";
            try (InputStream in = getClass().getClassLoader().getResourceAsStream(path);
                 OutputStream out = new FileOutputStream(gamesJson))
            {

                int data;
                while ((data = in.read()) != -1) {
                    out.write(data);
                }
                log.info("Game's file was created");
            }
            catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }


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