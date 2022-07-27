package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ua.mani123.DTBot;
import ua.mani123.interaction.ButtonInteraction;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.InteractionType;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        for (Interaction interact : DTBot.getInteractions().get(InteractionType.BUTTON)) {
            if (interact.getId().equals(event.getComponentId())) {
                ButtonInteraction buttonInteraction = (ButtonInteraction) interact;
                switch (buttonInteraction.getActions()) {
                    case CREATE_TEXT_CHAT -> {
                        event.getGuild().createTextChannel(event.getUser().getAsTag(), event.getGuild().getCategoryById(buttonInteraction.getCategory())).queue();
                        event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Success").setDescription("You created chanel: " + event.getUser().getAsTag()).build()).setEphemeral(true).queue();
                    }
                    case CREATE_VOICE_CHAT -> {
                        event.getGuild().createVoiceChannel(event.getUser().getAsTag(), event.getGuild().getCategoryById(buttonInteraction.getCategory())).queue();
                        event.getInteraction().replyEmbeds(new EmbedBuilder().setAuthor("Success").setDescription("You created chanel: " + event.getUser().getAsTag()).build()).setEphemeral(true).queue();
                    }
                }
            }
        }
    }
}
