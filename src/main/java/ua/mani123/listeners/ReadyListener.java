package ua.mani123.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ReadyListener extends ListenerAdapter implements DiscordListener {

    public static HashMap<String, ArrayList<SlashCommandData>> commandsPerBot = new HashMap<>();
    public static HashMap<String, ArrayList<SlashCommandData>> commandsPerGuild = new HashMap<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        discordUtils.getDiscordConfigs().getInteractions().forEach(interaction -> interaction.init(event.getJDA()));
        event.getJDA().updateCommands().addCommands(commandsPerBot.get(event.getJDA().getSelfUser().getId())).queue();
        commandsPerGuild.forEach((s, slashCommandData) -> Objects.requireNonNull(event.getJDA().getGuildById(s)).updateCommands().addCommands(slashCommandData).queue());
    }
}
