package ua.mani123.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("otheraction")) {
            // soon
        } else {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setAuthor("Error").setDescription("This button is wrong").build()).queue();
        }
    }
}
