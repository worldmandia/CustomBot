package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class ADD_MEMBER_ROLE implements Action {

  boolean removeIfHave;
  String reason;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;

  public ADD_MEMBER_ROLE(CommentedConfig config) {
    this.removeIfHave = config.getOrElse("removeIfHave", false);
    this.reason = config.getOrElse("reason", "ADD_MEMBER_ROLE");
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    for (UserSnowflake userSnowflake: tempData.getUserSnowflakes()) {
      Member member = Objects.requireNonNull(event.getGuild()).getMember(userSnowflake);
        for (Role role: tempData.getRoles()) {
          if (!member.getRoles().contains(role)) {
            Objects.requireNonNull(event.getGuild()).addRoleToMember(member, role).reason(reason).queue();
          } else if (removeIfHave) {
            Objects.requireNonNull(event.getGuild()).removeRoleFromMember(member, role).reason(reason).queue();
          }
        }
    }
  }

  @Override
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData) {
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return filters;
  }
}
