package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

public class MUTE_USER implements Action {

  List<String> users;
  List<String> focusedOptionIds;
  boolean unmuteIfMuted;
  List<String> voiceChats;

  List<Filter> filters;

  public MUTE_USER(CommentedConfig config) {
    this.users = config.getOrElse("users", new ArrayList<>());
    this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
    this.unmuteIfMuted = config.getOrElse("unmuteIfMuted", false);
    this.voiceChats = config.getOrElse("voiceChats", new ArrayList<>());
    this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
  }

  @Override
  public void run(GenericInteractionCreateEvent event) {
    List<Member> members = new ArrayList<>();

    members.addAll(ActionUtils.getMembersFromList(event, users));
    members.addAll(ActionUtils.getMembersFromFocusedOptions(event, focusedOptionIds));
    members.addAll(ActionUtils.getMembersFromVoiceChat(event, voiceChats));

    try {
      for (Member member : members) {
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
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
    run(event);
  }
}
