package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.Interaction;

public class ButtonInteraction implements Interaction {

  private final String id;
  private final ArrayList<String> actionIds;
  private final ArrayList<String> filterIds;
  private final ArrayList<Filter> filters;

  public ButtonInteraction(CommentedConfig config) {
    this.id = config.getOrElse("id", "");
    this.actionIds = config.getOrElse("actionsIds", new ArrayList<>());
    this.filterIds = config.getOrElse("filtersIds", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
  }

  public ArrayList<Filter> getFilters() {
    return filters;
  }

  public String getId() {
    return id;
  }

  public ArrayList<String> getActionIds() {
    return actionIds;
  }

  public ArrayList<String> getFilterIds() {
    return filterIds;
  }
}
