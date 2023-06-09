package ua.mani123.discordModule.filters;

import net.dv8tion.jda.api.events.GenericEvent;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;

public class DISCORD_BOT extends DiscordConfigs.Filter {

    private final ArrayList<String> discordBotIds;
    private final boolean whitelist;
    public DISCORD_BOT(String type, String id, ArrayList<String> discordBotIds, boolean whitelist) {
        this.setId(id);
        this.setType(type);
        this.discordBotIds = discordBotIds;
        this.whitelist = whitelist;
    }


    @Override
    public boolean canNext(GenericEvent event) {
        if (whitelist) {
            return discordBotIds.contains(event.getJDA().getSelfUser().getId());
        } else {
            return !discordBotIds.contains(event.getJDA().getSelfUser().getId());
        }
    }
}
