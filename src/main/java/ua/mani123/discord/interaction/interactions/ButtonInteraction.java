package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.interaction;

public class ButtonInteraction implements interaction {

  private final String id;
  private final ArrayList<String> actionIds;
  private final ArrayList<Filter> filters;

  public ButtonInteraction(CommentedConfig config) {
    this.id = config.getOrElse("id", "");
    this.actionIds = config.getOrElse("actionsIds", new ArrayList<>());
    ArrayList<String> filtersIds = config.getOrElse("filtersIds", new ArrayList<>());
    ArrayList<CommentedConfig> filtersConfig = new ArrayList<>();
    for (String filter : filtersIds) {
      CommentedConfig commentedConfig = config.get("filter." + filter);
      if (commentedConfig != null) {
        filtersConfig.add(commentedConfig);
      }
    }
    this.filters = filterUtils.enable(filtersConfig);
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
}
