package ua.mani123.interaction;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import ua.mani123.DTBot;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractionUtils {

    public static Map<InteractionType, List<Interaction>> interactionSorter(String path) {
        List<CommentedConfig> mapListStrings = Utils.getMap(path, DTBot.getInteraction().getFileConfig());
        Map<InteractionType, List<Interaction>> map = new HashMap<>();
        for (InteractionType type: InteractionType.values()) {
            map.put(type, new ArrayList<>());
        }
        for (CommentedConfig interaction : mapListStrings) {
            switch (InteractionType.valueOf(interaction.get("type"))) {
                case BUTTON -> map.get(InteractionType.BUTTON).add(new ButtonInteraction(
                        interaction.get("button-id"),
                        ButtonStyle.valueOf(interaction.getOrElse("buttonStyle", "SUCCESS").toUpperCase()),
                        interaction.getOrElse("button-text", "Not found"),
                        Actions.valueOf(interaction.getOrElse("action", "CREATE_CHAT").toUpperCase()),
                        interaction.get("category")
                ));
                default -> DTBot.getLogger().info(interaction.get("id") + " wrong type");
            }
        }
        return map;
    }
}
