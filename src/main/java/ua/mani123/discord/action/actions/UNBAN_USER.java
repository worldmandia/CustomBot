package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class UNBAN_USER implements Action {
  boolean banIfUnbanned;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;
  int deleteBannedUserMessagesDuringTime;
  String deleteBannedUserMessagesTimeType;
  String reason;

  public UNBAN_USER(CommentedConfig config) {
    this.banIfUnbanned = config.getOrElse("banIfUnbanned", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
    this.deleteBannedUserMessagesDuringTime = config.getIntOrElse("deleteBannedUserMessagesDuringTime", 0);
    this.deleteBannedUserMessagesTimeType = config.getOrElse("deleteBannedUserMessagesTimeType", "MINUTES");
    this.reason = config.getOrElse("reason", null);
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    for (UserSnowflake member : tempData.getUserSnowflakes()) {
      Objects.requireNonNull(event.getGuild()).retrieveBan(member).queue(
          (success) -> event.getGuild().unban(success.getUser()).queue(), (error) -> {
            if (banIfUnbanned) {
              AuditableRestAction<Void> auditableRestAction = event.getGuild()
                  .ban(member, deleteBannedUserMessagesDuringTime, TimeUnit.valueOf(deleteBannedUserMessagesTimeType));
              if (reason != null) {
                auditableRestAction.reason(reason).queue();
              } else {
                auditableRestAction.queue();
              }
            }
          }
      );
    }
  }

  @Override
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData) {
    reason = str.replace(reason);
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }
}
