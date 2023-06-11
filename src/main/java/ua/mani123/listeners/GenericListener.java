package ua.mani123.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GenericListener extends ListenerAdapter implements DiscordListener {

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        discordUtils.getDiscordConfigs().getInteractions().forEach(interaction -> interaction.run(event));
    }

}
