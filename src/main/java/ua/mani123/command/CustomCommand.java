package ua.mani123.command;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.action.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomCommand {
    String name;
    String description;
    List<String> permissions;
    Collection<Action> actions;

    public CustomCommand(String name, String description, List<String> permissions, Collection<Action> actions) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public static class settings {
        public static List<SlashCommandData> getCommandData(Collection<CustomCommand> customCommandList) {
            ArrayList<SlashCommandData> commandDataArrayList = new ArrayList<>();
            for (CustomCommand cmd : customCommandList) {
                commandDataArrayList.add(Commands.slash(cmd.getName().toLowerCase(), cmd.getDescription()));
            }
            return commandDataArrayList;
        }
    }

}
