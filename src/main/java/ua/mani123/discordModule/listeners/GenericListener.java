package ua.mani123.discordModule.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discordModule.TempData;

public class GenericListener extends ListenerAdapter implements DiscordListener {

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        discordUtils.getDiscordConfigs().getInteractions().forEach(interaction -> interaction.run(event, new TempData()));
    }

}
