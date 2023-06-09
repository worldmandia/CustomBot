package ua.mani123.discordModule.filters;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;

@Getter
@Setter
public class USER extends DiscordConfigs.Filter {

    private ArrayList<String> users;
    private boolean whitelist;

    public USER(String type, String id, ArrayList<String> users, boolean whitelist) {
        super(type, id);
        this.users = users;
        this.whitelist = whitelist;
    }

    @Override
    public boolean canNext(GenericEvent event) {
        if (event instanceof Interaction interaction) {
            return whitelist == users.contains(interaction.getUser().getId());
        }
        return false;
    }
}
