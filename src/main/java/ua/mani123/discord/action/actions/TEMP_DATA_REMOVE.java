package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class TEMP_DATA_REMOVE implements Action {

  HashSet<String> users;
  HashSet<String> focusedUserOptionIds;
  HashSet<String> focusedStringOptionIds;
  HashSet<String> voiceChannels;
  HashSet<String> textChannels;
  HashSet<String> roles;
  HashSet<String> members;
  boolean allowAddInteractionUser;

  public TEMP_DATA_REMOVE(CommentedConfig config) {
    this.users = config.getOrElse("users", new HashSet<>());
    this.focusedUserOptionIds = config.getOrElse("focusedUserOptionIds", new HashSet<>());
    this.focusedStringOptionIds = config.getOrElse("focusedStringOptionIds", new HashSet<>());
    this.voiceChannels = config.getOrElse("voiceChannels", new HashSet<>());
    this.textChannels = config.getOrElse("textChannels", new HashSet<>());
    this.roles = config.getOrElse("roles", new HashSet<>());
    this.members = config.getOrElse("members", new HashSet<>());
    this.allowAddInteractionUser = config.getOrElse("allowAddInteractionUser", false);
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    tempData.getUserSnowflakes().removeAll(ActionUtils.getAllUsers(event, users, focusedUserOptionIds, voiceChannels, members));
    voiceChannels.forEach(s -> Objects.requireNonNull(event.getGuild()).getVoiceChannelsByName(s, false).forEach(tempData.getVoiceChannels()::remove));
    textChannels.forEach(s -> Objects.requireNonNull(event.getGuild()).getTextChannelsByName(s, false).forEach(tempData.getTextChannels()::remove));
    roles.forEach(s -> Objects.requireNonNull(event.getGuild()).getRolesByName(s, false).forEach(tempData.getRoles()::remove));
    if (allowAddInteractionUser) {
      tempData.getUserSnowflakes().remove(event.getInteraction().getUser());
    }
    if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
      for (String optionId: focusedStringOptionIds) {
        tempData.getContentData().put(optionId, Objects.requireNonNull(slashCommandInteractionEvent.getOption(optionId)).getAsString());
      }
    }
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
