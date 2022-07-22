package ua.mani123.utils;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.DTBot;
import ua.mani123.ticket.Ticket;
import ua.mani123.ticket.TicketBlank;
import ua.mani123.ticket.TicketButton;
import ua.mani123.ticket.TicketType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static Map<TicketType, Ticket> ticketSorter(String path) {
        Map<TicketType, Config> mapListStrings = DTBot.getConfig().getTicketMap(path);
        Map<TicketType, Ticket> map = new HashMap<>();
            for (Map.Entry<TicketType, Config> ticket : mapListStrings.entrySet()) {
                switch (ticket.getKey()) {
                    case TICKET_BUTTON -> map.put(ticket.getKey(), new TicketButton(
                            ticket.getValue().get("id"),
                            ticket.getValue().getOrElse("title", "Not found"),
                            ticket.getValue().getOrElse("description", "Not found"),
                            ticket.getValue().getOrElse("embed-color", "#000000"),
                            ticket.getValue().getOrElse("category", "0"),
                            ticket.getValue().getOrElse("button-style", "SECONDARY"),
                            ticket.getValue().get("button-id"),
                            ticket.getValue().getOrElse("button-text", "Not found")
                    ));
                    //case TICKET_FORM -> map.put(ticket.getKey(), new TicketBlank());
                    //case TICKET_BLANK -> map.put(ticket.getKey(), new TicketBlank());
                    default -> map.put(ticket.getKey(), new TicketBlank());
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

    public static Ticket getTicketById(String id, Map<TicketType, Ticket> tickets){
        for (Map.Entry<TicketType, Ticket> entry: tickets.entrySet()) {
            if (entry.getValue().getId().equals(id)){
                return entry.getValue();
            }
        }
        return new TicketBlank();
    }

    public static Map<TicketType, List<String>> getSortedMapIds(Map<TicketType, Ticket> ticketMap) {
        Map<TicketType, List<String>> listMap = new HashMap<>();
        listMap.put(TicketType.TICKET_BUTTON, getByTypeIds(ticketMap, TicketType.TICKET_BUTTON));
        listMap.put(TicketType.TICKET_FORM, getByTypeIds(ticketMap, TicketType.TICKET_FORM));
        listMap.put(TicketType.TICKET_BLANK, getByTypeIds(ticketMap, TicketType.TICKET_BLANK));
        return listMap;
    }
}
