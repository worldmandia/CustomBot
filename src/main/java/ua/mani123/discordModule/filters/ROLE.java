package ua.mani123.discordModule.filters;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.TempData;
import ua.mani123.discordModule.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class ROLE extends DiscordConfigs.Filter {

    private ArrayList<String> roles;
    private boolean whitelist;
    private boolean containsALL;

    public ROLE(String type, String id, ArrayList<String> roles, boolean whitelist, boolean containsALL, ArrayList<DiscordConfigs.Order> actions) {
        super(type, id);
        this.roles = roles;
        this.whitelist = whitelist;
        this.containsALL = containsALL;
        this.getDenyOrders().addAll(actions);
    }

    @Override
    public boolean canNext(GenericEvent event, TempData tempData) {
        if (event instanceof Interaction interaction) {
            Member member = interaction.getMember();
            if (member != null) {
                ArrayList<Role> guildRoles = new ArrayList<>();
                for (String roleString : roles) {
                    Role role = null;
                    try {
                        role = member.getGuild().getRoleById(roleString);
                    } catch (NumberFormatException ignore) {
                        List<Role> rolesByName = member.getGuild().getRolesByName(roleString, false);
                        if (!rolesByName.isEmpty()) {
                            role = rolesByName.get(0);
                        }
                    }
                    if (role != null) {
                        guildRoles.add(role);
                    }
                }

                boolean result = (!containsALL) ?
                        guildRoles.stream().anyMatch(member.getRoles()::contains) :
                        new HashSet<>(member.getRoles()).containsAll(guildRoles);

                result = whitelist == result;
                if (!result) {
                    Utils.runOrdersWithFilterSystem(event, getDenyOrders(), tempData);
                }
                return result;
            } else {
                getLogger().info(getId() + " filter cant check roles not from guild");
            }
        }
        return false;
    }
}
