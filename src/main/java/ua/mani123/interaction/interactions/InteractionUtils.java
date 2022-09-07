package ua.mani123.interaction.interactions;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.interaction.Interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractionUtils {

    protected static Map<String, Interaction> allInteractions = new HashMap<>();

    public static void load() {
        List<Config> allInteractionsConfigs = DTBot.getInteraction().getFileConfig().get("interaction");
        for (Config interaction : allInteractionsConfigs) {
            String type = interaction.get("type");
            type = type.trim().toLowerCase();
            if (type.equals("button")) {
                allInteractions.put(interaction.get("id"), new BUTTON_INTERACTION(
                        interaction.get("id"),
                        interaction.get("button-style"),
                        interaction.get("button-text"),
                        null
                ));
            }
        }
    }
}
