package ua.mani123.discord.event.CustomEvents;

import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.event.EventUtils;
import ua.mani123.discord.interaction.InteractionTypes;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.ModalInteraction;

public class CustomModalInteraction extends ListenerAdapter {

  @Override
  public void onModalInteraction(@NotNull ModalInteractionEvent event) {
    InteractionUtils.getInteractions().get(InteractionTypes.MODALS).forEach(interaction -> {
      if (interaction instanceof ModalInteraction modalInteraction) {
        if (event.getModalId().equals(modalInteraction.getModalId())) {
          TempData tempData = new TempData();
          modalInteraction.getPlaceholderValues()
              .forEach(value -> tempData.getContentData().put(value, Objects.requireNonNull(event.getValue(value)).getAsString()));
          if (filterUtils.filterCheck(modalInteraction.getFilters(), event, new StringSubstitutor(tempData.getPlaceholders()), tempData)) {
            EventUtils.runActions(modalInteraction.getActionIds(), event, new StringSubstitutor(tempData.getPlaceholders()), tempData);
          }
        }
      }
    });
  }
}
