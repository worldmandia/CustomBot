package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Mentions;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
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

  public static void addDefaultPlaceholders(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      tempData.getPlaceholders().put("interaction-user", genericInteractionCreateEvent.getUser().getName());
      tempData.getPlaceholders().put("interaction-user-mentioned", genericInteractionCreateEvent.getUser().getAsMention());
      tempData.getPlaceholders().put("interaction-user-as-tag", genericInteractionCreateEvent.getUser().getAsTag());
      tempData.getPlaceholders().put("guild-name", Objects.requireNonNull(genericInteractionCreateEvent.getGuild()).getName());
      tempData.getPlaceholders().put("guild-owner-mentioned", Objects.requireNonNull(genericInteractionCreateEvent.getGuild().getOwner()).getAsMention());
      tempData.getPlaceholders().put("guild-owner-nickname", genericInteractionCreateEvent.getGuild().getOwner().getNickname());
      tempData.getPlaceholders().put("channel-mentioned", Objects.requireNonNull(genericInteractionCreateEvent.getChannel()).getAsMention());
      tempData.getPlaceholders().put("channel-type", genericInteractionCreateEvent.getChannelType().toString());
    }
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      tempData.getUserSnowflakes().addAll(ActionUtils.getAllUsers(genericInteractionCreateEvent, focusedUserOptionIds, voiceChannels, members));
      voiceChannels.forEach(s -> tempData.getVoiceChannels().addAll(ActionUtils.getVoiceChannelsByNameOrId(genericInteractionCreateEvent, s, false)));
      textChannels.forEach(s -> tempData.getTextChannels().addAll(ActionUtils.getTextChannelsByNameOrId(genericInteractionCreateEvent, s, false)));
      roles.forEach(s -> tempData.getRoles().addAll(ActionUtils.getRolesByNameOrId(genericInteractionCreateEvent, s, false)));
      if (allowAddInteractionUser) {
        tempData.getUserSnowflakes().add(genericInteractionCreateEvent.getInteraction().getUser());
      }
    }
    if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
      for (String optionId : focusedStringOptionIds) {
        tempData.getContentData().put(optionId, Objects.requireNonNull(slashCommandInteractionEvent.getOption(optionId)).getAsString());
      }
    }
    if (event instanceof StringSelectInteractionEvent stringSelectInteractionEvent) {
      List<String> strings = stringSelectInteractionEvent.getValues();
      for (int i = 0; i < strings.size(); i++) {
        tempData.getContentData().put(stringSelectInteractionEvent.getComponentId() + "-" + i, strings.get(i));
      }
    }
    if (event instanceof EntitySelectInteractionEvent entitySelectInteractionEvent) {
      Mentions mentions = entitySelectInteractionEvent.getMentions();
      tempData.getRoles().addAll(mentions.getRoles());
      tempData.getUserSnowflakes().addAll(mentions.getMembers());
      mentions.getChannels().forEach(guildChannel -> {
        if (guildChannel instanceof TextChannel textChannel) tempData.getTextChannels().add(textChannel);
        else if (guildChannel instanceof VoiceChannel voiceChannel) tempData.getVoiceChannels().add(voiceChannel);
      });
    }



    // Placeholders feature

    if (updateDefaultPlaceholders) {
      addDefaultPlaceholders(event, tempData);
    }
    if (contentDataAddToPlaceholders && !tempData.getContentData().isEmpty()) {
      tempData.getPlaceholders().putAll(tempData.getContentData());
    }
  }

  @Override
  public void runWithPlaceholders(GenericEvent event, StringSubstitutor str, TempData tempData) {
    run(event, tempData);
  }

  @Override
  public ArrayList<Filter> getFilters() {
    return Action.super.getFilters();
  }
}
