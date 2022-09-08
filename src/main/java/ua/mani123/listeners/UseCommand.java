package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.action.actions.CREATE_TEXT_CHAT;
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
            RestAction<?> restAction = null;
            if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
                restAction = event.replyEmbeds(new EmbedBuilder()
                        .setDescription(create_button_embed.getEmbedDescription())
                        .setTitle(create_button_embed.getEmbedTitle())
                        .setColor(Utils.decode(create_button_embed.getEmbedColor()))
                        .build())
                        .setEphemeral(create_button_embed.isEphemeral())
                        .addActionRow(create_button_embed.getButtons());
            } else if (action instanceof CREATE_TEXT_CHAT create_text_chat){
                restAction = event.getGuild().getCategoriesByName(create_text_chat.getCategoryName(), false).get(0).createTextChannel(create_text_chat.getActionName()).setTopic(create_text_chat.getActionDescription());
                create_text_chat.getConfig().set("counter", create_text_chat.getCounter().getAndIncrement());
            }
            else {
                DTBot.getLogger().warn(action.getId() + " is unknown id");

            }
            if (restAction != null){
                restAction.queue();
            }
        }
    }
}
