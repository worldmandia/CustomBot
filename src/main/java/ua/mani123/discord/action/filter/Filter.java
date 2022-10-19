package ua.mani123.discord.action.filter;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface Filter {
    boolean defaultCanRun = true;

    /**
     * Check actions filters and return true if can or false if cant
     *
     * @param event event for run method
     */
    default boolean canRun(GenericInteractionCreateEvent event) {
        return defaultCanRun;
    }

}
