package ua.mani123.utils;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Placeholder {

    public static String use(String s, GenericInteractionCreateEvent event, String action) {
        return s
                .replaceAll("%username-mentioned%", event.getUser().getAsMention())
                .replaceAll("%username%", event.getUser().getName())
                .replaceAll("%data%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .replaceAll("%action%", action);
    }

    public static String use(int counter, String s, GenericInteractionCreateEvent event, String action) {
        return s
                .replaceAll("%username-mentioned%", event.getUser().getAsMention())
                .replaceAll("%username%", event.getUser().getName())
                .replaceAll("%counter%", String.valueOf(counter))
                .replaceAll("%data%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .replaceAll("%action%", action);
    }
}
