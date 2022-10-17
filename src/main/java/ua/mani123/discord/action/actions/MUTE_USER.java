package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

import java.util.ArrayList;
import java.util.List;

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

        if (!users.isEmpty()) {
            for (String name : users) {
                Member member = event.getGuild().getMemberByTag(name);
                if (member != null) {
                    members.add(member);
                }
            }
        }

        if (!focusedOptionIds.isEmpty()) {
            if (event instanceof SlashCommandInteractionEvent commandEvent) {
                for (String id : focusedOptionIds) {
                    Member member = commandEvent.getOption(id).getAsMember();
                    if (member != null) {
                        members.add(member);
                    }
                }
            }
        }

        if (!voiceChats.isEmpty()) {
            for (String chatName : voiceChats) {
                members.addAll(event.getGuild().getVoiceChannelsByName(chatName, false).get(0).getMembers());
            }
        }

        for (Member member : members) {
            if (!member.getVoiceState().isGuildMuted()) {
                member.mute(true).queue();
            } else if (unmuteIfMuted) {
                member.mute(false).queue();
            }
        }
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        run(event);
    }
}
