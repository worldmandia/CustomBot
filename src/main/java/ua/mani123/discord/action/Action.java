package ua.mani123.discord.action;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface Action {
    void run(GenericInteractionCreateEvent event);
}
