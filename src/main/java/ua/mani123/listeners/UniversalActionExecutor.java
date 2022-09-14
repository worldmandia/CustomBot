package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.RestAction;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.action.actions.CHECK_MIN_MAX_FROM_DATABASE;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.action.actions.CREATE_TEXT_CHAT;
import ua.mani123.action.actions.CREATE_VOICE_CHAT;
import ua.mani123.utils.Utils;

import java.util.List;
import java.util.Map;

public class UniversalActionExecutor {

    // for soon feature Integer.parseInt(s.replaceAll("[\\D]", ""))

    public static RestAction<?> use(GenericInteractionCreateEvent event, Action action, Map<String, String> placeholders) {
        RestAction<?> restAction = null;

        if (action instanceof CHECK_MIN_MAX_FROM_DATABASE check_min_max_from_database) {
            int value = DTBot.getDatabase().getInt(event.getGuild() + "." + event.getMember().getId() + "." + check_min_max_from_database.getSection());
            if (!(check_min_max_from_database.getMin() >= value && value >= check_min_max_from_database.getMax())) {
                return null; // need new IReplyCallback().deferReply(true).setEmbeds(new EmbedBuilder().build());
            }
        }

        if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
            restAction = event.getMessageChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setDescription(Utils.placeholder(create_button_embed.getEmbedDescription(), placeholders))
                    .setTitle(Utils.placeholder(create_button_embed.getEmbedTitle(), placeholders))
                    .setColor(Utils.decode(create_button_embed.getEmbedColor()))
                    .build()).addActionRow(create_button_embed.getButtons());
        } else if (action instanceof CREATE_TEXT_CHAT create_text_chat) {
            try {
                List<Category> categories = event.getGuild().getCategoriesByName(create_text_chat.getCategoryName(), false);
                placeholders.put("%counter%", String.valueOf(create_text_chat.getCounter().getAndAdd(1)));
                restAction = categories.get(0).createTextChannel(Utils.placeholder(create_text_chat.getActionName(), placeholders)).setTopic(Utils.placeholder(create_text_chat.getActionDescription(), placeholders));
                create_text_chat.getConfig().set("counter", create_text_chat.getCounter().get());
            } catch (IllegalArgumentException e) {
                DTBot.getLogger().warn("Not found category" + create_text_chat.getCategoryName());
            }
        } else if (action instanceof CREATE_VOICE_CHAT create_voice_chat) {
            List<Category> categories = event.getGuild().getCategoriesByName(create_voice_chat.getCategoryName(), false);
            placeholders.put("%counter%", String.valueOf(create_voice_chat.getCounter().getAndAdd(1)));
            restAction = categories.get(0).createVoiceChannel(Utils.placeholder(create_voice_chat.getActionName(), placeholders));
            create_voice_chat.getConfig().set("counter", create_voice_chat.getCounter().get());
        } else {
            DTBot.getLogger().warn(action.getId() + " is unknown id");
            return null;
        }
        return restAction;
    }
}
