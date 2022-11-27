package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
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
    boolean ephemeral;
    List<Filter> filters;
    List<SubAction> subActions;


    public SEND_MESSAGE(CommentedFileConfig config) {
        this.message = config.getOrElse("message", " ");
        this.ephemeral = config.getOrElse("ephemeral", false);
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
        this.subActions = subActionsUtils.enable(config.getOrElse("sub-action", new ArrayList<>()));
    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        if (ephemeral) {
            ReplyCallbackAction replyCallbackAction = null;
            if (event instanceof GenericCommandInteractionEvent commandInteractionEvent) {
                replyCallbackAction = commandInteractionEvent.reply(message).setEphemeral(ephemeral);
            } else if (event instanceof GenericComponentInteractionCreateEvent componentInteractionCreateEvent){
                replyCallbackAction = componentInteractionCreateEvent.reply(message).setEphemeral(ephemeral);
            }
            if (!subActions.isEmpty()) {
                for (SubAction s : subActions) {
                    replyCallbackAction.addActionRow(s.getComponent());
                }
            }
            replyCallbackAction.queue();
        }
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessage(message);
        if (!subActions.isEmpty()) {
            for (SubAction s : subActions) {
                messageCreateAction.addActionRow(s.getComponent());
            }
        }
        messageCreateAction.queue();
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessage(str.replace(message));
        if (!subActions.isEmpty()) {
            for (SubAction s : subActions) {
                messageCreateAction.addActionRow(s.getComponent());
            }
        }
        messageCreateAction.queue();
    }

}
