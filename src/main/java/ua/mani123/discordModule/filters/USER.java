package ua.mani123.discordModule.filters;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.TempData;
import ua.mani123.discordModule.Utils;

import java.util.ArrayList;

@Getter
@Setter
public class USER extends DiscordConfigs.Filter {

    private ArrayList<String> users;
    private boolean whitelist;

    public USER(String type, String id, ArrayList<String> users, boolean whitelist, ArrayList<DiscordConfigs.Order> actions) {
        super(type, id);
        this.users = users;
        this.whitelist = whitelist;
        this.getDenyOrders().addAll(actions);
    }

    @Override
    public boolean canNext(GenericEvent event, TempData tempData) {
        boolean answer = false;
        if (event instanceof Interaction interaction) {
            answer = whitelist == users.contains(interaction.getUser().getId());
            if (!answer) {
                Utils.runOrdersWithFilterSystem(event, getDenyOrders(), tempData);
            }
            return answer;
        } else {
            getLogger().error(getId() + " filter cant run in non interaction");
            return answer;
        }
    }
}
