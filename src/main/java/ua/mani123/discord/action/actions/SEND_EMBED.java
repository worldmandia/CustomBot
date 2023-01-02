package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.awt.Color;
import java.util.ArrayList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.action.subActions.SubAction;
import ua.mani123.discord.action.subActions.subActionsUtils;

public class SEND_EMBED implements Action {

  String title;
  String description;
  Color color;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;
  ArrayList<SubAction> subActions;
  ArrayList<String> subActionIds;
  boolean ephemeral;
  MessageEmbed messageEmbed;

  public SEND_EMBED(CommentedConfig config) {
    this.title = config.getOrElse("title", "");
    this.description = config.getOrElse("description", "");
    this.ephemeral = config.getOrElse("ephemeral", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.subActionIds = config.getOrElse("sub-action-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
    this.subActions = subActionsUtils.enable(subActionIds, config);
    this.color = ActionUtils.getHexToColor(config.getOrElse("color", "ffffff"));
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (messageEmbed == null) {
      messageEmbed = new EmbedBuilder().setTitle(title).setDescription(description).setColor(color).build();
    }
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      if (genericInteractionCreateEvent instanceof IReplyCallback iReplyCallback) {
        iReplyCallback.replyEmbeds(messageEmbed).setEphemeral(ephemeral).addComponents(subActionsUtils.getRows(subActions)).queue();
      } else {
        genericInteractionCreateEvent.getMessageChannel().sendMessageEmbeds(messageEmbed).addComponents(subActionsUtils.getRows(subActions)).queue();
      }
    }
  }

  @Override
  public void runWithPlaceholders(GenericEvent event, StringSubstitutor str, TempData tempData) {
    messageEmbed = new EmbedBuilder().setTitle(str.replace(title)).setDescription(str.replace(description)).setColor(color).build();
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }

}
