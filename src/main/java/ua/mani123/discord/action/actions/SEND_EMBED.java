package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.action.subActions.SubAction;
import ua.mani123.discord.action.subActions.subActionsUtils;

import java.awt.*;
import java.util.ArrayList;

public class SEND_EMBED implements Action {

    String title;
    String description;
    Color color;
    ArrayList<Filter> filters;
    ArrayList<SubAction> subActions;
    boolean ephemeral;

    public SEND_EMBED(CommentedFileConfig config) {
        this.title = config.getOrElse("title", "");
        this.description = config.getOrElse("description", "");
        this.ephemeral = config.getOrElse("ephemeral", false);
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
        this.subActions = subActionsUtils.enable(config.getOrElse("sub-action", new ArrayList<>()));
        this.color = actionUtils.getHexToColor(config.getOrElse("color", "ffffff"));
    }

    MessageEmbed messageEmbed;

    @Override
    public void run(GenericInteractionCreateEvent event) {
        if (messageEmbed == null) {
            messageEmbed = new EmbedBuilder().setTitle(title).setDescription(description).setColor(color).build();
        }
        if (ephemeral) {
            ReplyCallbackAction replyCallbackAction = null;
            if (event instanceof GenericCommandInteractionEvent commandInteractionEvent) {
                replyCallbackAction = commandInteractionEvent.replyEmbeds(messageEmbed).setEphemeral(ephemeral);
            } else if (event instanceof GenericComponentInteractionCreateEvent componentInteractionCreateEvent){
                replyCallbackAction = componentInteractionCreateEvent.replyEmbeds(messageEmbed).setEphemeral(ephemeral);
            }
            if (!subActions.isEmpty()) {
                for (SubAction s : subActions) {
                    replyCallbackAction.addActionRow(s.getComponent());
                }
            }
            replyCallbackAction.queue();
        }
        if (!subActions.isEmpty()){
            MessageCreateAction messageCreateAction = null;
            for (SubAction s : subActions) {
                messageCreateAction = event.getMessageChannel().sendMessageEmbeds(messageEmbed).addActionRow(s.getComponent());
            }
            messageCreateAction.queue();
        }
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        messageEmbed = new EmbedBuilder().setTitle(str.replace(title)).setDescription(str.replace(description)).setColor(color).build();
        run(event);
    }

    @Override
    public ArrayList<Filter> getFilters() {
        return filters;
    }

}
