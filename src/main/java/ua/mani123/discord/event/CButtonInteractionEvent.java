package ua.mani123.discord.event;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.Utils;
import ua.mani123.discord.interaction.InteractionUtils;

public class CButtonInteractionEvent extends ListenerAdapter {

  @Override
  public void onButtonInteraction(ButtonInteractionEvent event) {
    String id = event.getComponentId();
    StringSubstitutor str = new StringSubstitutor(Utils.getPlaceholders());
    EventUtils.runActions(InteractionUtils.getButtons().get(id).getActionIds(), event, str);
  }
}
