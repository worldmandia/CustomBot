package ua.mani123.action;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.action.actions.CREATE_TEXT_CHAT;
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionUtils {

    protected static Map<String, Action> allActions = new HashMap<>();

    public static void load() {
        allActions.clear();
        List<Config> allActionsConfigs = DTBot.getActions().getFileConfig().get("action");
        for (Config action : allActionsConfigs) {
            String type = action.get("type").toString().trim();
            String id = action.get("id");
            if (type.equalsIgnoreCase("CREATE_BUTTON_EMBED")) {
                ArrayList<String> list = action.get("button-ids");
                allActions.put(id, new CREATE_BUTTON_EMBED(
                        id,
                        action.getOrElse("embed-title", "Not set"),
                        action.getOrElse("embed-description", "Not set"),
                        action.getOrElse("embed-color", "#ffffff"),
                        action.getOrElse("ephemeral", false),
                        Utils.filterInteraction(InteractionUtils.getAllInteractions(), list).values()
                ));
            } else if (type.equalsIgnoreCase("CREATE_TEXT_CHAT")){
                allActions.put(id, new CREATE_TEXT_CHAT(id,
                        action.getOrElse("action-name", "Not set"),
                        action.getOrElse("action-description", "Not set"),
                        action.getIntOrElse("counter", 0),
                        action.getOrElse("category-name", "Not set"),
                        action
                ));
            } else {
                DTBot.getLogger().warn(type + " not found");
            }
        }
    }

    public static Map<String, Action> getAllActions() {
        return allActions;
    }
}
