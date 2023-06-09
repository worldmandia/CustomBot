package ua.mani123.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discordModule.DiscordUtils;

public class ReadyListener extends DiscordListener {

    public ReadyListener(DiscordUtils discordUtils) {
        super(discordUtils);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        discordUtils.getDiscordConfigs().getInteractions().forEach(interaction -> interaction.init(event.getJDA()));
    }
}
