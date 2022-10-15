package ua.mani123.discord.action;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface Action {
    String id = null;

    default void run(GenericInteractionCreateEvent event) {

    }
}
