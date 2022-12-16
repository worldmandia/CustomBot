package ua.mani123.discord.event;

import java.util.List;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.filter.filterUtils;

public class EventUtils {

  public static void runActions(List<String> actionIds, GenericInteractionCreateEvent event, StringSubstitutor str){
    if (actionIds != null) {
      for (String actionId : actionIds) {
        if (ActionUtils.getActionMap().containsKey(actionId)) {
          Action action = ActionUtils.getActionMap().get(actionId);
          if (filterUtils.filterCheck(action.getFilters(), event, str)) {
            action.runWithPlaceholders(event, str);
          }
        } else {
          CBot.getLog().warn(actionId + " - not found");
        }
      }
    }
  }

}
