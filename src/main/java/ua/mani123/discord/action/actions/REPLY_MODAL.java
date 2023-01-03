package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IModalCallback;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.action.subActions.SubAction;
import ua.mani123.discord.action.subActions.subActionsUtils;

public class REPLY_MODAL implements Action {

  String modalId;
  String title;
  String tempTitle;
  ArrayList<SubAction> subActions;
  ArrayList<String> subActionIds;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;

  public REPLY_MODAL(CommentedConfig config) {
    this.modalId = config.get("modalId");
    this.title = config.get("title");
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
    this.subActionIds = config.getOrElse("sub-action-ids", new ArrayList<>());
    this.subActions = subActionsUtils.enable(subActionIds, config);
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      if (tempTitle == null) {
        tempTitle = title;
      }
      Modal.Builder modalBuilder = Modal.create(modalId, tempTitle);
      subActions.forEach(subAction -> modalBuilder.addActionRow(subAction.getComponent(tempData)));
      if (event instanceof IModalCallback iModalCallback) {
        iModalCallback.replyModal(modalBuilder.build()).queue();
      } else {
        CBot.getLog().warn(genericInteractionCreateEvent.getType().name() + " - not support modals");
      }
    }
  }

  @Override
  public void runWithPlaceholders(GenericEvent event, StringSubstitutor str, TempData tempData) {
    tempTitle = str.replace(title);
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }
}
