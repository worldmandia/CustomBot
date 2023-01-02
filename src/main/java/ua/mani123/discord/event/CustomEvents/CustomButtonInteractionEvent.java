package ua.mani123.discord.event.CustomEvents;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.event.EventUtils;
import ua.mani123.discord.interaction.InteractionTypes;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.ButtonInteraction;

public class CustomButtonInteractionEvent extends ListenerAdapter {

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    InteractionUtils.getInteractions().get(InteractionTypes.BUTTON).forEach(interaction -> {
      if (interaction instanceof ButtonInteraction buttonInteraction) {
        if (event.getComponentId().equals(buttonInteraction.getId())) {
          TempData tempData = new TempData();

          StringSubstitutor str = new StringSubstitutor(tempData.getPlaceholders());
          EventUtils.runActions(buttonInteraction.getActionIds(), event, str, tempData);
        }
      }
    });
  }
}
