package ua.mani123.discordModule.filters;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.Utils;

import java.util.ArrayList;

@Getter
@Setter
public class DISCORD_BOT extends DiscordConfigs.Filter {

    private ArrayList<String> discordBotIds;
    private boolean whitelist;
    public DISCORD_BOT(String type, String id, ArrayList<String> discordBotIds, boolean whitelist, ArrayList<DiscordConfigs.Order> actions) {
        super(type, id);
        this.discordBotIds = discordBotIds;
        this.whitelist = whitelist;
        this.getDenyOrders().addAll(actions);
    }


    @Override
    public boolean canNext(GenericEvent event) {
        boolean answer = whitelist == discordBotIds.contains(event.getJDA().getSelfUser().getId());
        if (!answer) {
            Utils.runOrdersWithFilterSystem(event, getDenyOrders());
        }
        return answer;
    }
}
