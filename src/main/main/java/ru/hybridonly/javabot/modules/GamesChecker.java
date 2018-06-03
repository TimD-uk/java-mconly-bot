package main.java.ru.hybridonly.javabot.modules;

import main.java.ru.hybridonly.javabot.Data.Manager;
import main.java.ru.hybridonly.javabot.Utils.Log;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamesChecker extends ListenerAdapter
{
    public void onUserUpdateGame(UserUpdateGameEvent e)
    {
        if (!checkUserAllow(e))
        {
            return;
        }
        Game game = e.getNewGame();
        String gameRoleName = checkGame(game);
        if (gameRoleName != null)
        {
            Object r = getRole(gameRoleName, e.getGuild());
            if (r != null)
            {
                Role role = (Role) r;
                e.getGuild().getController().addSingleRoleToMember(e.getMember(), role).queue();
                Log.info("The user '" + e.getUser().getId() + "' was given the game role of '" + role.getName() + "' for game '" + game.getName() + "'");
            }
        }
    }

    private boolean checkRegEx(String regex, String game)
    {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(game);
        return m.matches();
    }

    private Role getRole(String gameRoleName, Guild guild)
    {
        List<Role> roles = guild.getRoles();
        for (Role r : roles)
        {
            if (r.getName().equals(gameRoleName))
            {
                return r;
            }
        }
        return null;
    }

    private JSONArray getJsonArr()
    {
        File gamesFile = new File("games.json");

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(gamesFile));
            JSONArray arr = (JSONArray) obj;
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String checkGame(Game game)
    {
        JSONArray arr = this.getJsonArr();
        if (!arr.equals(null))
        {
            for (Object gameElem : arr)
            {
                JSONArray gameElemArr = (JSONArray) gameElem;
                if (checkRegEx(gameElemArr.get(0).toString(), game.getName()))
                {
                    return gameElemArr.get(1).toString();
                }
            }
        }
        return null;
    }

    private boolean checkUserAllow(UserUpdateGameEvent event)
    {
        if (Manager.get().serversUsers.containsKey(event.getGuild().getId()))
        {
            if (Manager.get().serversUsers.get(event.getGuild().getId()).containsKey(event.getUser().getId()))
            {
                return Manager.get().serversUsers.get(event.getGuild().getId()).get(event.getUser().getId()).allow_games;
            }
        }
        return false;
    }
}