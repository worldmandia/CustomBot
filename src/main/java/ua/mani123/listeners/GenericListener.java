package ua.mani123.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discordModule.DiscordUtils;

public class GenericListener extends DiscordListener {

    public GenericListener(DiscordUtils discordUtils) {
        super(discordUtils);
    }

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        this.discordUtils.getDiscordConfigs().getInteractions().forEach(interaction -> interaction.run(event));
    }

}
