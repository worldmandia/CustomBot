package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.action.subActions.SubAction;
import ua.mani123.discord.action.subActions.subActionsUtils;

import java.util.ArrayList;
import java.util.List;

public class SEND_MESSAGE implements Action {

    String message;
    List<Filter> filters;
    List<SubAction> subActions;

    public String getMessage() {
        return message;
    }

    public SEND_MESSAGE(CommentedFileConfig config) {
        this.message = config.getOrElse("message", " ");
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
        this.subActions = subActionsUtils.enable(config.getOrElse("sub-action", new ArrayList<>()));
    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessage(message);
        if (!subActions.isEmpty()){
            for (SubAction s : subActions) {
                messageCreateAction.addActionRow(s.getComponent());
            }
        }
        messageCreateAction.queue();
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessage(str.replace(message));
        if (!subActions.isEmpty()){
            for (SubAction s : subActions) {
                messageCreateAction.addActionRow(s.getComponent());
            }
        }
        messageCreateAction.queue();
    }
}
