package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.interaction;

public class CommandInteraction implements interaction {
  private final String name;
  private final String description;
  private final ArrayList<String> actionIds;
  private final boolean onlyGuild;
  private final boolean isNSFW;
  private final ArrayList<String> optionIds;
  private final HashMap<String, List<String>> autocompleteIds = new HashMap<>();
  private final CommentedConfig config;
  private final ArrayList<Filter> filters;
  private final SlashCommandData commandData;

  public CommandInteraction(CommentedConfig config) {
    this.name = config.get("name");
    this.description = config.get("description");
    this.actionIds = config.get("actionsIds");
    this.optionIds = config.get("optionIds");
    this.config = config;
    this.onlyGuild = config.getOrElse("onlyGuild", false);
    this.isNSFW = config.getOrElse("isNSFW", false);
    ArrayList<String> filtersIds = config.getOrElse("filtersIds", new ArrayList<>());
    ArrayList<CommentedConfig> filtersConfig = new ArrayList<>();
    for (String filter : filtersIds) {
      CommentedConfig commentedConfig = config.get("filter." + filter);
      if (commentedConfig != null) {
        filtersConfig.add(commentedConfig);
      }
    }
    this.filters = filterUtils.enable(filtersConfig);


    this.commandData = Commands.slash(name.toLowerCase(), description).setNSFW(isNSFW);
    ArrayList<OptionData> optionDataArrayList = new ArrayList<>();
    if (!optionIds.isEmpty()) {
      for (String id : optionIds) {
        String type = config.getOrElse("option." + id + ".type", "none");
        String name = config.getOrElse("option." + id + ".name", "");
        String description = config.getOrElse("option." + id + ".description", "");
        ArrayList<String> choiceData = config.getOrElse("option." + id + ".choiceData", new ArrayList<>());
        boolean required = config.getOrElse("option." + id + ".required", false);
        boolean autocomplete = config.getOrElse("option." + id + ".autocomplete", false);
        autocompleteIds.put(name, config.get("option." + id + ".autocompleteIds"));
        OptionData tempOptionData = new OptionData(OptionType.valueOf(type.toUpperCase()), name, description, required, autocomplete);
        if (!choiceData.isEmpty()) {
          choiceData.forEach(s -> {
            String[] mas = s.split("::", 2);
            tempOptionData.addChoice(mas[0], mas[1]);
          });
        }
        optionDataArrayList.add(tempOptionData);
      }
    }
    this.commandData.addOptions(optionDataArrayList);
  }

  public SlashCommandData getCommandData() {
    return commandData;
  }

  public HashMap<String, List<String>> getAutocompleteIds() {
    return autocompleteIds;
  }

  public String getName() {
    return name;
  }

  public List<String> getActionIds() {
    return actionIds;
  }

  public boolean isOnlyGuild() {
    return onlyGuild;
  }

  public CommentedConfig getConfig() {
    return config;
  }

  public ArrayList<Filter> getFilters() {
    return filters;
  }

  public String getDescription() {
    return description;
  }

  public boolean isNSFW() {
    return isNSFW;
  }

  public ArrayList<String> getOptionIds() {
    return optionIds;
  }
}
