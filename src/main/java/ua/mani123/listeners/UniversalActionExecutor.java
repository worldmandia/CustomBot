package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.requests.RestAction;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.action.actions.CREATE_BUTTON_EMBED;
import ua.mani123.action.actions.CREATE_TEXT_CHAT;
import ua.mani123.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversalActionExecutor {
    public static void use(GenericInteractionCreateEvent event, Action action) {

        RestAction<?> restAction = null;
        Member member = event.getInteraction().getMember();
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("%username-mentioned%", member.getAsMention());
        placeholders.put("%username%", member.getEffectiveName());

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
        } else {
            DTBot.getLogger().warn(action.getId() + " is unknown id");
            return;
        }

        if (restAction == null) return;

        if (event instanceof SlashCommandInteractionEvent commandEvent){
            restAction.submit().whenComplete((v, error) -> {
                if (error != null) {DTBot.getLogger().error(error.getMessage());}
                else commandEvent.replyEmbeds(new EmbedBuilder().setTitle("Success").setDescription("Success commandEvent").build()).setEphemeral(true).queue();
            });
        } else  if (event instanceof ButtonInteractionEvent buttonEvent){
            restAction.submit().whenComplete((v, error) -> {
                if (error != null) {DTBot.getLogger().error(error.getMessage());}
                else buttonEvent.replyEmbeds(new EmbedBuilder().setTitle("Success").setDescription("Success buttonEvent").build()).setEphemeral(true).queue();
            });
        }

    }
}
