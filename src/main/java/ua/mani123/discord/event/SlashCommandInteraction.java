package ua.mani123.discord.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ua.mani123.CBot;
import ua.mani123.Utils;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.interaction.interactionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

import java.util.List;

public class SlashCommandInteraction extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandInteraction commandInteraction = interactionUtils.getCommandByName(event.getName());
        Utils.getPlaceholders().put("interaction-user", event.getUser().getName());
        Utils.getPlaceholders().put("interaction-user-mentioned", event.getUser().getAsMention());
        Utils.getPlaceholders().put("interaction-user-as-tag", event.getUser().getAsTag());

        StringSubstitutor str = new StringSubstitutor(Utils.getPlaceholders());

        List<String> actionIds = commandInteraction.getActionIds();


        if (actionIds != null) {
            for (String actionId: actionIds) {
                if (actionUtils.getActionMap().containsKey(actionId)){
                    actionUtils.getActionMap().get(actionId).runWithPlaceholders(event, str);
                } else {
                    CBot.getLog().warn(actionId + " - not found");
                }
            }
        }
        event.replyEmbeds(new EmbedBuilder().setTitle(str.replace(commandInteraction.getSuccessTitle())).setDescription(str.replace(commandInteraction.getSuccessDescription())).build()).setEphemeral(commandInteraction.successIsEphemeral()).queue();
    }
}
