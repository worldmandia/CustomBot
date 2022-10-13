package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.CBot;
import ua.mani123.discord.interaction.interaction;

import java.util.List;

public class CommandInteraction implements interaction {
    String name;
    String description;
    String actionIds;
    List<String> botIds;
    String successTitle;
    String successDescription;
    List<String> autoCompleteIds;

    CommentedConfig config;

    public CommandInteraction(CommentedConfig config) {
        this.name = config.get("name");
        this.description = config.get("description");
        this.actionIds = config.get("actionIds");
        this.botIds = config.getOrElse("botIds", null);
        this.successTitle = config.getOrElse("successTitle", "successTitle not set");
        this.successDescription = config.getOrElse("successDescription", "successDescription not set");
        this.autoCompleteIds = config.getOrElse("autoCompleteIds", null);
        this.config = config;
    }

    public CommandData getCommand() {
        SlashCommandData commandData = Commands.slash(name.toLowerCase(), description);
        if (!autoCompleteIds.isEmpty()){
            for (String id : autoCompleteIds) {
                String type = config.getOrElse("autocomplete." + id + ".type", "none");
                String name = config.get("autocomplete." + id + ".name");
                String description = config.get("autocomplete." + id + ".description");
                boolean required = config.getOrElse("autocomplete." + id + ".required", false);
                try {
                    commandData.addOption(OptionType.valueOf(type.toUpperCase()), name, description, required);
                } catch (IllegalArgumentException e) {
                    CBot.getLog().warn("Type: " + type + " not found");
                }
            }
        }
        return commandData;

    }

    public List<String> getBotIds() {
        return botIds;
    }

    public String getName() {
        return name;
    }

    public String getSuccessTitle() {
        return successTitle;
    }

    public String getSuccessDescription() {
        return successDescription;
    }
}
