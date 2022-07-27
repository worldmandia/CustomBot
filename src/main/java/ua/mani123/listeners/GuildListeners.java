package ua.mani123.listeners;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;

import java.util.List;

public class GuildListeners extends ListenerAdapter {

    protected List<CommandData> commandDataList = List.of(Commands.slash(DTBot.getLang().getString("commands.ticketembed.name", "ticketembed"),
                    DTBot.getLang().getString("commands.ticketembed.description", "Not found description"))
            .addOption(OptionType.STRING, "type", "type of ticket", true, true)
            .addOption(OptionType.STRING, "id", "id of ticket", true, true));

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}
