package ua.mani123.events;

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

public class onReadyEvent extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        DTBot.getLogger().info("Update commands...");
        event.getJDA().updateCommands().addCommands(CustomCommand.settings.getCommandData(CommandUtils.getAllCommands().values())).queue();
        if (event.getGuildTotalCount() > 0) {
            DTBot.getLogger().info("Your bot is in " + event.getGuildTotalCount() + " guilds!");
        }
        for (Guild guild : event.getJDA().getGuilds()) {
            if (DTBot.getDatabase().isNull(guild.getId() + ".guild-name")) {
                DTBot.getLogger().info("Write" + guild.getName() + " guild to the database...");
                Config guildConfig = DTBot.getDatabase().createSubConfig();
                DTBot.getDatabase().set(guild.getId(), guildConfig);
                DTBot.getDatabase().set(guild.getId() + ".guild-name", guild.getName());
                if (DTBot.getConfig().getOrElse("bot.put-all-members-to-database", true)) {
                    for (Member member : guild.getMembers()) {
                        if (DTBot.getDatabase().isNull(guild.getId() + "." + member.getId() + ".username")) {
                            DTBot.getLogger().info("Write all members from guild '" + guild.getName() + "' to the database...");
                            Config memberConfig = DTBot.getDatabase().createSubConfig();
                            DTBot.getDatabase().set(guild.getId() + "." + member.getId(), memberConfig);
                            DTBot.getDatabase().set(guild.getId() + "." + member.getId() + ".username", member.getUser().getAsTag());
                            DTBot.getDatabase().set(guild.getId() + "." + member.getId() + ".personal-counter", 0);
                        }
                    }
                }
            }
        }

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
