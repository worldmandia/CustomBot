package ua.mani123.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CustomCommand cmd = CommandUtils.getAllCommands().get(event.getName());
        for (Action action : cmd.getActions()) {
            if (action.isOnlyTicket()) {
                DTBot.getLogger().warn(action.getId() + " this action only for tickets");
            } else {
                UniversalActionExecutor.use(event, action);
            }
        }
    }
}
