package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.interaction.ButtonInteraction;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.InteractionType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (Interaction interact : DTBot.getInteractions().get(InteractionType.BUTTON)) {
            if (interact.getId().equals(event.getComponentId())) {
                ButtonInteraction buttonInteraction = (ButtonInteraction) interact;
                switch (buttonInteraction.getActions()) {
                    case CREATE_TEXT_CHAT -> {
                        AtomicInteger counter = new AtomicInteger(buttonInteraction.getCustoms().get("counter"));
                        String name = getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-name", "Not found action-name"), event, "null");
                        RestAction<TextChannel> action = event.getGuild().createTextChannel(name, event.getGuild().getCategoryById(buttonInteraction.getCustoms().get("category")))
                                .setTopic(getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-description", "Not found action-name"), event, "null"));
                        action.queue(
                                (channel) -> event.replyEmbeds(new EmbedBuilder()
                                        .setAuthor(getWithPlaceholders(counter.get(), DTBot.getLang().getString("embeds.success-title", "Not found **embeds.success-title**"), event, ("you created " + name)))
                                        .setDescription(getWithPlaceholders(counter.get(), DTBot.getLang().getString("embeds.success-description", "Not found **embeds.success-description**"), event, ("you created " + channel.getManager().getChannel().getAsMention())))
                                        .build()).setEphemeral(true).queue(),
                                (error) -> DTBot.getLogger().error(error.getMessage())
                        );
                        buttonInteraction.getCustoms().set("counter", counter.addAndGet(1));
                        return;
                    }
                    case CREATE_VOICE_CHAT -> {
                        AtomicInteger counter = new AtomicInteger(buttonInteraction.getCustoms().get("counter"));
                        String name = getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-name", "Not found action-name"), event, "null");
                        event.getGuild().createVoiceChannel(getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-name", "Not found action-name"), event, "null"), event.getGuild().getCategoryById(buttonInteraction.getCustoms().get("category"))).queue();
                        buttonInteraction.getCustoms().set("counter", counter.addAndGet(1));
                        event.replyEmbeds(new EmbedBuilder()
                                .setAuthor(getWithPlaceholders(counter.get(), DTBot.getLang().getString("embeds.success-title", "Not found **embeds.success-title**"), event, ("you created " + name)))
                                .setDescription(getWithPlaceholders(counter.get(), DTBot.getLang().getString("embeds.success-description", "Not found **embeds.success-description**"), event, ("you created " + name)))
                                .build()).setEphemeral(true).queue();
                        return;
                    }
                    default -> {
                        event.replyEmbeds(new EmbedBuilder()
                                .setAuthor("Error")
                                .setDescription("Action not found")
                                .build()).setEphemeral(true).queue();
                        return;
                    }
                }
            }
        }
        event.replyEmbeds(new EmbedBuilder()
                .setAuthor("Error")
                .setDescription("This button not found")
                .build()).setEphemeral(true).queue();
    }

    public String getWithPlaceholders(int counter, String s, GenericInteractionCreateEvent event, String action) {
        return s
                .replaceAll("%username-mentioned%", event.getUser().getAsMention())
                .replaceAll("%username%", event.getUser().getName())
                .replaceAll("%counter%", String.valueOf(counter))
                .replaceAll("%data%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .replaceAll("%action%", action);
    }
}
