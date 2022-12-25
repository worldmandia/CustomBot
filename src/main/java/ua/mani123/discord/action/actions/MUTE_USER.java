package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class MUTE_USER implements Action {
  boolean unmuteIfMuted;
  ArrayList<Filter> filters;
  ArrayList<String> filterIds;

  public MUTE_USER(CommentedConfig config) {
    this.unmuteIfMuted = config.getOrElse("unmuteIfMuted", false);
    this.filterIds = config.getOrElse("filter-ids", new ArrayList<>());
    this.filters = filterUtils.enable(filterIds, config);
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    try {
      for (UserSnowflake userSnowflake : tempData.getUserSnowflakes()) {
        Member member = Objects.requireNonNull(event.getGuild()).getMember(userSnowflake);
          if (!Objects.requireNonNull(member.getVoiceState()).isGuildMuted()) {
            member.mute(true).queue();
          } else if (unmuteIfMuted) {
            member.mute(false).queue();
          }
      }
    } catch (Exception e) {
      CBot.getLog().warn("The bot cannot mute or unmute a member if they are not in a voice channel, you can ignore it");
    }
  }

  @Override
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData) {
    run(event, tempData);
  }
}
