package ua.mani123.listeners;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ua.mani123.discordModule.DiscordUtils;

@AllArgsConstructor
public class DiscordListener extends ListenerAdapter {

    protected final DiscordUtils discordUtils;

}
