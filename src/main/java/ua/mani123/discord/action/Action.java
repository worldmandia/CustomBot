package ua.mani123.discord.action;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public interface Action {

    /*
     TODO member.deafen(true).queue();
     TODO member.ban().queue();
     */

    List<Filter> filters = new ArrayList<>();

    /**
     * Run action, recommend to use Action::runWithPlaceholders
     *
     * @param  event  event for run method
     */
    void run(GenericInteractionCreateEvent event);

    /**
     * Run action with placeholders, for add your own placeholders use Utils::getPlaceholders.put
     *
     * @param  event  event for run method
     * @param  str  StringSubstitutor with placeholders map
     */
    void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str);

    default List<Filter> getFilters() {
        return filters;
    }

}
