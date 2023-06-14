package ua.mani123.discordModule.additions;

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

    public COMMAND_OPTION(String type, String id, ArrayList<String> choices, String optionType, String description, boolean required, boolean autoComplete) {
        super(type, id);
        this.choices = choices;
        this.optionType = optionType.toUpperCase();
        this.description = description;
        this.required = required;
        this.autoComplete = autoComplete;

        for (String s: choices) {
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
