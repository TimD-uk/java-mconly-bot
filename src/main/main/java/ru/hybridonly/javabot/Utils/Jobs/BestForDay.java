package main.java.ru.hybridonly.javabot.Utils.Jobs;

import main.java.ru.hybridonly.javabot.Data.Manager;
import main.java.ru.hybridonly.javabot.Data.UserData;
import main.java.ru.hybridonly.javabot.Main;
import main.java.ru.hybridonly.javabot.Utils.Log;
import main.java.ru.hybridonly.javabot.modules.MySQL;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class BestForDay implements Job
{
    @Override
    public void execute(JobExecutionContext context) {
        HashMap<String, UserData> users = Manager.get().serversUsers.get("366615202470297600");
        HashMap<String, Integer> coefList = new HashMap<>();
        String bestUserId = null;
        Integer largestCoef = -1;

        for (String key : users.keySet())
        {
            UserData locUser = users.get(key);
            int coefficient = 0;
            coefficient += (int) (locUser.day_messages * 0.3);
            coefficient += (int) (locUser.day_count_messages * 0.8);
            coefficient += (int) (locUser.day_reactions * 0.6);
            coefficient += (int) (locUser.day_emojis * 0.005);
            coefList.put(key, coefficient);
        }

        for (String key : coefList.keySet())
        {
            if (coefList.get(key) >= largestCoef)
            {
                largestCoef = coefList.get(key);
                bestUserId = key;
            }
        }
        Guild guild = Main.api.getGuildById(users.get(bestUserId).user_server_id);
        Member member = guild.getMemberById(bestUserId);
        Member oldMan = null;
        Role role = getRole("Cool man", guild);
        for (Member mem : guild.getMembers())
        {
            if (mem.getRoles().contains(role))
            {
                oldMan = mem;
            }
        }
        if (role != null)
        {
            if (oldMan != null)
            {
                guild.getController().removeRolesFromMember(oldMan, role).queue();
                Log.ainfo("[Best of Day] Role revoked from " + oldMan.getUser().getId());
            }

            if (!member.getRoles().contains(role))
            {
                guild.getController().addSingleRoleToMember(member, role).queue();
                Log.ainfo("[Best of Day] Role given to " + member.getUser().getId());
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long time = timestamp.getTime() / 1000;
            MySQL.get().init(Main.config);
            MySQL.get().custom(
                    "CREATE TABLE userData_" + String.valueOf(time) +
                    " SELECT *" +
                    " FROM userData");
            /*
            TODO #1 занесение в таблицу топа
            TODO #2 добавить просто топ дня
            TODO #3 заносить лучшего юзера дня в UserData
            TODO #4 Формула для высчитывания (не только сообщения)

            dayMsgs * 0.3 + dayCountMsgs * 0.8 + dayReact * 0.6 + dayEmojis * 0.025
             */
            MySQL.get().close();
            Manager.get().everyDayReset();
        }
    }

    private Role getRole(String roleName, Guild guild)
    {
        List<Role> roles = guild.getRoles();
        for (Role r : roles)
        {
            if (r.getName().equals(roleName))
            {
                return r;
            }
        }
        return null;
    }
}