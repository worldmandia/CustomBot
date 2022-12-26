package ua.mani123.discord.event.CustomEvents;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.event.EventUtils;
import ua.mani123.discord.interaction.InteractionUtils;

public class CustomButtonInteractionEvent extends ListenerAdapter {

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    TempData tempData = new TempData();
    String id = event.getComponentId();
    StringSubstitutor str = new StringSubstitutor(tempData.getPlaceholders());
    EventUtils.runActions(InteractionUtils.getButtons().get(id).getActionIds(), event, str, tempData);
  }
}
