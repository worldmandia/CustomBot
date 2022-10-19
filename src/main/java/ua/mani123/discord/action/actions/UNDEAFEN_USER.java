package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

import java.util.ArrayList;
import java.util.List;

public class UNDEAFEN_USER implements Action {

    List<String> users;
    List<String> focusedOptionIds;
    boolean muteIfUnmuted;
    List<String> voiceChats;
    List<Filter> filters;

    public UNDEAFEN_USER(CommentedConfig config) {
        this.users = config.getOrElse("users", new ArrayList<>());
        this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
        this.muteIfUnmuted = config.getOrElse("muteIfUnmuted", false);
        this.voiceChats = config.getOrElse("voiceChats", new ArrayList<>());
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));

    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        List<Member> members = new ArrayList<>();

        members.addAll(actionUtils.getMembersFromList(event, users));
        members.addAll(actionUtils.getMembersFromFocusedOptions(event, focusedOptionIds));
        members.addAll(actionUtils.getMembersFromVoiceChat(event, voiceChats));


        for (Member member : members) {
            if (member.getVoiceState().isDeafened()) {
                member.deafen(false).queue();
            } else if (muteIfUnmuted) {
                member.deafen(true).queue();
            }
        }
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        run(event);
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
