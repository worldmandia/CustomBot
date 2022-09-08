package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.command.CommandUtils;
import ua.mani123.command.CustomCommand;
import ua.mani123.utils.Utils;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CustomCommand cmd = CommandUtils.getAllCommands().get(event.getName());
        for (Action action : cmd.getActions()) {
            if (action.isOnlyTicket()) {
                DTBot.getLogger().warn(action.getId() + " this action only for tickets");
                return;
            }
            RestAction<InteractionHook> restAction = null;
            if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
                restAction = event.replyEmbeds(new EmbedBuilder()
                        .setDescription(create_button_embed.getEmbedDescription())
                        .setTitle(create_button_embed.getEmbedTitle())
                        .setColor(Utils.decode(create_button_embed.getEmbedColor()))
                        .build())
                        .addActionRow(create_button_embed.getButtons());
            } else {
                DTBot.getLogger().warn(action.getId() + " is unknown id");

            }
            if (restAction != null){
                restAction.queue((channel) -> channel.sendMessage("Nice command").setEphemeral(true).queue());
            }
        }
    }
}
