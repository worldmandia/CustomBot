package ua.mani123.discord.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.interaction.interactionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class SlashCommandInteraction extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandInteraction commandInteraction = interactionUtils.getCommandByName(event.getName());
        event.replyEmbeds(new EmbedBuilder().setTitle(commandInteraction.getSuccessTitle()).setDescription(commandInteraction.getSuccessDescription()).build()).queue();
    }
}
