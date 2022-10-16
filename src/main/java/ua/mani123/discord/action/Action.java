package ua.mani123.discord.action;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;

public interface Action {

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
     * @param  str  StringSubstitutor with placeholder
     */
    void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str);
}
