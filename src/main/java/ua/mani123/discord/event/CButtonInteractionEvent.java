package ua.mani123.discord.event;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CButtonInteractionEvent extends ListenerAdapter {

  @Override
  public void onButtonInteraction(ButtonInteractionEvent event) {
    String id = event.getComponentId();
  }
}
