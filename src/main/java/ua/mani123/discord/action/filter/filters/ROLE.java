package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import ua.mani123.discord.action.filter.Filter;

public class ROLE implements Filter {

  private final ArrayList<String> actionNames;
  List<String> rolesNames;
  boolean isBlackList;


  public ROLE(CommentedConfig config) {
    this.rolesNames = config.getOrElse("list", new ArrayList<>());
    this.isBlackList = config.getOrElse("isBlackList", false);
    this.actionNames = config.getOrElse("filter-actions", new ArrayList<>());
  }

  @Override
  public boolean canRun(GenericInteractionCreateEvent event) {
    if (!rolesNames.isEmpty()) {
      List<Role> roles = new ArrayList<>();
      for (String name : rolesNames) {
        roles.add(event.getGuild().getRolesByName(name, false).get(0));
      }
      boolean answer = new HashSet<>(event.getMember().getRoles()).containsAll(roles);
      if (isBlackList) {
        return !answer;
      } else {
        return answer;
      }
    }
    return true;
  }

  @Override
  public boolean canRun(GenericGuildEvent event) {
    if (!rolesNames.isEmpty()) {
      List<Role> roles = new ArrayList<>();
      for (String name : rolesNames) {
        roles.add(event.getGuild().getRolesByName(name, false).get(0));
      }
      boolean answer = new HashSet<>(event.getGuild().getSelfMember().getRoles()).containsAll(roles);
      if (isBlackList) {
        return !answer;
      } else {
        return answer;
      }
    }
    return true;
  }

  @Override
  public boolean canRun(GenericSessionEvent genericSessionEvent) {
    return true;
  }

  @Override
  public ArrayList<String> getFilterActionIds() {
    return this.actionNames;
  }
}
