package ua.mani123.command;

import com.electronwill.nightconfig.core.CommentedConfig;
import ua.mani123.DTBot;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandUtils {
    public static Map<CommandActions, List<CustomCommand>> commandsSorter(String path) {
        List<CommentedConfig> mapListStrings = Utils.getMap(path, DTBot.getLang().getFileConfig());
        Map<CommandActions, List<CustomCommand>> map = new HashMap<>();
        for (CommandActions type : CommandActions.values()) {
            map.put(type, new ArrayList<>());
        }
        for (CommentedConfig interaction : mapListStrings) {
            try {
                switch (CommandActions.valueOf(interaction.get("action").toString().toUpperCase())) {
                    case CREATE_TICKET_EMBED -> map.get(CommandActions.CREATE_TICKET_EMBED).add(new CustomCommand(
                            interaction.get("name"),
                            interaction.getOrElse("description", "Not found description"),
                            interaction.getOrElse("permission", null)
                    ));
                    default -> DTBot.getLogger().info(interaction.get("action") + " wrong action");
                }
            } catch (Exception e) {
                DTBot.getLogger().error("Error: " + e.getMessage());
            }
        }
        return map;
    }
}
