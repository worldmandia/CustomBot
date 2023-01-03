package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class UNDEAFEN_USER implements Action {
  boolean muteIfUnmuted;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;

  public UNDEAFEN_USER(CommentedConfig config) {
    this.muteIfUnmuted = config.getOrElse("muteIfUnmuted", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    tempData.getUserSnowflakes().forEach(userSnowflake -> {
      if (userSnowflake instanceof Member member) {
        if (Objects.requireNonNull(member.getVoiceState()).isDeafened()) {
          member.deafen(false).queue();
        } else if (muteIfUnmuted) {
          member.deafen(true).queue();
        }
      } else {
        CBot.getLog().info(userSnowflake.getId() + " not in guild");
      }
    });
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
