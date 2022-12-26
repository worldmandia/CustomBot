package ua.mani123.discord.event.CustomEvents;

import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CustomSelectMenuInteraction extends ListenerAdapter {

  @Override
  public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
    super.onStringSelectInteraction(event);
  }

  @Override
  public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
    super.onEntitySelectInteraction(event);
  }
}
