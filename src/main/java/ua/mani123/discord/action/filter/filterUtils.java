package ua.mani123.discord.action.filter;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.filter.filters.BOT;
import ua.mani123.discord.action.filter.filters.GUILD;
import ua.mani123.discord.action.filter.filters.ROLE;
import ua.mani123.discord.action.filter.filters.USER;

public class filterUtils {

  public static ArrayList<Filter> enable(List<CommentedConfig> configs) {
    ArrayList<Filter> filterList = new ArrayList<>();
    if (!configs.isEmpty()) {
      for (CommentedConfig config : configs) {
        String type = config.get("type");
        if (type != null) {
          type = type.toUpperCase().trim();
          switch (type) {
            case "ROLE" -> filterList.add(new ROLE(config));
            case "USER" -> filterList.add(new USER(config));
            case "GUILD" -> filterList.add(new GUILD(config));
            case "BOT" -> filterList.add(new BOT(config));
          }
        }
      }
    }
    return filterList;
  }

  public static boolean filterCheck(ArrayList<Filter> filters, GenericInteractionCreateEvent event, StringSubstitutor str) {
    boolean canInteract = true;
    if (!filters.isEmpty()) {
      for (Filter filter : filters) {
        canInteract = filter.canRun(event);
        if (!canInteract) {
          for (String actionId : filter.getFilterActionIds()) {
            ActionUtils.getActionMap().get(actionId).runWithPlaceholders(event, str);
          }
        }
      }
    }
    return canInteract;
  }

  public static boolean filterCheck(ArrayList<Filter> filters, GenericGuildEvent event) {
    boolean canInteract = true;
    if (!filters.isEmpty()) {
      for (Filter filter : filters) {
        if (canInteract) {
          canInteract = filter.canRun(event);
        }
      }
    }
    return canInteract;
  }

  public static boolean filterCheck(ArrayList<Filter> filters, GenericSessionEvent event) {
    boolean canInteract = true;
    if (!filters.isEmpty()) {
      for (Filter filter : filters) {
        if (canInteract) {
          canInteract = filter.canRun(event);
        }
      }
    }
    return canInteract;
  }

}
