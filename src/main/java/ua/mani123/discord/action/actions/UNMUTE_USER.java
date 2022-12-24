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
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class UNMUTE_USER implements Action {

  ArrayList<String> users;
  ArrayList<String> focusedOptionIds;
  boolean muteIfUnmuted;
  ArrayList<String> voiceChats;
  ArrayList<Filter> filters;

  public UNMUTE_USER(CommentedConfig config) {
    this.users = config.getOrElse("users", new ArrayList<>());
    this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
    this.muteIfUnmuted = config.getOrElse("muteIfUnmuted", false);
    this.voiceChats = config.getOrElse("voiceChats", new ArrayList<>());
    this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));

  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {

    tempData.getUserSnowflakes().addAll(ActionUtils.getMembersFromList(event, users));
    tempData.getUserSnowflakes().addAll(ActionUtils.getMembersFromFocusedOptions(event, focusedOptionIds));
    tempData.getUserSnowflakes().addAll(ActionUtils.getMembersFromVoiceChat(event, voiceChats));

    try {
      for (UserSnowflake userSnowflake : tempData.getUserSnowflakes()) {
        if (userSnowflake instanceof Member member) {
          if (Objects.requireNonNull(member.getVoiceState()).isGuildMuted()) {
            member.mute(false).queue();
          } else if (muteIfUnmuted) {
            member.mute(true).queue();
          }
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
