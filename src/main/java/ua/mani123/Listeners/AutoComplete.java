package ua.mani123.Listeners;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import ua.mani123.DTBot;
import ua.mani123.ticket.TicketType;

import java.util.List;
import java.util.stream.Collectors;

public class AutoComplete extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equals(DTBot.getLang().getString("commands.embed.name", "ticketembed")) && event.getFocusedOption().getName().equals("type")) {
            List<Command.Choice> options = TicketType.getStrings()
                    .stream()
                    .filter(s -> s.startsWith(event.getFocusedOption().getValue()))
                    .map(s -> new Command.Choice(s, s))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
        if (event.getName().equals(DTBot.getLang().getString("commands.embed.name", "ticketembed")) && event.getFocusedOption().getName().equals("id")) {
            List<Command.Choice> options = DTBot.getIdsByType().get(TicketType.valueOf(event.getOption("type", OptionMapping::getAsString)))
                    .stream()
                    .filter(s -> s.startsWith(event.getFocusedOption().getValue()))
                    .map(s -> new Command.Choice(s, s))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}
