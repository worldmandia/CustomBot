package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.Objects;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class TEMP_DATA_ADD implements Action {

  ArrayList<String> focusedUserOptionIds;
  ArrayList<String> focusedStringOptionIds;
  ArrayList<String> voiceChannels;
  ArrayList<String> textChannels;
  ArrayList<String> roles;
  ArrayList<String> members;
  boolean allowAddInteractionUser;
  boolean contentDataAddToPlaceholders;
  boolean updateDefaultPlaceholders;

  public TEMP_DATA_ADD(CommentedConfig config) {
    this.focusedUserOptionIds = config.getOrElse("focusedUserOptionIds", new ArrayList<>());
    this.focusedStringOptionIds = config.getOrElse("focusedStringOptionIds", new ArrayList<>());
    this.voiceChannels = config.getOrElse("voiceChannels", new ArrayList<>());
    this.textChannels = config.getOrElse("textChannels", new ArrayList<>());
    this.roles = config.getOrElse("roles", new ArrayList<>());
    this.members = config.getOrElse("members", new ArrayList<>());
    this.allowAddInteractionUser = config.getOrElse("allowAddInteractionUser", false);
    this.contentDataAddToPlaceholders = config.getOrElse("contentDataAddToPlaceholders", false);
    this.updateDefaultPlaceholders = config.getOrElse("updateDefaultPlaceholders", false);
  }

  public static void addDefaultPlaceholders(GenericInteractionCreateEvent event, TempData tempData) {
    tempData.getPlaceholders().put("interaction-user", event.getUser().getName());
    tempData.getPlaceholders().put("interaction-user-mentioned", event.getUser().getAsMention());
    tempData.getPlaceholders().put("interaction-user-as-tag", event.getUser().getAsTag());
    tempData.getPlaceholders().put("guild-name", Objects.requireNonNull(event.getGuild()).getName());
    tempData.getPlaceholders().put("guild-owner-mentioned", Objects.requireNonNull(event.getGuild().getOwner()).getAsMention());
    tempData.getPlaceholders().put("guild-owner-nickname", event.getGuild().getOwner().getNickname());
    tempData.getPlaceholders().put("channel-mentioned", Objects.requireNonNull(event.getChannel()).getAsMention());
    tempData.getPlaceholders().put("channel-type", event.getInteraction().getChannelType().toString());
  }

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    tempData.getUserSnowflakes().addAll(ActionUtils.getAllUsers(event, focusedUserOptionIds, voiceChannels, members));
    voiceChannels.forEach(s -> tempData.getVoiceChannels().addAll(ActionUtils.getVoiceChannelsByNameOrId(event, s, false)));
    textChannels.forEach(s -> tempData.getTextChannels().addAll(ActionUtils.getTextChannelsByNameOrId(event, s, false)));
    roles.forEach(s -> tempData.getRoles().addAll(ActionUtils.getRolesByNameOrId(event, s, false)));
    if (allowAddInteractionUser) {
      tempData.getUserSnowflakes().add(event.getInteraction().getUser());
    }
    if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
      for (String optionId : focusedStringOptionIds) {
        tempData.getContentData().put(optionId, Objects.requireNonNull(slashCommandInteractionEvent.getOption(optionId)).getAsString());
      }
    }
    if (updateDefaultPlaceholders) {
      addDefaultPlaceholders(event, tempData);
    }
    if (contentDataAddToPlaceholders && !tempData.getContentData().isEmpty()) {
      tempData.getPlaceholders().putAll(tempData.getContentData());
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
