package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

import java.util.ArrayList;

public class UNDEAFEN_USER implements Action {

    ArrayList<String> users;
    ArrayList<String> focusedOptionIds;
    boolean muteIfUnmuted;
    ArrayList<String> voiceChats;
    ArrayList<Filter> filters;

    public UNDEAFEN_USER(CommentedConfig config) {
        this.users = config.getOrElse("users", new ArrayList<>());
        this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
        this.muteIfUnmuted = config.getOrElse("muteIfUnmuted", false);
        this.voiceChats = config.getOrElse("voiceChats", new ArrayList<>());
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));

    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        ArrayList<Member> members = new ArrayList<>();

        members.addAll(actionUtils.getMembersFromList(event, users));
        members.addAll(actionUtils.getMembersFromFocusedOptions(event, focusedOptionIds));
        members.addAll(actionUtils.getMembersFromVoiceChat(event, voiceChats));


        try {
            for (Member member : members) {
                if (member.getVoiceState().isDeafened()) {
                    member.deafen(false).queue();
                } else if (muteIfUnmuted) {
                    member.deafen(true).queue();
                }
            }
        } catch (IllegalStateException e) {
            CBot.getLog().warn("The bot cannot mute or unmute a member if they are not in a voice channel, you can ignore it");
        }
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        run(event);
    }

    @Override
    public ArrayList<Filter> getFilters() {
        return filters;
    }
}
