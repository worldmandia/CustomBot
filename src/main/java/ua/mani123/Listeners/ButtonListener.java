package ua.mani123.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ua.mani123.DTBot;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        //for (Map.Entry<InteractionType, Interaction> buttons: DTBot.getInteractions().entrySet()) {
        //
        //}
        if (event.getComponentId().equals("otheraction")) {
            // soon
        } else {
            event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(DTBot.getLang().getString("embeds.error.button.title", "Not found **embeds.error.button.title**"))
                    .setDescription(DTBot.getLang().getString("embeds.error.button.description", "Not found **embeds.error.button.description**"))
                    .build()).setEphemeral(true).queue();
        }
    }
}
