package ua.mani123.listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.activity.ActivityUtils;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuildListeners extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        DTBot.getLogger().info("Update commands...");
        event.getJDA().updateCommands().addCommands(CustomCommand.settings.getCommandData(CommandUtils.getAllCommands().values())).queue();
        if (event.getGuildTotalCount() > 0) {
            DTBot.getLogger().info("Your bot is in " + event.getGuildTotalCount() + " guilds");
        }

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        int time = DTBot.getConfig().getFileConfig().getIntOrElse("bot-ticket.activity-change-every", 10);
        exec.scheduleAtFixedRate(() -> {
            for (ua.mani123.activity.activity activity : ActivityUtils.getAllActivities()) {
                if (!DTBot.isBotEnabled()) return;
                DTBot.getLogger().info(activity.getType().toString());
                if (activity.getType() != Activity.ActivityType.STREAMING) {
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

        DTBot.getLogger().info("Done!");
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}
