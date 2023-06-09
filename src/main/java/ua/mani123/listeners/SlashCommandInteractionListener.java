package ua.mani123.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discordModule.DiscordUtils;

public class SlashCommandInteractionListener extends DiscordListener {

    public SlashCommandInteractionListener(DiscordUtils discordUtils) {
        super(discordUtils);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

    }


}
