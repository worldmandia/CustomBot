package ua.mani123.discord.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.discord.interaction.interactionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;
import ua.mani123.Utils;

public class SlashCommandInteraction extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandInteraction commandInteraction = interactionUtils.getCommandByName(event.getName());

        Member member = event.getOption("user").getAsMember();
        Utils.getPlaceholders().put("source-user", member.getUser().getName());
        Utils.getPlaceholders().put("source-user-mentioned", member.getUser().getAsMention());
        Utils.getPlaceholders().put("interaction-user", event.getUser().getName());
        Utils.getPlaceholders().put("interaction-user-mentioned", event.getUser().getAsMention());
        if (member.getVoiceState().isMuted()){
            member.mute(false).queue();
        } else {
            member.mute(true).queue();
        }
        StringSubstitutor str = new StringSubstitutor(Utils.getPlaceholders());
        event.replyEmbeds(new EmbedBuilder().setTitle(str.replace(commandInteraction.getSuccessTitle())).setDescription(str.replace(commandInteraction.getSuccessDescription())).build()).queue();
    }
}
