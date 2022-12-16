package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class UNBAN_USER implements Action {

  ArrayList<String> users;
  ArrayList<String> focusedOptionIds;
  boolean banIfUnbaned;
  ArrayList<String> voiceChats;

  ArrayList<Filter> filters;
  int deleteBannedUserMessagesDuringTime;
  String deleteBannedUserMessagesTimeType;
  String reason;

  public UNBAN_USER(CommentedConfig config) {
    this.users = config.getOrElse("users", new ArrayList<>());
    this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
    this.banIfUnbaned = config.getOrElse("banIfUnbaned", false);
    this.voiceChats = config.getOrElse("voiceChats", new ArrayList<>());
    this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
    this.deleteBannedUserMessagesDuringTime = config.getIntOrElse("deleteBannedUserMessagesDuringTime", 0);
    this.deleteBannedUserMessagesTimeType = config.getOrElse("deleteBannedUserMessagesTimeType", "MINUTES");
    this.reason = config.getOrElse("reason", null);
  }

  private List<UserSnowflake> getAllUsers(GenericInteractionCreateEvent event) {
    ArrayList<UserSnowflake> members = new ArrayList<>();

    members.addAll(ActionUtils.getMembersFromList(event, users));
    members.addAll(ActionUtils.getMembersFromFocusedOptions(event, focusedOptionIds));
    members.addAll(ActionUtils.getMembersFromVoiceChat(event, voiceChats));
    members.addAll(ActionUtils.getUserSnowflakeFromList(event, users));

    return members;
  }

  @Override
  public void run(GenericInteractionCreateEvent event) {
    for (UserSnowflake member : getAllUsers(event)) {
      Objects.requireNonNull(event.getGuild()).retrieveBan(member).queue(
          (success) -> event.getGuild().unban(success.getUser()).queue(), (error) -> {
            if (banIfUnbaned) {
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
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
    for (UserSnowflake member : getAllUsers(event)) {
      Objects.requireNonNull(event.getGuild()).retrieveBan(member).queue(
          (success) -> event.getGuild().unban(success.getUser()).queue(), (error) -> {
            if (banIfUnbaned) {
              AuditableRestAction<Void> auditableRestAction = event.getGuild()
                  .ban(member, deleteBannedUserMessagesDuringTime, TimeUnit.valueOf(deleteBannedUserMessagesTimeType));
              if (reason != null) {
                auditableRestAction.reason(str.replace(reason)).queue();
              } else {
                auditableRestAction.queue();
              }
            }
          }
      );
    }
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }
}
