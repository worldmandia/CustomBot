package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class BAN_USER implements Action {
  boolean unbanIfBanned;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;
  int deleteBannedUserMessagesDuringTime;
  String deleteBannedUserMessagesTimeType;
  String reason;

  public BAN_USER(CommentedConfig config) {
    this.unbanIfBanned = config.getOrElse("unbanIfBanned", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
    this.deleteBannedUserMessagesDuringTime = config.getIntOrElse("deleteBannedUserMessagesDuringTime", 0);
    this.deleteBannedUserMessagesTimeType = config.getOrElse("deleteBannedUserMessagesTimeType", "MINUTES");
    this.reason = config.getOrElse("reason", null);
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      tempData.getUserSnowflakes().forEach(userSnowflake -> Objects.requireNonNull(genericInteractionCreateEvent.getGuild()).retrieveBan(userSnowflake).queue(
          (success) -> {
            if (unbanIfBanned) {
              genericInteractionCreateEvent.getGuild().unban(success.getUser()).queue();
            }
          }, (error) -> {
            AuditableRestAction<Void> auditableRestAction = genericInteractionCreateEvent.getGuild()
                .ban(userSnowflake, deleteBannedUserMessagesDuringTime, TimeUnit.valueOf(deleteBannedUserMessagesTimeType));
            if (reason != null) {
              auditableRestAction.reason(reason).queue();
            } else {
              auditableRestAction.queue();
            }
          }
      ));
    }
  }

  @Override
  public void runWithPlaceholders(GenericEvent event, StringSubstitutor str, TempData tempData) {
    reason = str.replace(reason);
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }
}
