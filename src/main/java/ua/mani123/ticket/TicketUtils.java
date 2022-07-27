package ua.mani123.ticket;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
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
        map.put(TicketType.TICKET_BUTTON, new ArrayList<>());
        for (CommentedConfig ticket : mapListStrings) {
            switch (TicketType.valueOf(ticket.get("type"))) {
                case TICKET_BUTTON -> map.get(TicketType.TICKET_BUTTON).add(new TicketButton(
                        ticket.get("id"),
                        ticket.getOrElse("title", "Not found"),
                        ticket.getOrElse("description", "Not found"),
                        ticket.getOrElse("embed-color", "#000000"),
                        ticket.get("button-ids")
                ));
                //case TICKET_FORM -> map.put(ticket.getKey(), new TicketBlank());
                //case TICKET_BLANK -> map.put(ticket.getKey(), new TicketBlank());
                default -> DTBot.getLogger().info(ticket.get("id") + " wrong type");
            }
        }
        return map;
    }

    public static List<String> getByTypeIds(Map<TicketType, Ticket> ticketMap, TicketType type) {
        List<String> ids = new ArrayList<>();
        for (Map.Entry<TicketType, Ticket> list : ticketMap.entrySet()) {
            if (list.getKey() == type) {
                ids.add(list.getValue().getId());
            }
        }
        return ids;
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
