package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import ua.mani123.discord.action.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class BOT implements Filter {

    List<String> botNames;
    boolean isBlackList;
    private final ArrayList<String> actionNames;

    public BOT(CommentedConfig config) {
        this.botNames = config.getOrElse("list", new ArrayList<>());
        this.isBlackList = config.getOrElse("isBlackList", false);
        this.actionNames = config.getOrElse("filter-actions", new ArrayList<>());
    }

    @Override
    public boolean canRun(GenericInteractionCreateEvent event) {
        if (!botNames.isEmpty()) {
            boolean answer = botNames.contains(event.getJDA().getSelfUser().getId());
            if (isBlackList){
                return !answer;
            } else return answer;
        }
        return true;
    }

    @Override
    public boolean canRun(GenericGuildEvent event) {
        if (!botNames.isEmpty()) {
            boolean answer = botNames.contains(event.getJDA().getSelfUser().getId());
            if (isBlackList){
                return !answer;
            } else return answer;
        }
        return true;
    }

    @Override
    public boolean canRun(GenericSessionEvent event) {
        if (!botNames.isEmpty()) {
            boolean answer = botNames.contains(event.getJDA().getSelfUser().getId());
            if (isBlackList){
                return !answer;
            } else return answer;
        }
        return true;
    }

    @Override
    public ArrayList<String> getFilterActionIds() {
        return this.actionNames;
    }
}
