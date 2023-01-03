package ua.mani123.discord.event.CustomEvents;

import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.event.EventUtils;
import ua.mani123.discord.interaction.InteractionTypes;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.EntityMenuInteraction;
import ua.mani123.discord.interaction.interactions.StringMenuInteraction;

public class CustomSelectMenuInteraction extends ListenerAdapter {

  @Override
  public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
    InteractionUtils.getInteractions().get(InteractionTypes.STRING_SELECT_MENU).forEach(interaction -> {
      if (interaction instanceof StringMenuInteraction stringMenuInteraction) {
        if (event.getComponentId().equals(stringMenuInteraction.getId())) {
          TempData tempData = new TempData();
          if (filterUtils.filterCheck(stringMenuInteraction.getFilters(), event, new StringSubstitutor(tempData.getPlaceholders()), tempData)) {
            EventUtils.runActions(stringMenuInteraction.getActionIds(), event, new StringSubstitutor(tempData.getPlaceholders()), tempData);
          }
        }
      }
    });
  }

  @Override
  public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
    InteractionUtils.getInteractions().get(InteractionTypes.ENTITY_SELECT_MENU).forEach(interaction -> {
      if (interaction instanceof EntityMenuInteraction entityMenuInteraction) {
        if (event.getComponentId().equals(entityMenuInteraction.getId())) {
          TempData tempData = new TempData();
          if (filterUtils.filterCheck(entityMenuInteraction.getFilters(), event, new StringSubstitutor(tempData.getPlaceholders()), tempData)) {
            EventUtils.runActions(entityMenuInteraction.getActionIds(), event, new StringSubstitutor(tempData.getPlaceholders()), tempData);
          }
        }
      }
    });
  }
}
