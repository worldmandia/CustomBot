package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.Interaction;

public class ModalInteraction implements Interaction {

  private final String modalId;
  private final ArrayList<String> actionIds;
  private final ArrayList<String> filterIds;
  private final ArrayList<String> placeholderValues;
  private final ArrayList<Filter> filters;

  public ModalInteraction(CommentedConfig config) {
    this.modalId = config.getOrElse("modalId", "");
    this.actionIds = config.getOrElse("actionsIds", new ArrayList<>());
    this.filterIds = config.getOrElse("filtersIds", new ArrayList<>());
    this.placeholderValues = config.getOrElse("placeholderValues", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
  }

  public ArrayList<Filter> getFilters() {
    return filters;
  }

  public String getModalId() {
    return modalId;
  }

  public ArrayList<String> getPlaceholderValues() {
    return placeholderValues;
  }

  public ArrayList<String> getActionIds() {
    return actionIds;
  }

  public ArrayList<String> getFilterIds() {
    return filterIds;
  }

}
