package ua.mani123.ticket;

import java.util.ArrayList;
import java.util.List;

public enum TicketType {
    TICKET_BLANK("TICKET_BLANK"),
    TICKET_BUTTON("TICKET_BUTTON"),
    TICKET_FORM("TICKET_FORM");


    private final String key;

    TicketType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static List<String> getStrings() {
        List<String> list = new ArrayList<>();
        for (TicketType type : TicketType.values()) {
            list.add(type.getKey());
        }
        return list;
    }
}

