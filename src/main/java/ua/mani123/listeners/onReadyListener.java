package ua.mani123.listeners;

import com.electronwill.nightconfig.core.Config;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.activity.ActivityUtils;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class onReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        DTBot.getLogger().info("Update commands...");
        event.getJDA().updateCommands().addCommands(CustomCommand.settings.getCommandData(CommandUtils.getAllCommands().values())).queue();
        if (event.getGuildTotalCount() > 0) {
            DTBot.getLogger().info("Your bot is in " + event.getGuildTotalCount() + " guilds!");
        }
        DTBot.getLogger().info("Write all guilds to the database...");
        for (Guild guild : event.getJDA().getGuilds()) {
            Config config = DTBot.getDatabase().createSubConfig();
            DTBot.getDatabase().set(guild.getId(), config);
            DTBot.getDatabase().set(guild.getId() + ".guild-name", guild.getName());
            if (DTBot.getConfig().getOrElse("bot.put-all-members-to-database", true)){
                for (Member member : guild.getMembers()) {
                    DTBot.getDatabase().set(guild.getId() + ".guild-name" + member.getId(), config);
                    DTBot.getDatabase().set(guild.getId() + ".guild-name" + member.getId(), member.getEffectiveName());
                }
            }

        }
        //DTBot.getDatabase().save();

        this.startActivityThread(event);

        DTBot.getLogger().info("Done!");
    }

    private void startActivityThread(@NotNull ReadyEvent event) {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        int time = DTBot.getConfig().getIntOrElse("bot.activity-change-every", 10);
        exec.scheduleAtFixedRate(() -> {
            for (ua.mani123.activity.activity activity : ActivityUtils.getAllActivities()) {
                if (activity.getUrl().equals("null")) {
                    event.getJDA().getPresence().setActivity(Activity.of(activity.getType(), activity.getActivityText()));
                } else {
                    event.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.STREAMING, activity.getActivityText(), activity.getUrl()));
                }
                try {
                    Thread.sleep(time * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, (long) time * ActivityUtils.getAllActivities().size(), TimeUnit.SECONDS);
    }
}
