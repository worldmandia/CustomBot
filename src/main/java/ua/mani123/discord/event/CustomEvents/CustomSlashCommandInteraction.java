package ua.mani123.discord.event.CustomEvents;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.event.EventUtils;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class CustomSlashCommandInteraction extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    String cmdName = event.getName();
    if (InteractionUtils.getCommands().containsKey(cmdName)) {
      CommandInteraction commandInteraction = InteractionUtils.getCommands().get(cmdName);
      TempData tempData = new TempData();
      StringSubstitutor str = new StringSubstitutor(tempData.getPlaceholders());

      if (filterUtils.filterCheck(commandInteraction.getFilters(), event, str, tempData)) {
        EventUtils.runActions(commandInteraction.getActionIds(), event, str, tempData);
      }
    }

  }

}
