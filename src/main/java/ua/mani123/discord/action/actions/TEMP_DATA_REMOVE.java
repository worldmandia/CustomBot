package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Mentions;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class TEMP_DATA_REMOVE implements Action {

  ArrayList<String> focusedUserOptionIds;
  ArrayList<String> focusedStringOptionIds;
  ArrayList<String> voiceChannels;
  ArrayList<String> textChannels;
  ArrayList<String> roles;
  ArrayList<String> members;
  boolean allowAddInteractionUser;

  public TEMP_DATA_REMOVE(CommentedConfig config) {
    this.focusedUserOptionIds = config.getOrElse("focusedUserOptionIds", new ArrayList<>());
    this.focusedStringOptionIds = config.getOrElse("focusedStringOptionIds", new ArrayList<>());
    this.voiceChannels = config.getOrElse("voiceChannels", new ArrayList<>());
    this.textChannels = config.getOrElse("textChannels", new ArrayList<>());
    this.roles = config.getOrElse("roles", new ArrayList<>());
    this.members = config.getOrElse("members", new ArrayList<>());
    this.allowAddInteractionUser = config.getOrElse("allowAddInteractionUser", false);
  }

  @Override
  public void run(GenericEvent event, TempData tempData) {
    if (event instanceof GenericInteractionCreateEvent genericInteractionCreateEvent) {
      tempData.getUserSnowflakes().removeAll(ActionUtils.getAllUsers(genericInteractionCreateEvent, focusedUserOptionIds, voiceChannels, members));
      voiceChannels.forEach(s -> ActionUtils.getVoiceChannelsByNameOrId(genericInteractionCreateEvent, s, false).forEach(tempData.getVoiceChannels()::remove));
      textChannels.forEach(s -> ActionUtils.getTextChannelsByNameOrId(genericInteractionCreateEvent, s, false).forEach(tempData.getTextChannels()::remove));
      roles.forEach(s -> ActionUtils.getRolesByNameOrId(genericInteractionCreateEvent, s, false).forEach(tempData.getRoles()::remove));
      if (allowAddInteractionUser) {
        tempData.getUserSnowflakes().remove(genericInteractionCreateEvent.getInteraction().getUser());
      }
    }
    //if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEventd) {
    //  for (String optionId: focusedStringOptionIds) {
    //    tempData.getContentData().put(optionId, Objects.requireNonNull(slashCommandInteractionEvent.getOption(optionId)).getAsString());
    //  }
    //}
    if (event instanceof StringSelectInteractionEvent stringSelectInteractionEvent) {
      List<String> strings = stringSelectInteractionEvent.getValues();
      for (int i = 0; i < strings.size(); i++) {
        tempData.getContentData().put(stringSelectInteractionEvent.getComponentId() + "-" + i, strings.get(i));
      }
    }
    if (event instanceof EntitySelectInteractionEvent entitySelectInteractionEvent) {
      Mentions mentions = entitySelectInteractionEvent.getMentions();
      mentions.getRoles().forEach(tempData.getRoles()::remove);
      mentions.getMembers().forEach(tempData.getUserSnowflakes()::remove);
      mentions.getChannels().forEach(guildChannel -> {
        if (guildChannel instanceof TextChannel textChannel) tempData.getTextChannels().remove(textChannel);
        else if (guildChannel instanceof VoiceChannel voiceChannel) tempData.getVoiceChannels().remove(voiceChannel);
      });
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
