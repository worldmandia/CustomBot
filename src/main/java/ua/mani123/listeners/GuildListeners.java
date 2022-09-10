package ua.mani123.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;

public class GuildListeners extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        DTBot.getLogger().info("Update commands...");
        event.getJDA().updateCommands().addCommands(CustomCommand.settings.getCommandData(CommandUtils.getAllCommands().values())).queue();
        if (event.getGuildTotalCount() > 0){
            DTBot.getLogger().info("Your bot is in " + event.getGuildTotalCount() + " guilds");
        }
        DTBot.getLogger().info("Done!");
        // TODO custom presence
        //event.getJDA().getPresence().setPresence();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}
