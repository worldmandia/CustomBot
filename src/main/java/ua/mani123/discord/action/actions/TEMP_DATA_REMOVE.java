package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class TEMP_DATA_REMOVE implements Action {

  ArrayList<String> users;
  ArrayList<String> focusedOptionIds;
  ArrayList<String> voiceChannels;
  ArrayList<String> textChannels;
  ArrayList<String> roles;

  public TEMP_DATA_REMOVE(CommentedConfig config) {
    this.users = config.getOrElse("users", new ArrayList<>());
    this.focusedOptionIds = config.getOrElse("focusedUserOptionIds", new ArrayList<>());
    this.voiceChannels = config.getOrElse("voiceChannels", new ArrayList<>());
    this.textChannels = config.getOrElse("textChannels", new ArrayList<>());
    this.roles = config.getOrElse("roles", new ArrayList<>());
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    tempData.getUserSnowflakes().removeAll(ActionUtils.getAllUsers(event, users, focusedOptionIds, voiceChannels));
    voiceChannels.forEach(s -> Objects.requireNonNull(event.getGuild()).getVoiceChannelsByName(s, false).forEach(tempData.getVoiceChannels()::remove));
    textChannels.forEach(s -> Objects.requireNonNull(event.getGuild()).getTextChannelsByName(s, false).forEach(tempData.getTextChannels()::remove));
    roles.forEach(s -> Objects.requireNonNull(event.getGuild()).getRolesByName(s, false).forEach(tempData.getRoles()::remove));
  }

  @Override
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData) {
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return Action.super.getFilters();
  }
}
