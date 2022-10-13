package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import ua.mani123.discord.interaction.interaction;

import java.util.List;

public class CommandInteraction implements interaction {
    String name;
    String description;
    String actionIds;
    List<String> botIds;
    String successTitle;

    String successDescription;

    public CommandInteraction(CommentedConfig config) {
        this.name = config.get("name");
        this.description = config.get("description");
        this.actionIds = config.get("actionIds");
        this.botIds = config.getOrElse("botIds", null);
        this.successTitle = config.getOrElse("successTitle", "successTitle not set");
        this.successDescription = config.getOrElse("successDescription", "successDescription not set");
    }

    public CommandData getCommand(){
        return Commands.slash(name.toLowerCase(), description);
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
