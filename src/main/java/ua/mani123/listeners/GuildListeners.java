package ua.mani123.listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.command.CommandActions;
import ua.mani123.command.CustomCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuildListeners extends ListenerAdapter {

    protected List<CommandData> commandDataList = new ArrayList<>();

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        for (Map.Entry<CommandActions, List<CustomCommand>> command : DTBot.getCommands().entrySet()) {
            switch (command.getKey()){
                case CREATE_TICKET_EMBED -> {
                    for (CustomCommand cmd: command.getValue()) {
                        commandDataList.add(cmd.getCommand()
                                .addOption(OptionType.STRING, "ticket_type", "type of ticket", true, true)
                                .addOption(OptionType.STRING, "ticket_id", "id of ticket", true, true));
                    }
                }
            }
        }
        event.getGuild().updateCommands().addCommands(commandDataList).queue();

        // TODO custom presence
        event.getJDA().getPresence().setPresence(Activity.of(Activity.ActivityType.valueOf(DTBot.getConfig().getString("bot-custom.activity".toUpperCase(), "PLAYING")),
                        DTBot.getConfig().getString("bot-custom.activity-text", "tickets %tickets%").replace("%tickets%", String.valueOf(DTBot.getTickets().size()))),
                false);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}
