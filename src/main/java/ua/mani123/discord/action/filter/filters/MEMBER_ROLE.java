package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import ua.mani123.CBot;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class MEMBER_ROLE implements Filter {

  private final ArrayList<String> actionNames;
  boolean isBlackList;
  private final ArrayList<String> beforeActionNames;


  public MEMBER_ROLE(CommentedConfig config) {
    this.isBlackList = config.getOrElse("isBlackList", false);
    this.actionNames = config.getOrElse("filter-actions", new ArrayList<>());
    this.beforeActionNames = config.getOrElse("before-filter-actions", new ArrayList<>());
  }

  @Override
  public boolean canRun(GenericInteractionCreateEvent event, TempData tempData) {
    beforeActionNames.forEach(s -> ActionUtils.getActionMap().get(s).forEach(action -> action.run(event, tempData)));
    AtomicBoolean answer = new AtomicBoolean(true);
    tempData.getUserSnowflakes().forEach(userSnowflake -> {
      if (userSnowflake instanceof Member member) {
        if (answer.get()) {
          answer.set(new HashSet<>(member.getRoles()).containsAll(tempData.getRoles()));
        }
      }
    });
    if (isBlackList) {
      return !answer.get();
    } else {
      return answer.get();
    }
  }

  @Override
  public boolean canRun(GenericGuildEvent event, TempData tempData) {
    beforeActionNames.forEach(s -> ActionUtils.getActionMap().get(s).forEach(action -> action.run(event, tempData)));
    AtomicBoolean answer = new AtomicBoolean(true);
    tempData.getUserSnowflakes().forEach(userSnowflake -> {
      if (userSnowflake instanceof Member member) {
        if (answer.get()) {
          answer.set(new HashSet<>(member.getRoles()).containsAll(tempData.getRoles()));
        }
      }
    });
    if (isBlackList) {
      return !answer.get();
    } else {
      return answer.get();
    }
  }

  @Override
  public boolean canRun(GenericSessionEvent genericSessionEvent, TempData tempData) {
    CBot.getLog().warn("GenericSessionEvent dont support MEMBER_ROLE filter");
    return false;
  }

  @Override
  public ArrayList<String> getFilterActionIds() {
    return this.actionNames;
  }
}
