package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.CBot;
import ua.mani123.discord.interaction.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandInteraction implements interaction {
    private final String name;
    private final String description;
    private final ArrayList<String> actionIds;
    private final ArrayList<String> botIds;
    private final String successTitle;
    private final String successDescription;
    private final ArrayList<String> optionIds;
    private final HashMap<String, List<String>> autocompleteIds = new HashMap<>();
    private final CommentedConfig config;

    public CommandInteraction(CommentedConfig config) {
        this.name = config.get("name");
        this.description = config.get("description");
        this.actionIds = config.get("actionsIds");
        this.botIds = config.get("botIds");
        this.successTitle = config.getOrElse("successTitle", "successTitle not set");
        this.successDescription = config.getOrElse("successDescription", "successDescription not set");
        this.optionIds = config.get("optionIds");
        this.config = config;
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

    public List<String> getBotIds() {
        return botIds;
    }

    public String getName() {
        return name;
    }

    public List<String> getActionIds() {
        return actionIds;
    }

    public String getSuccessTitle() {
        return successTitle;
    }

    public String getSuccessDescription() {
        return successDescription;
    }
}
