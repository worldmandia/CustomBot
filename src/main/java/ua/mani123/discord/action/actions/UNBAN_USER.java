package ua.mani123.discord.action.actions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.action.filter.Filter;
import ua.mani123.discord.action.filter.filterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UNBAN_USER implements Action {

    List<String> users;
    List<String> focusedOptionIds;
    boolean banIfUnbaned;
    List<String> voiceChats;

    List<Filter> filters;
    int deleteBannedUserMessagesDuringTime;
    String deleteBannedUserMessagesTimeType;
    String reason;

    public UNBAN_USER(CommentedConfig config) {
        this.users = config.getOrElse("users", new ArrayList<>());
        this.focusedOptionIds = config.getOrElse("focusedOptionIds", new ArrayList<>());
        this.banIfUnbaned = config.getOrElse("banIfUnbaned", false);
        this.voiceChats = config.getOrElse("voiceChats", new ArrayList<>());
        this.filters = filterUtils.enable(config.getOrElse("filter", new ArrayList<>()));
        this.deleteBannedUserMessagesDuringTime = config.getIntOrElse("deleteBannedUserMessagesDuringTime", 0);
        this.deleteBannedUserMessagesTimeType = config.getOrElse("deleteBannedUserMessagesTimeType", "MINUTES");
        this.reason = config.getOrElse("reason", null);
    }

    private List<UserSnowflake> getAllUsers(GenericInteractionCreateEvent event) {
        List<UserSnowflake> members = new ArrayList<>();

        members.addAll(actionUtils.getMembersFromList(event, users));
        members.addAll(actionUtils.getMembersFromFocusedOptions(event, focusedOptionIds));
        members.addAll(actionUtils.getMembersFromVoiceChat(event, voiceChats));
        members.addAll(actionUtils.getUserSnowflakeFromList(event, users));

        return members;
    }

    @Override
    public void run(GenericInteractionCreateEvent event) {
        for (UserSnowflake member : getAllUsers(event)) {
            event.getGuild().retrieveBan(member).queue(
                    (success) -> event.getGuild().unban(success.getUser()).queue(), (error) -> {
                        if (banIfUnbaned) {
                            AuditableRestAction<Void> auditableRestAction = event.getGuild().ban(member, deleteBannedUserMessagesDuringTime, TimeUnit.valueOf(deleteBannedUserMessagesTimeType));
                            if (reason != null) {
                                auditableRestAction.reason(reason).queue();
                            } else {
                                auditableRestAction.queue();
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str) {
        for (UserSnowflake member : getAllUsers(event)) {
            event.getGuild().retrieveBan(member).queue(
                    (success) -> event.getGuild().unban(success.getUser()).queue(), (error) -> {
                        if (banIfUnbaned) {
                            AuditableRestAction<Void> auditableRestAction = event.getGuild().ban(member, deleteBannedUserMessagesDuringTime, TimeUnit.valueOf(deleteBannedUserMessagesTimeType));
                            if (reason != null) {
                                auditableRestAction.reason(str.replace(reason)).queue();
                            } else {
                                auditableRestAction.queue();
                            }
                        }
                    }
            );
        }
    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
