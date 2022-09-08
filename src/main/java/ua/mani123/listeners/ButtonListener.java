package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.action.actions.CREATE_TEXT_CHAT;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.interactions.BUTTON_INTERACTION;
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.utils.Utils;

import java.util.List;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Interaction interaction = InteractionUtils.getAllInteractions().get(event.getComponentId());
        if (interaction instanceof BUTTON_INTERACTION button_interaction) {
            for (Action action : button_interaction.getActions()) {
                if (action.isOnlyCommand()) {
                    DTBot.getLogger().warn(action.getId() + " this action only for commands");
                } else {
                    RestAction<?> restAction = null;
                    if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
                        restAction = event.replyEmbeds(new EmbedBuilder()
                                        .setDescription(create_button_embed.getEmbedDescription())
                                        .setTitle(create_button_embed.getEmbedTitle())
                                        .setColor(Utils.decode(create_button_embed.getEmbedColor()))
                                        .build())
                                .setEphemeral(create_button_embed.isEphemeral())
                                .addActionRow(create_button_embed.getButtons());
                    } else if (action instanceof CREATE_TEXT_CHAT create_text_chat) {
                        restAction = event.getGuild().getCategoriesByName(create_text_chat.getCategoryName(), false).get(0).createTextChannel(create_text_chat.getActionName()).setTopic(create_text_chat.getActionDescription());
                        create_text_chat.getConfig().set("counter", create_text_chat.getCounter().addAndGet(1));
                    } else {
                        DTBot.getLogger().warn(action.getId() + " is unknown id");

                    }
                    if (restAction != null) {
                        restAction.queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(DTBot.getLang().get("success-button-title")).setDescription(DTBot.getLang().get("success-button-description", List.of("%username-mentioned%", "%username%"), List.of(event.getMember().getAsMention(), event.getMember().getNickname()))).build()).setEphemeral(true).queue());
                    }
                }
            }
        }
    }
}
