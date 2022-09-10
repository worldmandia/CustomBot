package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Interaction interaction = InteractionUtils.getAllInteractions().get(event.getComponentId());
        Member member = event.getInteraction().getMember();
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());
        if (interaction instanceof BUTTON_INTERACTION button_interaction) {
            for (Action action : button_interaction.getActions()) {
                boolean isReplied = false;
                if (action.isOnlyCommand()) {
                    DTBot.getLogger().warn(action.getId() + " this action only for commands");
                } else {
                    RestAction<?> restAction = null;
                    if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
                        restAction = event.replyEmbeds(new EmbedBuilder()
                                        .setDescription(Utils.placeholder(create_button_embed.getEmbedDescription(), placeholders))
                                        .setTitle(Utils.placeholder(create_button_embed.getEmbedTitle(), placeholders))
                                        .setColor(Utils.decode(create_button_embed.getEmbedColor()))
                                        .build())
                                .setEphemeral(create_button_embed.isEphemeral())
                                .addActionRow(create_button_embed.getButtons());
                        isReplied = true;
                    } else if (action instanceof CREATE_TEXT_CHAT create_text_chat) {
                        try {
                            List<Category> categories = event.getGuild().getCategoriesByName(create_text_chat.getCategoryName(), false);
                            placeholders.put("%counter%", String.valueOf(create_text_chat.getCounter().getAndAdd(1)));
                            restAction = categories.get(0).createTextChannel(Utils.placeholder(create_text_chat.getActionName(), placeholders)).setTopic(Utils.placeholder(create_text_chat.getActionDescription(), placeholders));
                            create_text_chat.getConfig().set("counter", create_text_chat.getCounter().get());
                        } catch (IllegalArgumentException e) {
                            DTBot.getLogger().warn("Not found category" + create_text_chat.getCategoryName());
                        }
                    } else {
                        DTBot.getLogger().warn(action.getId() + " is unknown id");

                    }
                    if (restAction != null) {
                        restAction.queue();
                        if (!isReplied) {
                            event.replyEmbeds(new EmbedBuilder().setTitle(DTBot.getLang().get("embeds.success-button-title", placeholders)).setDescription(DTBot.getLang().get("embeds.success-button-description", placeholders)).build()).setEphemeral(true).queue();
                        }
                    } else {
                        event.replyEmbeds(new EmbedBuilder().setTitle(DTBot.getLang().get("embeds.error-title")).setDescription(DTBot.getLang().get("embeds.error-description", placeholders)).build()).setEphemeral(true).queue();
                    }
                }
            }
        }
    }
}
