package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
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

import java.util.ArrayList;
import java.util.List;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CustomCommand cmd = CommandUtils.getAllCommands().get(event.getName());
        Member member = event.getInteraction().getMember();
        ArrayList<String> placeholders = new ArrayList<>(List.of("%username-mentioned%", "%username%"));
        ArrayList<String> values = new ArrayList<>();
        values.add(member.getAsMention());
        values.add(member.getNickname());
        for (Action action : cmd.getActions()) {
            if (action.isOnlyTicket()) {
                DTBot.getLogger().warn(action.getId() + " this action only for tickets");
            } else {
                RestAction<?> restAction = null;
                if (action instanceof CREATE_BUTTON_EMBED create_button_embed) {
                    restAction = event.replyEmbeds(new EmbedBuilder()
                                    .setDescription(Utils.placeholder(create_button_embed.getEmbedDescription(), placeholders, values))
                                    .setTitle(Utils.placeholder(create_button_embed.getEmbedTitle(), placeholders, values))
                                    .setColor(Utils.decode(create_button_embed.getEmbedColor()))
                                    .build())
                            .setEphemeral(create_button_embed.isEphemeral())
                            .addActionRow(create_button_embed.getButtons());
                } else if (action instanceof CREATE_TEXT_CHAT create_text_chat) {
                    try {
                        List<Category> categories = event.getGuild().getCategoriesByName(create_text_chat.getCategoryName(), false);
                        placeholders.add("%counter%");
                        values.add(String.valueOf(create_text_chat.getCounter().getAndAdd(1)));
                        restAction = categories.get(0).createTextChannel(Utils.placeholder(create_text_chat.getActionName(), placeholders, values)).setTopic(Utils.placeholder(create_text_chat.getActionDescription(), placeholders, values));
                        create_text_chat.getConfig().set("counter", create_text_chat.getCounter().get());
                    } catch (IllegalArgumentException e){
                        DTBot.getLogger().warn("Not found category" + create_text_chat.getCategoryName());
                    }
                } else {
                    DTBot.getLogger().warn(action.getId() + " is unknown id");
                }
                try{
                    restAction.queue((success) -> event.replyEmbeds(new EmbedBuilder().setTitle(DTBot.getLang().get("success-cmd-title")).setDescription(DTBot.getLang().get("success-cmd-description", placeholders, values)).build()).setEphemeral(true).queue());
                } catch (NullPointerException e){
                    event.replyEmbeds(new EmbedBuilder().setTitle(DTBot.getLang().get("error-title")).setDescription(DTBot.getLang().get("error-description", placeholders, values)).build()).setEphemeral(true).queue();
                }
            }
        }
    }
}
