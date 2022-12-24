package ua.mani123.discord.action;

import java.util.ArrayList;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.filter.Filter;

public interface Action {

  ArrayList<Filter> filters = new ArrayList<>();

  /**
   * Run action, recommend to use Action::runWithPlaceholders
   *
   * @param event event for run method
   */
  void run(GenericInteractionCreateEvent event);

  /**
   * Run action with placeholders, for add your own placeholders use Utils::getPlaceholders.put
   *
   * @param event event for run method
   * @param str   StringSubstitutor with placeholders map
   */
  default void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
    run(event);
  }

  default ArrayList<Filter> getFilters() {
    return filters;
  }

}
