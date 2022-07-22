package ua.mani123.utils;

import com.electronwill.nightconfig.core.CommentedConfig;
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
        Map<TicketType, Ticket> map = new HashMap<>();
        for (Map.Entry<TicketType, CommentedConfig> ticket : DTBot.getConfig().getTicketMap(path).entrySet()) {
            switch (ticket.getKey()) {
                case TICKET_BUTTON -> map.put(ticket.getKey(), new TicketButton(
                        ticket.getValue().get("id"),
                        ticket.getValue().get("title"),
                        ticket.getValue().get("description"),
                        ticket.getValue().get("category"),
                        ticket.getValue().get("button-style"),
                        ticket.getValue().get("button-text"),
                        ticket.getValue().get("button-emoji")
                ));
                case TICKET_FORM -> map.put(ticket.getKey(), new TicketBlank());
                case TICKET_BLANK -> map.put(ticket.getKey(), new TicketBlank());
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

    public static Map<TicketType, List<String>> getSortedMapIds(Map<TicketType, Ticket> ticketMap) {
        Map<TicketType, List<String>> listMap = new HashMap<>();
        listMap.put(TicketType.TICKET_BUTTON, getByTypeIds(ticketMap, TicketType.TICKET_BUTTON));
        listMap.put(TicketType.TICKET_FORM, getByTypeIds(ticketMap, TicketType.TICKET_FORM));
        listMap.put(TicketType.TICKET_BLANK, getByTypeIds(ticketMap, TicketType.TICKET_BLANK));
        return listMap;
    }
}
