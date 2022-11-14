package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.action.subActions.SubAction;
import ua.mani123.discord.action.subActions.subActionsUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SEND_EMBED implements Action {

    String title;
    String description;
    Color color;
    List<Filter> filters;
    List<SubAction> subActions;

    public SEND_EMBED(CommentedFileConfig config) {
        this.title = config.getOrElse("title", "");
        this.description = config.getOrElse("description", "");
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
        this.subActions = subActionsUtils.enable(config.getOrElse("sub-action", new ArrayList<>()));
        this.color = actionUtils.getHexToColor(config.getOrElse("color", "ffffff"));
    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessageEmbeds(new EmbedBuilder().setTitle(title).setDescription(description).setColor(color).build());
        if (!subActions.isEmpty()){
            for (SubAction s : subActions) {
                messageCreateAction.addActionRow(s.getComponent());
            }
        }
        messageCreateAction.queue();
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessageEmbeds(new EmbedBuilder().setTitle(str.replace(title)).setDescription(str.replace(description)).setColor(color).build());
        if (!subActions.isEmpty()){
            for (SubAction s : subActions) {
                messageCreateAction.addActionRow(s.getComponent());
            }
        }
        messageCreateAction.queue();
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
