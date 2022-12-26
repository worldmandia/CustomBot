package ua.mani123.discord.event.CustomEvents;

import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class CustomCommandAutoCompleteInteraction extends ListenerAdapter {

  @Override
  public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
    if (InteractionUtils.getCommands().containsKey(event.getName())) {
      CommandInteraction commandInteraction = InteractionUtils.getCommands().get(event.getName());
      List<String> list = commandInteraction.getAutocompleteIds().get(event.getFocusedOption().getName());
      if (list != null) {
        List<Command.Choice> options = list.stream().filter(string -> string
                .startsWith(event.getFocusedOption().getValue()))
            .map(string -> new Command.Choice(string, string)).toList();
        event.replyChoices(options).queue();
      }
    }
  }
}
