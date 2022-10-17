package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

import java.util.ArrayList;
import java.util.List;

public class UNMUTE_USER implements Action {
    List<String> users;
    List<String> focusedOptionIds;
    boolean muteIfUnmuted;

    List<Filter> filters;

    public UNMUTE_USER(CommentedConfig config) {
        this.users = config.getOrElse("users", new ArrayList<>());
        this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
        this.muteIfUnmuted = config.getOrElse("muteIfUnmuted", false);
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
        } else if (!focusedOptionIds.isEmpty()) {
            if (event instanceof SlashCommandInteractionEvent commandEvent){
                for (String id: focusedOptionIds) {
                    Member member = commandEvent.getOption(id).getAsMember();
                    if (member != null) {
                        members.add(member);
                    }
                }
            }
        } else {
            CBot.getLog().warn("Options support only in commands or you not add any data");
        }
        for (Member member : members) {
            if (member.getVoiceState().isGuildMuted()){
                member.mute(false).queue();
            } else if (muteIfUnmuted) {
                member.mute(true).queue();
            }
        }
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        run(event);
    }
}
