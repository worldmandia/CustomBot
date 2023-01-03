package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.action.subActions.SubAction;
import ua.mani123.discord.action.subActions.subActionsUtils;

public class SEND_MESSAGE implements Action {

  String message;
  boolean ephemeral;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;
  ArrayList<SubAction> subActions;
  String tempMessage;
  ArrayList<String> subActionIds;

  public SEND_MESSAGE(CommentedConfig config) {
    this.message = config.getOrElse("message", "");
    this.ephemeral = config.getOrElse("ephemeral", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.subActionIds = config.getOrElse("sub-action-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
    this.subActions = subActionsUtils.enable(subActionIds, config);
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      if (tempMessage == null) {
        tempMessage = message;
      }
      if (genericInteractionCreateEvent instanceof IReplyCallback iReplyCallback) {
        iReplyCallback.reply(tempMessage).setEphemeral(ephemeral).addComponents(subActionsUtils.getRows(subActions, tempData)).queue();
      } else {
        genericInteractionCreateEvent.getMessageChannel().sendMessage(tempMessage).addComponents(subActionsUtils.getRows(subActions, tempData)).queue();
      }
    }
  }

  @Override
  public void runWithPlaceholders(GenericEvent event, StringSubstitutor str, TempData tempData) {
    tempMessage = str.replace(message);
    run(event, tempData);
  }

}
