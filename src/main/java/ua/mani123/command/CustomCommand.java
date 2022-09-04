package ua.mani123.command;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.List;

public class CustomCommand {
    String name;
    String description;
    List<String> permissions;
    List<String> actionsIds;

    public CustomCommand(String name, String description, List<String> permissions, List<String> actionsIds) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.actionsIds = actionsIds;
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

    public List<String> getActionsIds() {
        return actionsIds;
    }

    public static class settings {
        public static List<CustomCommand> getCommands(FileConfig cfg) {
            ArrayList<CustomCommand> completeCmd = new ArrayList<>();
            List<Config> listOfCmd = cfg.get("commands");
            for (Config cmd : listOfCmd) {
                completeCmd.add(new CustomCommand(
                        cmd.get("name"),
                        cmd.get("description"),
                        cmd.get("permissions"),
                        cmd.get("action-ids")
                ));
            }
            return completeCmd;
        }

        public static List<SlashCommandData> getCommandData(List<CustomCommand> customCommandList) {
            ArrayList<SlashCommandData> commandDataArrayList = new ArrayList<>();
            for (CustomCommand cmd : customCommandList) {
                commandDataArrayList.add(Commands.slash(cmd.getName(), cmd.getDescription()));
            }
            return commandDataArrayList;
        }
    }

}
