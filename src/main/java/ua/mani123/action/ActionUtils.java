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
                allActions.put(action.get("id"), new CREATE_BUTTON_EMBED(
                        action.get("id"),
                        action.get("embed-title"),
                        action.get("embed-description"),
                        action.get("embed-color"),
                        Utils.filterInteraction(InteractionUtils.getAllInteractions(), list).values()
                ));
            }
        }
    }

    public static Map<String, Action> getAllActions() {
        return allActions;
    }
}
