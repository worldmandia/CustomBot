package ua.mani123.action;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
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
            if (action.get("type").toString().trim().equalsIgnoreCase("CREATE_BUTTON_EMBED")) {
                ArrayList<String> list = action.get("button-ids");
                String id = action.get("id");
                allActions.put(id, new CREATE_BUTTON_EMBED(
                        id,
                        action.getOrElse("embed-title", "Not set"),
                        action.getOrElse("embed-description", "Not set"),
                        action.getOrElse("embed-color", "#ffffff"),
                        action.getOrElse("ephemeral", false),
                        Utils.filterInteraction(InteractionUtils.getAllInteractions(), list).values()
                ));
            }
        }
    }

    public static Map<String, Action> getAllActions() {
        return allActions;
    }
}
