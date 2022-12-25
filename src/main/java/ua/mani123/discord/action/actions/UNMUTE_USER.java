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

public class UNMUTE_USER implements Action {

  boolean muteIfUnmuted;
  ArrayList<Filter> filters;

  public UNMUTE_USER(CommentedConfig config) {
    this.muteIfUnmuted = config.getOrElse("muteIfUnmuted", false);
    this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));

  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    try {
      for (UserSnowflake userSnowflake : tempData.getUserSnowflakes()) {
        Member member = Objects.requireNonNull(event.getGuild()).getMember(userSnowflake);
        if (Objects.requireNonNull(member.getVoiceState()).isGuildMuted()) {
            member.mute(false).queue();
          } else if (muteIfUnmuted) {
            member.mute(true).queue();
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
