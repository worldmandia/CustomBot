package ua.mani123.action;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.RestAction;
import ua.mani123.DTBot;
import ua.mani123.action.actions.*;
import ua.mani123.utils.ReplyReason;
import ua.mani123.utils.Utils;

import java.util.List;
import java.util.Map;

public class UniversalActionExecutor {

    /*
     TODO for soon feature Integer.parseInt(s.replaceAll("[\\D]", ""))
   */

    static List<Category> categories = null;
    public static boolean nextSkip = false;

    public static ReplyReason use(GenericInteractionCreateEvent event, botAction action, Map<String, String> placeholders) {
        RestAction<?> restAction;

        try {
            if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
                restAction = event.getMessageChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(Utils.placeholder(create_button_embed.getEmbedDescription(), placeholders, event.getMember())).setTitle(Utils.placeholder(create_button_embed.getEmbedTitle(), placeholders, event.getMember())).setColor(Utils.decode(create_button_embed.getEmbedColor())).build()).addActionRow(create_button_embed.getButtons());
                create_button_embed.getReplyReason().enablePlaceholder(placeholders, event.getMember());
                nextSkip = true;
                return create_button_embed.getReplyReason().setRestActionAndReturnReplyReason(restAction);

            } else if (action instanceof CHECK_MIN_MAX_FROM_DATABASE check_min_max_from_database) {
                int value = DTBot.getDatabase().getInt(event.getGuild().getId() + "." + event.getInteraction().getUser().getId() + "." + check_min_max_from_database.getSection());
                if (!(check_min_max_from_database.getMin() >= value && value >= check_min_max_from_database.getMax())) {
                    check_min_max_from_database.getReplyReason().enablePlaceholder(placeholders, event.getMember());
                    return check_min_max_from_database.getReplyReason();
                }

            } else if (action instanceof CREATE_TEXT_CHAT create_text_chat) {
                placeholders.put("%counter%", String.valueOf(create_text_chat.getCounter().getAndAdd(1)));
                if (categories == null) {
                    restAction = event.getGuild().createTextChannel(Utils.placeholder(create_text_chat.getActionName(), placeholders, event.getMember())).setTopic(Utils.placeholder(create_text_chat.getActionDescription(), placeholders, event.getMember()));
                } else {
                    restAction = categories.get(0).createTextChannel(Utils.placeholder(create_text_chat.getActionName(), placeholders, event.getMember())).setTopic(Utils.placeholder(create_text_chat.getActionDescription(), placeholders, event.getMember()));
                }
                create_text_chat.getConfig().set("counter", create_text_chat.getCounter().get());
                create_text_chat.getReplyReason().enablePlaceholder(placeholders, event.getMember());
                return create_text_chat.getReplyReason().setRestActionAndReturnReplyReason(restAction);

            } else if (action instanceof CREATE_VOICE_CHAT create_voice_chat) {
                placeholders.put("%counter%", String.valueOf(create_voice_chat.getCounter().getAndAdd(1)));
                if (categories == null) {
                    restAction = event.getGuild().createVoiceChannel(Utils.placeholder(create_voice_chat.getActionName(), placeholders, event.getMember()));
                } else {
                    restAction = categories.get(0).createVoiceChannel(Utils.placeholder(create_voice_chat.getActionName(), placeholders, event.getMember()));
                }
                create_voice_chat.getConfig().set("counter", create_voice_chat.getCounter().get());
                create_voice_chat.getReplyReason().enablePlaceholder(placeholders, event.getMember());
                return create_voice_chat.getReplyReason().setRestActionAndReturnReplyReason(restAction);

            } else if (action instanceof SELECT_CATEGORY select_category) {
                categories = event.getGuild().getCategoriesByName(select_category.getCategory(), false);

            } else {
                DTBot.getLogger().warn(action.getId() + " is unknown id");
                return new ReplyReason("Action not found", "Action id: " + action.getId(), true);
            }
        } catch (IllegalArgumentException e) {
            DTBot.getLogger().warn("Not found category" + e.getMessage());
        }
        return new ReplyReason("Error", "Error message", true);
    }

    public static void clear(){
        categories = null;
    }

}
