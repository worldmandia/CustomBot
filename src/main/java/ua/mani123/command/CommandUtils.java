package ua.mani123.command;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.action.ActionUtils;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandUtils {
    protected static Map<String, CustomCommand> allCommands = new HashMap<>();

    public static void load() {
        allCommands.clear();
        List<Config> allCommandsConfigs = DTBot.getCommands().get("commands");
        for (Config cmd : allCommandsConfigs) {
            ArrayList<String> list = cmd.get("action-ids");
            allCommands.put(cmd.get("name"), new CustomCommand(
                    cmd.get("name"),
                    cmd.get("description"),
                    cmd.get("permissions"),
                    Utils.filterAction(ActionUtils.getAllActions(), list).values()
            ));
        }
    }

    public static Map<String, CustomCommand> getAllCommands() {
        return allCommands;
    }
}
