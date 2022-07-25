package ua.mani123.interaction;

import com.electronwill.nightconfig.core.Config;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import ua.mani123.DTBot;
import ua.mani123.ticket.Ticket;
import ua.mani123.ticket.TicketType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractionUtils {

    public static Map<InteractionType, Interaction> interactionSorter(String path) {
        Map<InteractionType, Config> mapListStrings = DTBot.getConfig().getInteractionMap(path);
        Map<InteractionType, Interaction> map = new HashMap<>();
        for (Map.Entry<InteractionType, Config> interaction : mapListStrings.entrySet()) {
            switch (interaction.getKey()) {
                case BUTTON -> map.put(interaction.getKey(), new ButtonInteraction(
                        interaction.getValue().get("id"),
                        ButtonStyle.valueOf(interaction.getValue().getOrElse("buttonStyle", "SUCCESS").toUpperCase()),
                        interaction.getValue().getOrElse("button-text", "Not found"),
                        Actions.valueOf(interaction.getValue().getOrElse("action", "CREATE_CHAT").toUpperCase()),
                        interaction.getValue().get("category")
                ));
                default -> DTBot.getLOGGER().info(interaction.getValue().get("id") + " wrong type");
            }
        }
        return map;
    }

    public static List<Interaction> getInteractionByType(Map<InteractionType, Interaction> interactionMap, InteractionType type) {
        List<Interaction> interactionList = new ArrayList<>();
        for (Map.Entry<InteractionType, Interaction> entry: interactionMap.entrySet()) {
            if (entry.getKey() == type){
                interactionList.add(entry.getValue());
            }
        }
        return interactionList;
    }
}
