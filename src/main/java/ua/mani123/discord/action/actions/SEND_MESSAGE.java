package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
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

  public SEND_MESSAGE(CommentedConfig config) {
    this.message = config.getOrElse("message", "");
    this.ephemeral = config.getOrElse("ephemeral", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
    this.subActions = subActionsUtils.enable(config.getOrElse("sub-action", new ArrayList<>()));
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    if (tempMessage == null) {
      tempMessage = message;
    }
      if (ephemeral) {
        ReplyCallbackAction replyCallbackAction = null;
        if (event instanceof GenericCommandInteractionEvent commandInteractionEvent) {
          replyCallbackAction = commandInteractionEvent.reply(tempMessage).setEphemeral(ephemeral);
        } else if (event instanceof GenericComponentInteractionCreateEvent componentInteractionCreateEvent) {
          replyCallbackAction = componentInteractionCreateEvent.reply(tempMessage).setEphemeral(ephemeral);
        }
      if (!subActions.isEmpty()) {
        ArrayList<ItemComponent> itemComponents = new ArrayList<>();
        for (SubAction s : subActions) {
          assert replyCallbackAction != null;
          if (!s.isNextRow()) {
            itemComponents.add(s.getComponent());
          } else {
            replyCallbackAction = replyCallbackAction.addActionRow(itemComponents);
            itemComponents.clear();
          }
          replyCallbackAction = replyCallbackAction.addActionRow(itemComponents);
        }
      }
      if (replyCallbackAction != null) {
        replyCallbackAction.queue();
      } else {
        MessageCreateAction messageCreateAction = event.getMessageChannel().sendMessage(tempMessage);
        if (!subActions.isEmpty()) {
          ArrayList<ItemComponent> itemComponents = new ArrayList<>();
          for (SubAction s : subActions) {
            if (!s.isNextRow()) {
              itemComponents.add(s.getComponent());
            } else {
              messageCreateAction = messageCreateAction.addActionRow(itemComponents);
              itemComponents.clear();
              itemComponents.add(s.getComponent());
            }
          }
          messageCreateAction = messageCreateAction.addActionRow(itemComponents);
        }
        messageCreateAction.queue();
      }
    }
  }

  @Override
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData) {
    tempMessage = str.replace(message);
    run(event, tempData);
  }

}
