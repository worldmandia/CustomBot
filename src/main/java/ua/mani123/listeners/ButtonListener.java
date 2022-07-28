package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.interaction.ButtonInteraction;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.InteractionType;

import java.time.LocalDateTime;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (Interaction interact : DTBot.getInteractions().get(InteractionType.BUTTON)) {
            if (interact.getId().equals(event.getComponentId())) {
                ButtonInteraction buttonInteraction = (ButtonInteraction) interact;
                switch (buttonInteraction.getActions()) {
                    case CREATE_TEXT_CHAT -> {
                        int counter = buttonInteraction.getCustoms().get("counter");
                        String title = buttonInteraction.getCustoms().get("action-name");
                        String description = buttonInteraction.getCustoms().get("action-title");
                        event.getGuild().createTextChannel(title.replace("%username%", event.getUser().getName()).replace("%counter%",
                                String.valueOf(counter)), event.getGuild().getCategoryById(buttonInteraction.getCustoms().get("category")))
                                .setTopic(description.replace("%username%", event.getUser().getName()).replace("%date%", LocalDateTime.now().toString())).queue();
                        event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Success").setDescription("You created chanel: " + event.getUser().getAsTag()).build()).setEphemeral(true).queue();
                        counter++;
                        buttonInteraction.getCustoms().set("counter", counter);
                        DTBot.getInteraction().save();
                    }
                    case CREATE_VOICE_CHAT -> {
                        int counter = buttonInteraction.getCustoms().get("counter");
                        event.getGuild().createVoiceChannel(event.getUser().getAsTag() + counter, event.getGuild().getCategoryById(buttonInteraction.getCustoms().get("category"))).queue();
                        event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Success").setDescription("You created chanel: " + event.getUser().getAsTag()).build()).setEphemeral(true).queue();
                        counter++;
                        buttonInteraction.getCustoms().set("counter", counter);
                        DTBot.getInteraction().save();
                    }
                    default ->
                            event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Error").setDescription("Action not found").build()).setEphemeral(true).queue();
                }
            }
        }
    }
}
