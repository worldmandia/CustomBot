package ua.mani123.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import ua.mani123.DTBot;
import ua.mani123.interaction.ButtonInteraction;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.InteractionType;
import ua.mani123.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TicketButton implements Ticket {
    String id;
    String title;
    String description;
    String embedColor;
    List<String> buttonIds;
    List<ButtonInteraction> interactions = new ArrayList<>();

    public TicketButton(String id, String title, String description, String embedColor, List<String> buttonIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.embedColor = embedColor;
        this.buttonIds = buttonIds;
    }

    public MessageEmbed getEmbed() {
        return new EmbedBuilder().setAuthor(title).setDescription(description).setColor(Utils.decode(embedColor)).build();
    }

    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        for (String id : buttonIds) {
            for (Interaction interaction : DTBot.getInteractions().get(InteractionType.BUTTON)) {
                if (interaction.getId().equals(id)) {
                    ButtonInteraction buttonInteraction = (ButtonInteraction) interaction;
                    buttons.add(buttonInteraction.getInteraction());
                    interactions.add(buttonInteraction);
                }
            }
        }
        return buttons;
    }

    public String getId() {
        return id;
    }
}
