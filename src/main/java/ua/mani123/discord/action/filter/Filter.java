package ua.mani123.discord.action.filter;

import java.util.ArrayList;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;

public interface Filter {

  boolean defaultCanRun = false;

  /**
   * Check actions filters and return true if can or false if cant
   *
   * @param event event for run method
   */
  default boolean canRun(GenericInteractionCreateEvent event) {
    return defaultCanRun;
  }

  /**
   * Check actions filters and return true if can or false if cant
   *
   * @param event event for run method
   */
  default boolean canRun(GenericGuildEvent event) {
    return defaultCanRun;
  }

  /**
   * Check actions filters and return true if can or false if cant
   *
   * @param event event for run method
   */
  default boolean canRun(GenericSessionEvent event) {
    return defaultCanRun;
  }

  ArrayList<String> getFilterActionIds();

}
