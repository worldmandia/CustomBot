package ua.mani123.discordModule.filters;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;

@Getter
@Setter
public class DISCORD_BOT extends DiscordConfigs.Filter {

    private ArrayList<String> discordBotIds;
    private boolean whitelist;
    public DISCORD_BOT(String type, String id, ArrayList<String> discordBotIds, boolean whitelist, ArrayList<DiscordConfigs.Action> actions) {
        super(type, id);
        this.discordBotIds = discordBotIds;
        this.whitelist = whitelist;
        this.getDenyActions().addAll(actions);
    }


    @Override
    public boolean canNext(GenericEvent event) {
        return whitelist == discordBotIds.contains(event.getJDA().getSelfUser().getId());
    }
}
