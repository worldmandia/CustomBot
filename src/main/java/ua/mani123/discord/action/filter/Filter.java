package ua.mani123.discord.action.filter;

import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;

import java.util.ArrayList;

public interface Filter {
    boolean defaultCanRun = true;
    ArrayList<String> filterActionIds = new ArrayList<>();

    /**
     * Check actions filters and return true if can or false if cant
     *
     * @param event event for run method
     */
    default boolean canRun(GenericInteractionCreateEvent event) {
        return defaultCanRun;
    }

    boolean canRun(GenericGuildEvent event);

    boolean canRun(GenericSessionEvent event);

    default ArrayList<String> getFilterActionIds() {
        return filterActionIds;
    }

}
