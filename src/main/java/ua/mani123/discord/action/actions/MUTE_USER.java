package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;

import java.util.ArrayList;
import java.util.List;

public class MUTE_USER implements Action {

    List<String> users;
    List<String> focusedOptionIds;
    boolean unmuteIfMuted;

    public MUTE_USER(CommentedConfig cfg) {
        this.users = cfg.getOrElse("users", new ArrayList<>());
        this.focusedOptionIds = cfg.getOrElse("focusedOptionIds", new ArrayList<>());
        this.unmuteIfMuted = cfg.getOrElse("unmuteIfMuted", false);
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
            if (!member.getVoiceState().isGuildMuted()){
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
