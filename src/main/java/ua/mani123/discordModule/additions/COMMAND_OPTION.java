package ua.mani123.discordModule.additions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;
import java.util.List;

public class COMMAND_OPTION extends DiscordConfigs.Addition {

    ArrayList<String> choices;
    String optionType;
    String description;
    boolean required;
    boolean autoComplete;

    List<Command.Choice> choiceList = new ArrayList<>();

    public COMMAND_OPTION(String type, CommentedConfig config) {
        super(type, config.getOrElse("id", "not_set"));
        this.choices = config.getOrElse("choices", new ArrayList<>());
        this.optionType = config.getOrElse("optionType", "STRING").toUpperCase();
        this.description = config.getOrElse("description", "not_set");
        this.required = config.getOrElse("required", false);
        this.autoComplete = config.getOrElse("autoComplete", false);

        for (String s : choices) {
            String[] parts = s.split(":");
            if (parts.length == 2) choiceList.add(new Command.Choice(parts[0], parts[1]));
            else getLogger().warn("Wrong choice: " + s);
        }
    }

    public OptionData get() {
        try {
            return new OptionData(OptionType.valueOf(optionType), getId().toLowerCase(), description, required, autoComplete).addChoices(choiceList);
        } catch (Exception e) {
            getLogger().warn(e.getMessage() + " in addition with id: " + getId());
            return null;
        }
    }
}
