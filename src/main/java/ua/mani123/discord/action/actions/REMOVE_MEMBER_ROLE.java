package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class REMOVE_MEMBER_ROLE implements Action {

  boolean addIfNot;
  String reason;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;

  public REMOVE_MEMBER_ROLE(CommentedConfig config) {
    this.addIfNot = config.getOrElse("addIfNot", false);
    this.reason = config.getOrElse("reason", "ADD_MEMBER_ROLE");
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      for (UserSnowflake userSnowflake : tempData.getUserSnowflakes()) {
        Member member = Objects.requireNonNull(genericInteractionCreateEvent.getGuild()).getMember(userSnowflake);
        for (Role role : tempData.getRoles()) {
          if (!member.getRoles().contains(role)) {
            Objects.requireNonNull(genericInteractionCreateEvent.getGuild()).removeRoleFromMember(member, role).reason(reason).queue();
          } else if (addIfNot) {
            Objects.requireNonNull(genericInteractionCreateEvent.getGuild()).addRoleToMember(member, role).reason(reason).queue();
          }
        }
      }
    }
  }

  @Override
  public void runWithPlaceholders(GenericEvent event, StringSubstitutor str, TempData tempData) {
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }
}
