package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

import java.util.ArrayList;
import java.util.List;

public class SEND_MESSAGE implements Action {

    String message;
    List<Filter> filters;

    public String getMessage() {
        return message;
    }

    public SEND_MESSAGE(CommentedFileConfig config) {
        this.message = config.getOrElse("message", "null");
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        event.getMessageChannel().sendMessage(message).queue();
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        event.getMessageChannel().sendMessage(str.replace(message)).queue();
    }
}
