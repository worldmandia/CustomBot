package ua.mani123.discord.event;

import java.util.ArrayList;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;

public class EventUtils {

  public static void runActions(ArrayList<String> actionIds, GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData){
    if (actionIds != null) {
      for (String actionId : actionIds) {
        if (ActionUtils.getActionMap().containsKey(actionId)) {
          ArrayList<Action> actionArrayList = ActionUtils.getActionMap().get(actionId);
          for (Action action : actionArrayList) {
            if (filterUtils.filterCheck(action.getFilters(), event, str, tempData)) {
              action.runWithPlaceholders(event, str, tempData);
            }
          }
        } else {
          CBot.getLog().warn(actionId + " - not found");
        }
      }
    }
  }

}
