package ua.mani123.discord.event;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.Utils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class SlashCommandInteraction extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    if (InteractionUtils.getCommands().containsKey(event.getName())) {
      CommandInteraction commandInteraction = InteractionUtils.getCommands().get(event.getName());
      TempData tempData = new TempData();
      StringSubstitutor str = new StringSubstitutor(Utils.getPlaceholders(event));

      if (filterUtils.filterCheck(commandInteraction.getFilters(), event, str, tempData)) {
        EventUtils.runActions(commandInteraction.getActionIds(), event, str, tempData);
      }
    }

  }

}
