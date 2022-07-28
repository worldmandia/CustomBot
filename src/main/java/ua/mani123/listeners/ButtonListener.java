package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
                        event.getGuild().createTextChannel(getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-name", "Not found action-name"), event), event.getGuild().getCategoryById(buttonInteraction.getCustoms().get("category")))
                                .setTopic(getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-description", "Not found action-name"), event)).queue();
                        //event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Success").setDescription("You created chanel: " + event.getUser().getAsTag()).build()).setEphemeral(true).queue();

                        buttonInteraction.getCustoms().set("counter", counter.addAndGet(1));
                    }
                    case CREATE_VOICE_CHAT -> {
                        AtomicInteger counter = new AtomicInteger(buttonInteraction.getCustoms().get("counter"));
                        event.getGuild().createVoiceChannel(getWithPlaceholders(counter.get(), buttonInteraction.getCustoms().getOrElse("action-name", "Not found action-name"), event), event.getGuild().getCategoryById(buttonInteraction.getCustoms().get("category"))).queue();
                        buttonInteraction.getCustoms().set("counter", counter.addAndGet(1));
                    }
                    default -> {
                        event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Error").setDescription("Action not found").build()).setEphemeral(true).queue();
                        return;
                    }
                }
                event.replyEmbeds(new EmbedBuilder().setAuthor("Success").setDescription("Your action completed").build()).setEphemeral(true).queue();
            }
        }
    }

    public String getWithPlaceholders(int counter, String s, GenericInteractionCreateEvent event) {
        return s
                .replaceAll("%username%", event.getUser().getName())
                .replaceAll("%counter%", String.valueOf(counter))
                .replaceAll("%data%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
