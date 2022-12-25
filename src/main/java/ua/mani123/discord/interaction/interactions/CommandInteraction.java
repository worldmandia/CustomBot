package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashMap;
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
  private final HashMap<String, ArrayList<String>> autocompleteIds = new HashMap<>();
  private final CommentedConfig config;
  private final ArrayList<Filter> filters;
  private final ArrayList<String> filterIds;
  private final SlashCommandData commandData;

  public CommandInteraction(CommentedConfig config) {
    this.name = config.get("name");
    this.description = config.get("description");
    this.actionIds = config.getOrElse("actionsIds", new ArrayList<>());
    this.optionIds = config.getOrElse("optionIds", new ArrayList<>());
    this.config = config;
    this.onlyGuild = config.getOrElse("onlyGuild", false);
    this.isNSFW = config.getOrElse("isNSFW", false);
    this.filterIds = config.getOrElse("filtersIds", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);


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

  public HashMap<String, ArrayList<String>> getAutocompleteIds() {
    return autocompleteIds;
  }

  public String getName() {
    return name;
  }

  public ArrayList<String> getActionIds() {
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

  public ArrayList<String> getFilterIds() {
    return filterIds;
  }
}
