package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class USER implements Filter {

  private final ArrayList<String> actionNames;
  List<String> userNames;
  boolean isBlackList;


  public USER(CommentedConfig config) {
    this.userNames = config.getOrElse("list", new ArrayList<>());
    this.isBlackList = config.getOrElse("isBlackList", false);
    this.actionNames = config.getOrElse("filter-actions", new ArrayList<>());
  }

  @Override
  public boolean canRun(GenericInteractionCreateEvent event, TempData tempData) {
    if (!userNames.isEmpty()) {
      List<Member> members = new ArrayList<>();
      for (String name : userNames) {
        members.add(Objects.requireNonNull(event.getGuild()).getMemberByTag(name));
      }
      boolean answer = members.contains(event.getMember());
      if (isBlackList) {
        return !answer;
      } else {
        return answer;
      }
    }
    return true;
  }

  @Override
  public boolean canRun(GenericGuildEvent event, TempData tempData) {
    if (!userNames.isEmpty()) {
      ArrayList<Member> members = new ArrayList<>();
      for (String name : userNames) {
        members.add(event.getGuild().getMemberByTag(name));
      }
      boolean answer = members.containsAll(event.getGuild().getMembers());
      if (isBlackList) {
        return !answer;
      } else {
        return answer;
      }
    }
    return true;
  }

  @Override
  public boolean canRun(GenericSessionEvent genericSessionEvent, TempData tempData) {
    return true;
  }

  @Override
  public ArrayList<String> getFilterActionIds() {
    return this.actionNames;
  }
}
