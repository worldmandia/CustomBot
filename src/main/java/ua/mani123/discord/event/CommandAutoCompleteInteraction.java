package ua.mani123.discord.event;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import ua.mani123.CBot;
import ua.mani123.discord.interaction.interactionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

import java.util.List;

public class CommandAutoCompleteInteraction extends ListenerAdapter {
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        CommandInteraction commandInteraction = interactionUtils.getCommandByName(event.getName());

        if (commandInteraction != null) {
            List<String> list = commandInteraction.getAutocompleteIds().get(event.getFocusedOption().getName());
            if (list != null){
                CBot.getLog().info(list.toString());
                List<Command.Choice> options = list.stream().filter(string -> string
                                .startsWith(event.getFocusedOption().getValue()))
                        .map(string -> new Command.Choice(string, string)).toList();
                event.replyChoices(options).queue();
            }
        }
    }
}
