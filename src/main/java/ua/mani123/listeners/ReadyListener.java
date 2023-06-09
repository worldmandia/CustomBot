package ua.mani123.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter implements DiscordListener {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        discordUtils.getDiscordConfigs().getInteractions().forEach(interaction -> interaction.init(event.getJDA()));
    }
}
