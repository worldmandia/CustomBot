package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.CBot;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandInteraction implements interaction {
    private final String name;
    private final String description;
    private final ArrayList<String> actionIds;
    private final boolean onlyGuild;
    private final ArrayList<String> optionIds;
    private final HashMap<String, List<String>> autocompleteIds = new HashMap<>();
    private final CommentedConfig config;
    private final ArrayList<Filter> filters;

    public CommandInteraction(CommentedConfig config) {
        this.name = config.get("name");
        this.description = config.get("description");
        this.actionIds = config.get("actionsIds");
        this.optionIds = config.get("optionIds");
        this.config = config;
        this.onlyGuild = config.getOrElse("onlyGuild", false);
        ArrayList<String> filtersIds = config.getOrElse("filtersIds", new ArrayList<>());
        ArrayList<CommentedConfig> filtersConfig = new ArrayList<>();
        for (String filter: filtersIds) {
            CommentedConfig commentedConfig = config.get("filter." + filter);
            if (commentedConfig != null) {
                filtersConfig.add(commentedConfig);
            }
        }
        this.filters = filterUtils.enable(filtersConfig);
    }

    public CommandData getCommand() {
        SlashCommandData commandData = Commands.slash(name.toLowerCase(), description);
        if (!optionIds.isEmpty()) {
            for (String id : optionIds) {
                String type = config.getOrElse("option." + id + ".type", "none");
                String name = config.get("option." + id + ".name");
                String description = config.get("option." + id + ".description");
                boolean required = config.getOrElse("option." + id + ".required", false);
                boolean autocomplete = config.getOrElse("option." + id + ".autocomplete", false);
                autocompleteIds.put(name, config.get("option." + id + ".autocompleteIds"));
                try {
                    commandData.addOption(OptionType.valueOf(type.toUpperCase()), name, description, required, autocomplete);
                } catch (Exception e) {
                    CBot.getLog().warn("Type: " + type + " not found");
                }
            }
        }
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
}
