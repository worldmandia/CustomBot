package ua.mani123.action;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.action.actions.CHECK_MIN_MAX_FROM_DATABASE;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.action.actions.CREATE_TEXT_CHAT;
import ua.mani123.action.actions.CREATE_VOICE_CHAT;
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
        List<Config> allActionsConfigs = DTBot.getActions().get("action");
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
                        Utils.filterInteraction(InteractionUtils.getAllInteractions(), list).values()
                ));
            } else if (type.equalsIgnoreCase("CREATE_TEXT_CHAT")) {
                allActions.put(id, new CREATE_TEXT_CHAT(id,
                        action.getOrElse("action-name", "Not set"),
                        action.getOrElse("action-description", "Not set"),
                        action.getIntOrElse("counter", 0),
                        action.getOrElse("category-name", "Not set"),
                        action
                ));
            } else if (type.equalsIgnoreCase("CREATE_VOICE_CHAT")) {
                allActions.put(id, new CREATE_VOICE_CHAT(id,
                        action.getOrElse("action-name", "Not set"),
                        action.getIntOrElse("counter", 0),
                        action.getOrElse("category-name", "Not set"),
                        action
                ));
            } else if (type.equalsIgnoreCase("CHECK_MIN_MAX_FROM_DATABASE")){
                allActions.put(id, new CHECK_MIN_MAX_FROM_DATABASE(id,
                        action.getIntOrElse("min", 0),
                        action.getIntOrElse("max", 0),
                        action.get("section")
                ));
            }
            else {
                DTBot.getLogger().warn(type + " not found");
            }
        }
    }

    public static Map<String, Action> getAllActions() {
        return allActions;
    }
}
