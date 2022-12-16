package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import ua.mani123.discord.action.filter.Filter;

public class GUILD implements Filter {

  private final ArrayList<String> actionNames;
  List<String> guildNames;
  boolean isBlackList;


  public GUILD(CommentedConfig config) {
    this.guildNames = config.getOrElse("list", new ArrayList<>());
    this.isBlackList = config.getOrElse("isBlackList", false);
    this.actionNames = config.getOrElse("filter-actions", new ArrayList<>());
  }

  @Override
  public boolean canRun(GenericInteractionCreateEvent event) {
    if (!guildNames.isEmpty()) {
      boolean answer = guildNames.contains(event.getGuild().getName());
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
    if (!guildNames.isEmpty()) {
      boolean answer = guildNames.contains(event.getGuild().getName());
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
