package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import ua.mani123.discord.action.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class USER implements Filter {

    List<String> userNames;

    public USER(CommentedConfig config) {
        this.userNames = config.getOrElse("list", new ArrayList<>());
    }

    @Override
    public boolean canRun(GenericInteractionCreateEvent event) {
        if (!userNames.isEmpty()) {
            List<Member> members = new ArrayList<>();
            for (String name : userNames) {
                members.add(event.getGuild().getMemberByTag(name));
            }
            return members.contains(event.getMember());
        }
        return true;
    }
}
