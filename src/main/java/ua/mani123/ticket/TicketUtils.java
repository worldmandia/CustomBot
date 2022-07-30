package ua.mani123.ticket;

import com.electronwill.nightconfig.core.CommentedConfig;
import ua.mani123.DTBot;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketUtils {
    public static Map<TicketType, List<Ticket>> ticketSorter(String path) {
        List<CommentedConfig> mapListStrings = Utils.getMap(path, DTBot.getConfig().getFileConfig());
        Map<TicketType, List<Ticket>> map = new HashMap<>();
        for (TicketType type : TicketType.values()) {
            map.put(type, new ArrayList<>());
        }
        for (CommentedConfig ticket : mapListStrings) {
            try {
                switch (TicketType.valueOf(ticket.get("type"))) {
                    case TICKET_BUTTON -> map.get(TicketType.TICKET_BUTTON).add(new TicketButton(
                            ticket.getOrElse("id", "-1"),
                            ticket.getOrElse("title", "Not found"),
                            ticket.getOrElse("description", "Not found"),
                            ticket.getOrElse("embed-color", "#000000"),
                            ticket.get("button-ids")
                    ));
                    //case TICKET_FORM -> map.put(ticket.getKey(), new TicketBlank());
                    //case TICKET_BLANK -> map.put(ticket.getKey(), new TicketBlank());
                    default -> DTBot.getLogger().info(ticket.get("id") + " wrong type");
                }
            } catch (Exception e) {
                DTBot.getLogger().error("Error: " + e.getMessage());
            }
        }
        return map;
    }

    public static Ticket getTicketById(String type, String id, Map<TicketType, List<Ticket>> tickets) {
        for (Ticket entry : tickets.get(TicketType.valueOf(type))) {
            if (entry.getId().equals(id)) {
                return entry;
            }
        }
        return null;
    }
}
