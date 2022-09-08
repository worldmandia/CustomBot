package ua.mani123.interaction.interactions;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.action.ActionUtils;
import ua.mani123.action.actions.BLANK_ACTION;
import ua.mani123.interaction.Interaction;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractionUtils {

    protected static Map<String, Interaction> allInteractions = new HashMap<>();

    public static void preLoad() {
        allInteractions.clear();
        Map<String, Action> preAllAction = new HashMap<>();
        List<Config> allInteractionsConfigs = DTBot.getInteraction().getFileConfig().get("interaction");
        for (Config interaction : allInteractionsConfigs) {
            preAllAction.put(interaction.get("id"), new BLANK_ACTION());
        }
        for (Config interaction : allInteractionsConfigs) {
            if (interaction.get("type").toString().trim().equalsIgnoreCase("BUTTON_INTERACTION")) {
                ArrayList<String> list = interaction.get("action-ids");
                allInteractions.put(interaction.get("id"), new BUTTON_INTERACTION(
                        interaction.get("id"),
                        interaction.get("button-style"),
                        interaction.get("button-text"),
                        Utils.filterAction(preAllAction, list).values()
                ));
            }
        }
    }

    public static void load() {
        allInteractions.clear();
        List<Config> allInteractionsConfigs = DTBot.getInteraction().getFileConfig().get("interaction");
        for (Config interaction : allInteractionsConfigs) {
            if (interaction.get("type").toString().trim().equalsIgnoreCase("BUTTON_INTERACTION")) {
                ArrayList<String> list = interaction.get("action-ids");
                String id = interaction.get("id");
                allInteractions.put(id, new BUTTON_INTERACTION(
                        id,
                        interaction.getOrElse("button-style", "SUCCESS"),
                        interaction.getOrElse("button-text", "Not set"),
                        Utils.filterAction(ActionUtils.getAllActions(), list).values()
                ));
            }
        }
    }

    public static Map<String, Interaction> getAllInteractions() {
        return allInteractions;
    }
}
