package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import ua.mani123.discord.action.filter.Filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ROLE implements Filter {

    List<String> rolesNames;

    public ROLE(CommentedConfig config) {
        this.rolesNames = config.getOrElse("list", new ArrayList<>());
    }

    @Override
    public boolean canRun(GenericInteractionCreateEvent event) {
        if (!rolesNames.isEmpty()) {
            List<Role> roles = new ArrayList<>();
            for (String name : rolesNames) {
                roles.add(event.getGuild().getRolesByName(name, false).get(0));
            }
            return new HashSet<>(event.getMember().getRoles()).containsAll(roles);
        }
        return true;
    }
}
